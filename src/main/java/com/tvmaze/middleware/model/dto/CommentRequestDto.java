package com.tvmaze.middleware.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentRequestDto {

    private Integer showId;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comment;

    @NotNull(message = "El rating es obligatorio")
    @Min(value = 0, message = "El rating mínimo es 0")
    @Max(value = 5, message = "El rating máximo es 5")
    private Integer rating;

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
