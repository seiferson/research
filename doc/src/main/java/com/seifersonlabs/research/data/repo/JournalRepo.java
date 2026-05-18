package com.seifersonlabs.research.data.repo;

import com.seifersonlabs.research.data.doc.Journal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface JournalRepo extends ReactiveMongoRepository<Journal, ObjectId> {

    Mono<Journal> findByIdAndAuthor(ObjectId id, String author);

    Flux<Journal> findByAuthorOrderByCreated(String author);
}
