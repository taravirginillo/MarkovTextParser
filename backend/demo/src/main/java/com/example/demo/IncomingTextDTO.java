package com.example.demo;

import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class IncomingTextDTO {

    @NotNull
    @Size(min=2, message="{validation.text.size_too_short}")
    @Size(max=4000,message="{validation.text.size_too_long}")
    private String text;

    @Size(min=1, message="{validation.prefixSize.size_too_short}")
    private int prefixSize = 1;

    @Size(min=1, message="{validation.maxOutputSize.size_too_short}")
    private int maxOutputSize = 10;

    public int getPrefixSize() {
        return prefixSize;
    }

    public void setPrefixSize(int prefixSize) {
        this.prefixSize = prefixSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMaxOutputSize() {
        return maxOutputSize;
    }

    public void setMaxOutputSize(int maxOutputSize) {
        this.maxOutputSize = maxOutputSize;
    }
}
