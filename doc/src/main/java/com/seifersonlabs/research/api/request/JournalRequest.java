package com.seifersonlabs.research.api.request;

import jakarta.validation.constraints.NotBlank;

public class JournalRequest {

    @NotBlank
    private String content;

    @NotBlank
    private String title;

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
