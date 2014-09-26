package com.github.rodiongork.bencoder.serialize;

import com.github.rodiongork.bencoder.represent.BytesNode;

public class StringConverter implements ObjectConverter<CharSequence, BytesNode> {

    StringConverter() {
    }

    @Override
    public BytesNode convert(CharSequence value) {
        return new BytesNode(value.toString());
    }

}
