package com.github.rodiongork.bencoder.serialize;

import com.github.rodiongork.bencoder.represent.BencNode;

public interface ObjectConverter<X, Y extends BencNode> {

    public Y convert(X value);

}
