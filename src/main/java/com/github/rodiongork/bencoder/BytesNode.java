package com.github.rodiongork.bencoder;

import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class BytesNode implements BencNode {

    private byte[] value;
    
    public byte[] getValue() {
        return Arrays.copyOf(value, value.length);
    }
    
    public void setValue(byte[] v) {
        value = Arrays.copyOf(v, v.length);
    }
    
    public String getValueAsString() {
        return new String(value, StandardCharsets.UTF_8);
    }
    
    public void setValue(String s) {
        value = s.getBytes(StandardCharsets.UTF_8);
    }

}

