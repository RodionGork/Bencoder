package com.github.rodiongork.bencoder.serialize;

import java.lang.reflect.Array;
import java.util.List;

import com.github.rodiongork.bencoder.represent.ListNode;

public class ListConverter implements ObjectConverter<Object, ListNode> {

    private BencSerializer serializer;

    ListConverter(BencSerializer serializer) {
        this.serializer = serializer;
    }

    public ListNode convert(Object data) {
        ListNode list = new ListNode();
        if (data instanceof List) {
            convertList(list, (List) data);
        } else {
            convertArray(list, data);
        }
        return list;
    }
    
    private void convertList(ListNode list, List data) {
        for (Object item : data) {
            addItem(list, item);
        }
    }
    
    private void convertArray(ListNode list, Object array) {
        for (int i = 0, n = Array.getLength(array); i < n; i++) {
            addItem(list, Array.get(array, i));
        }
    }
    
    private void addItem(ListNode list, Object item) {
        if (item == null) {
            throw new BencSerializer.SerializationException("Null in the list could not be serialized");
        }
        list.add(serializer.serialize(item));
    }
}
