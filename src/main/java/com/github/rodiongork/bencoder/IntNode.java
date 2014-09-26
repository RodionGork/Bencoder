package com.github.rodiongork.bencoder;

public class IntNode implements BencNode {
    
    private long value;

    public IntNode() {
    }

    public IntNode(long v) {
        this();
        value = v;
    }

    public long getValue() {
        return value;
    }
    
    public void setValue(long v) {
        value = v;
    }
    
}

