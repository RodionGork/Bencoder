package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BencNode;
import com.github.rodiongork.bencoder.represent.BytesNode;
import com.github.rodiongork.bencoder.represent.IntNode;

public class BencDeserializer {
    
    private IntNodeConverter intNodeConverter = new IntNodeConverter();
    
    private BytesNodeConverter bytesNodeConverter = new BytesNodeConverter();
    
    public <T extends BencNode, Y> Y deserialize(T node, Class<Y> cls) {
        return ((NodeConverter<T>) chooseConverter(node.getClass())).convert(node, cls);
    }
    
    private <T extends BencNode> NodeConverter<T> chooseConverter(Class<T> cls) {
        if (IntNode.class.isAssignableFrom(cls)) {
            return (NodeConverter<T>) intNodeConverter;
        } else if (BytesNode.class.isAssignableFrom(cls)) {
            return (NodeConverter<T>) bytesNodeConverter;
        }
        return null;
    }
    
    public static class DeserializationException extends RuntimeException {
        DeserializationException(String msg) {
            super(msg);
        }
    }
    
}
