package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BencNode;

public interface NodeConverter<T extends BencNode> {
    
    public <Y> Y convert(T node, Class<Y> cls);
    
}
