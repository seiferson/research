package com.seifersonlabs.research.api.response;

import com.seifersonlabs.research.data.doc.Journal;
import com.seifersonlabs.research.data.doc.JournalHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JournalResponse {

    private static final Logger logger = LoggerFactory.getLogger(JournalResponse.class);

    private String id;
    private String author;
    private String content;
    private String title;
    private Date created;
    private Date modified;
    private List<JournalHistory> history;

    public JournalResponse(Journal journal) {
        this.id = Base64.getUrlEncoder().withoutPadding().encodeToString(journal.getId().toByteArray());
        this.author = journal.getAuthor();
        this.created = journal.getCreated();
        this.modified = journal.getModified();
        this.history = journal.getHistory();
        this.content = journal.getContent();
        this.title = journal.getTitle();
    }

    public JournalResponse() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
