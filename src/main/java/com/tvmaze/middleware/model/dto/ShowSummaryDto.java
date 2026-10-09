package com.tvmaze.middleware.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowSummaryDto {

    private Integer id;
    private String name;
    private String channel;
    private String summary;
    private List<String> genres;
    private List<CommentDto> comments;

    public ShowSummaryDto() {
    }

    public ShowSummaryDto(Integer id, String name, String channel, String summary, List<String> genres) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.summary = summary;
        this.genres = genres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}
