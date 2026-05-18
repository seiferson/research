package com.seifersonlabs.research.api.rest;

import com.seifersonlabs.research.api.request.JournalRequest;
import com.seifersonlabs.research.api.response.JournalResponse;
import com.seifersonlabs.research.data.doc.Journal;
import com.seifersonlabs.research.data.repo.JournalRepo;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("/api/v0/journals")
public class JournalAPI {

    private static final Logger logger = LoggerFactory.getLogger(JournalAPI.class);

    @Autowired
    private JournalRepo journalRepo;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<JournalResponse> save(Principal principal, @RequestBody @Valid JournalRequest request) {
        return Mono
                .just(new Journal(principal.getName(), request.getContent(), request.getTitle()))
                .flatMap(journalRepo::save)
                .map(JournalResponse::new);
    }

    @GetMapping
    public Flux<JournalResponse> findAll(Principal principal) {
        return journalRepo
                .findByAuthorOrderByCreated(principal.getName())
                .map(JournalResponse::new);
    }

    @GetMapping("/{id}")
    public Mono<JournalResponse> findById(Principal principal, @PathVariable("id") String id) {
        return Mono
                .fromCallable(() -> new ObjectId(Base64.getUrlDecoder().decode(id)))
                .onErrorMap(
                        IllegalArgumentException.class,
                        error -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                )
                .flatMap(objectId -> journalRepo.findByIdAndAuthor(objectId, principal.getName()))
                .map(JournalResponse::new)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PatchMapping("/{id}")
    public Mono<JournalResponse> update(Principal principal, @PathVariable("id") String id, @RequestBody @Valid JournalRequest request) {
        return Mono
                .fromCallable(() -> new ObjectId(Base64.getUrlDecoder().decode(id)))
                .onErrorMap(
                        IllegalArgumentException.class,
                        error -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .flatMap(objectId -> journalRepo.findByIdAndAuthor(objectId, principal.getName()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(journal -> {
                        journal.createRevision();
                        journal.setContent(request.getContent());
                        journal.setTitle(request.getTitle());
                        journal.setModified(new Date());
                        return journal;
                })
                .flatMap(journalRepo::save)
                .map(JournalResponse::new);
    }
}
