package com.tvmaze.middleware.model.dto;

public class CommentDto {

    private String comment;
    private Integer rating;

    public CommentDto() {
    }

    public CommentDto(String comment, Integer rating) {
        this.comment = comment;
        this.rating = rating;
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
