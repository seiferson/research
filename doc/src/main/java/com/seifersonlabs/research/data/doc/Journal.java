package com.seifersonlabs.research.data.doc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class Journal {

    public Journal() {}

    public Journal(String author, String content) {
        this.setHistory(new ArrayList<>());
        this.setCreated(new Date());
        this.setModified(new Date());
        this.setAuthor(author);
        this.setContent(content);
    }

    @Id
    private ObjectId id;

    @NotBlank
    @Size(min = 4, max = 12)
    private String author;

    @NotBlank
    private String content;

    @NotNull
    private Date created;

    @NotNull
    private Date modified;

    @NotNull
    private List<JournalHistory> history;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public List<JournalHistory> getHistory() {
        return history;
    }

    public void setHistory(List<JournalHistory> history) {
        this.history = history;
    }

    public void createRevision() {
        this.getHistory().add(new JournalHistory(this.getContent()));
    }
}
