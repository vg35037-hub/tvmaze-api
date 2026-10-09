package com.tvmaze.middleware.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "shows")
public class ShowDocument {

    @Id
    private Integer id;

    private Map<String, Object> data;

    private LocalDateTime cachedAt;

    public ShowDocument() {
    }

    public ShowDocument(Integer id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
        this.cachedAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public LocalDateTime getCachedAt() {
        return cachedAt;
    }

    public void setCachedAt(LocalDateTime cachedAt) {
        this.cachedAt = cachedAt;
    }
}
