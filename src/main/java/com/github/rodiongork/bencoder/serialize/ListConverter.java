package com.github.rodiongork.bencoder.serialize;

import java.util.List;

import com.github.rodiongork.bencoder.represent.ListNode;

public class ListConverter implements ObjectConverter<List, ListNode> {

    private BencSerializer serializer;

    ListConverter(BencSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public ListNode convert(List data) {
        ListNode list = new ListNode();
        for (Object item : data) {
            list.add(serializer.serialize(item));
        }
        return list;
    }
}
