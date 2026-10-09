package com.tvmaze.middleware.controller;

import com.tvmaze.middleware.model.dto.CommentDto;
import com.tvmaze.middleware.model.dto.CommentRequestDto;
import com.tvmaze.middleware.model.dto.ShowSummaryDto;
import com.tvmaze.middleware.service.CommentService;
import com.tvmaze.middleware.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;
    private final CommentService commentService;

    public ShowController(ShowService showService, CommentService commentService) {
        this.showService = showService;
        this.commentService = commentService;
    }

    /**
     * A - Buscar shows por query.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ShowSummaryDto>> searchShows(@RequestParam("q") String query) {
        List<ShowSummaryDto> results = showService.searchShows(query);
        return ResponseEntity.ok(results);
    }

    /**
     * B - Obtener detalle de un show por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getShowById(@PathVariable("id") Integer id) {
        Map<String, Object> show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }

    /**
     * C - Agregar comentario y rating a un show.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CommentRequestDto request) {
        // Asegurar que el showId del path se use
        request.setShowId(id);
        CommentDto saved = commentService.saveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
