package com.seifersonlabs.research.data.doc;

import java.util.Date;

public class JournalHistory {

    private String content;
    private String title;
    private Date version;

    public JournalHistory(String content, String title) {
        this.content = content;
        this.title = title;
        this.version = new Date();
    }

    public JournalHistory() {}

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
