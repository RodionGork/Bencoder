package com.github.rodiongork.bencoder;

import java.io.ByteArrayOutputStream;

public class BencGenerator {
    
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    public void writeTree(BencNode root) {
        if (out == null) {
            throw new IllegalStateException("Generator could not be reused");
        }
        writeUnknownTypeNode(root);
    }
    
    private void writeUnknownTypeNode(BencNode node) {
        if (node instanceof IntNode) {
            
        }
    }
    
    public byte[] output() {
        return out.toByteArray();
    }
    
}
