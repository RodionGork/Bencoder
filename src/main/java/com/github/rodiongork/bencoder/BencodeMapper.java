package com.github.rodiongork.bencoder;

import com.github.rodiongork.bencoder.deserialize.BencDeserializer;
import com.github.rodiongork.bencoder.represent.BencGenerator;
import com.github.rodiongork.bencoder.represent.BencNode;
import com.github.rodiongork.bencoder.represent.BencParser;
import com.github.rodiongork.bencoder.serialize.BencSerializer;

public class BencodeMapper {
    
    private BencSerializer serializer = new BencSerializer();
    
    private BencDeserializer deserializer = new BencDeserializer();
    
    public <T> T treeToValue(BencNode node, Class<T> cls) {
        return deserializer.deserialize(node, cls);
    }

    public BencNode valueToTree(Object value) {
        return serializer.serialize(value);
    }
    
    public <T> T readValue(byte[] bytes, Class<T> cls) {
        return treeToValue(BencParser.bytesToTree(bytes), cls);
    }
    
    public byte[] writeValue(Object obj) {
        return BencGenerator.treeToBytes(valueToTree(obj));
    }
    
}

