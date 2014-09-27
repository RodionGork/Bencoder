package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BencNode;
import com.github.rodiongork.bencoder.represent.BytesNode;
import com.github.rodiongork.bencoder.represent.DictNode;
import com.github.rodiongork.bencoder.represent.IntNode;
import com.github.rodiongork.bencoder.represent.ListNode;

public class BencDeserializer {
    
    private IntNodeConverter intNodeConverter = new IntNodeConverter();
    
    private BytesNodeConverter bytesNodeConverter = new BytesNodeConverter();
    
    private ListNodeConverter listNodeConverter = new ListNodeConverter(this);
    
    private DictNodeConverter dictNodeConverter = new DictNodeConverter(this);
    
    public <T extends BencNode, Y> Y deserialize(T node, Class<Y> cls) {
        return ((NodeConverter<T>) chooseConverter(node)).convert(node, cls);
    }
    
    public <T extends BencNode> Object deserializeCollection(T node, TypeRef ref) {
        if (node instanceof ListNode) {
            return listNodeConverter.convert((ListNode) node, ref);
        } else if (node instanceof DictNode) {
            return dictNodeConverter.convert((DictNode) node, ref);
        }
        throw new DeserializationException("Wrong node type for collection deserialization");
    }
    
    private <T extends BencNode> NodeConverter<T> chooseConverter(T node) {
        if (node instanceof IntNode) {
            return (NodeConverter<T>) intNodeConverter;
        } else if (node instanceof BytesNode) {
            return (NodeConverter<T>) bytesNodeConverter;
        } else if (node instanceof ListNode) {
            return (NodeConverter<T>) listNodeConverter;
        } else if (node instanceof DictNode) {
            return (NodeConverter<T>) dictNodeConverter;
        }
        return null;
    }
    
    public static class DeserializationException extends RuntimeException {
        DeserializationException(String msg) {
            super(msg);
        }
    }
    
}
