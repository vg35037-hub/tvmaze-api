package com.tvmaze.middleware.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class TvMazeClient {

    private static final Logger log = LoggerFactory.getLogger(TvMazeClient.class);
    private static final String BASE_URL = "https://api.tvmaze.com";

    private final RestTemplate restTemplate;

    public TvMazeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> searchShows(String query) {
        String url = BASE_URL + "/search/shows?q=" + query;
        log.debug("Consultando TV Maze search: {}", url);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public Map<String, Object> getShowById(Integer showId) {
        String url = BASE_URL + "/shows/" + showId;
        log.debug("Consultando TV Maze show: {}", url);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }
}
