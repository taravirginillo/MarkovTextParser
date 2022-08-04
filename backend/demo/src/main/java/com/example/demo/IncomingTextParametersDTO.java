package com.example.demo;

import javax.validation.constraints.Size;

public class IncomingTextParametersDTO {

    private int prefixSize;

    private int maxOutputSize;

    public IncomingTextParametersDTO(int prefixSize, int maxOutputSize){
        this.prefixSize = prefixSize;
        this.maxOutputSize = maxOutputSize;
    }

    public int getPrefixSize() {
        return prefixSize;
    }

    public void setPrefixSize(int prefixSize) {
        this.prefixSize = prefixSize;
    }

    public int getMaxOutputSize() {
        return maxOutputSize;
    }

    public void setMaxOutputSize(int maxOutputSize) {
        this.maxOutputSize = maxOutputSize;
    }
}
