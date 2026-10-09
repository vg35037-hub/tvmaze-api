package com.tvmaze.middleware.service;

import com.tvmaze.middleware.model.document.CommentDocument;
import com.tvmaze.middleware.model.dto.CommentDto;
import com.tvmaze.middleware.model.dto.CommentRequestDto;
import com.tvmaze.middleware.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentDto saveComment(CommentRequestDto request) {
        log.info("Guardando comentario para show {}", request.getShowId());

        CommentDocument document = new CommentDocument();
        document.setShowId(request.getShowId());
        document.setComment(request.getComment());
        document.setRating(request.getRating());
        document.setCreatedAt(LocalDateTime.now());

        CommentDocument saved = commentRepository.save(document);
        log.info("Comentario guardado con id: {}", saved.getId());

        return new CommentDto(saved.getComment(), saved.getRating());
    }
}
