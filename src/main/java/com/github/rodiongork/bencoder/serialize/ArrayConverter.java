package com.github.rodiongork.bencoder.serialize;

import java.lang.reflect.Array;

import com.github.rodiongork.bencoder.represent.ListNode;

public class ArrayConverter implements ObjectConverter<Object, ListNode> {

    private BencSerializer serializer;

    ArrayConverter(BencSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public ListNode convert(Object array) {
        ListNode list = new ListNode();
        for (int i = 0, n = Array.getLength(array); i < n; i++) {
            list.add(serializer.serialize(Array.get(array, i)));
        }
        return list;
    }
}
