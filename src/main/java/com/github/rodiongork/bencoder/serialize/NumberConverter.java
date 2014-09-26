package com.github.rodiongork.bencoder.serialize;

import com.github.rodiongork.bencoder.represent.IntNode;

public class NumberConverter implements ObjectConverter<Number, IntNode> {

    NumberConverter() {
    }

    @Override
    public IntNode convert(Number value) {
        return new IntNode(value.longValue());
    }

}
