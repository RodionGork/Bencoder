package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BytesNode;

public class BytesNodeConverter implements NodeConverter<BytesNode> {
    
    BytesNodeConverter() {
    }
    
    @Override
    public <Y> Y convert(BytesNode node, Class<Y> cls) {
        if (cls.equals(String.class)) {
            return (Y) node.getValueAsString();
        } else if (cls.equals(byte[].class)) {
            return (Y) node.getValue();
        }
        throw new BencDeserializer.DeserializationException(
                "Bytestring should be deserialized to String or byte array");
    }
    
}
