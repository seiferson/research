package com.seifersonlabs.research.data.doc;

import java.util.Date;

public class JournalHistory {

    private String content;
    private Date version;

    public JournalHistory(String content) {
        this.content = content;
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
}
