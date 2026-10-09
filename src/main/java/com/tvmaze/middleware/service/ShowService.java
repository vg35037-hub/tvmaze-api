package com.tvmaze.middleware.service;

import com.tvmaze.middleware.client.TvMazeClient;
import com.tvmaze.middleware.model.document.CommentDocument;
import com.tvmaze.middleware.model.document.ShowDocument;
import com.tvmaze.middleware.model.dto.CommentDto;
import com.tvmaze.middleware.model.dto.ShowSummaryDto;
import com.tvmaze.middleware.repository.CommentRepository;
import com.tvmaze.middleware.repository.ShowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShowService {

    private static final Logger log = LoggerFactory.getLogger(ShowService.class);

    private final TvMazeClient tvMazeClient;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;

    public ShowService(TvMazeClient tvMazeClient,
                       ShowRepository showRepository,
                       CommentRepository commentRepository) {
        this.tvMazeClient = tvMazeClient;
        this.showRepository = showRepository;
        this.commentRepository = commentRepository;
    }

    public List<ShowSummaryDto> searchShows(String query) {
        log.info("Buscando shows con query: '{}'", query);

        List<Map<String, Object>> results = tvMazeClient.searchShows(query);

        if (results == null || results.isEmpty()) {
            log.info("No se encontraron resultados para: '{}'", query);
            return Collections.emptyList();
        }

        List<ShowSummaryDto> shows = results.stream()
                .map(this::mapToShowSummary)
                .toList();

        // Enriquecer con comentarios de MongoDB
        shows.forEach(this::enrichWithComments);

        log.info("Se encontraron {} shows para: '{}'", shows.size(), query);
        return shows;
    }

    public Map<String, Object> getShowById(Integer showId) {
        log.info("Obteniendo show con id: {}", showId);

        // Primero buscar en cache (MongoDB)
        Optional<ShowDocument> cached = showRepository.findById(showId);
        if (cached.isPresent()) {
            log.info("Show {} encontrado en cache MongoDB", showId);
            Map<String, Object> data = cached.get().getData();
            enrichShowMapWithComments(data, showId);
            return data;
        }

        // Si no está en cache, consumir API
        log.info("Show {} no encontrado en cache, consultando TV Maze API", showId);
        Map<String, Object> showData = tvMazeClient.getShowById(showId);

        // Guardar en MongoDB antes de retornar
        ShowDocument document = new ShowDocument(showId, showData);
        showRepository.save(document);
        log.info("Show {} guardado en cache MongoDB", showId);

        // Enriquecer con comentarios
        enrichShowMapWithComments(showData, showId);

        return showData;
    }

    @SuppressWarnings("unchecked")
    private ShowSummaryDto mapToShowSummary(Map<String, Object> searchResult) {
        Map<String, Object> show = (Map<String, Object>) searchResult.get("show");
        if (show == null) {
            return new ShowSummaryDto();
        }

        Integer id = (Integer) show.get("id");
        String name = (String) show.get("name");
        String summary = (String) show.get("summary");
        List<String> genres = (List<String>) show.get("genres");

        // Obtener channel: primero network, si no webchannel
        String channel = extractChannel(show);

        return new ShowSummaryDto(id, name, channel, summary, genres);
    }

    @SuppressWarnings("unchecked")
    private String extractChannel(Map<String, Object> show) {
        Map<String, Object> network = (Map<String, Object>) show.get("network");
        if (network != null && network.get("name") != null) {
            return (String) network.get("name");
        }

        Map<String, Object> webChannel = (Map<String, Object>) show.get("webChannel");
        if (webChannel != null && webChannel.get("name") != null) {
            return (String) webChannel.get("name");
        }

        return null;
    }

    private void enrichWithComments(ShowSummaryDto show) {
        if (show.getId() == null) {
            return;
        }
        List<CommentDto> comments = getCommentsForShow(show.getId());
        if (!comments.isEmpty()) {
            show.setComments(comments);
        }
    }

    private void enrichShowMapWithComments(Map<String, Object> showData, Integer showId) {
        List<CommentDto> comments = getCommentsForShow(showId);
        if (!comments.isEmpty()) {
            showData.put("comments", comments);
        }
    }

    private List<CommentDto> getCommentsForShow(Integer showId) {
        List<CommentDocument> commentDocs = commentRepository.findByShowId(showId);
        return commentDocs.stream()
                .map(doc -> new CommentDto(doc.getComment(), doc.getRating()))
                .toList();
    }
}
