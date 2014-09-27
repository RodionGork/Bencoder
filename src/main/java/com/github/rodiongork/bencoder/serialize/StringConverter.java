package com.github.rodiongork.bencoder.serialize;

import com.github.rodiongork.bencoder.represent.BytesNode;

public class StringConverter implements ObjectConverter<Object, BytesNode> {

    StringConverter() {
    }

    @Override
    public BytesNode convert(Object value) {
        BytesNode node = new BytesNode();
        if (value instanceof CharSequence) {
            node.setValue(value.toString());
        } else {
            node.setValue((byte[]) value);
        }
        return node;
    }

}
