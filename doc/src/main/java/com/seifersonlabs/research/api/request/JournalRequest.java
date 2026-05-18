package com.seifersonlabs.research.api.request;

import jakarta.validation.constraints.NotBlank;

public class JournalRequest {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
