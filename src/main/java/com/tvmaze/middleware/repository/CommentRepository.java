package com.tvmaze.middleware.repository;

import com.tvmaze.middleware.model.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<CommentDocument, String> {

    List<CommentDocument> findByShowId(Integer showId);
}
