package com.tvmaze.middleware.repository;

import com.tvmaze.middleware.model.document.ShowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<ShowDocument, Integer> {
}
