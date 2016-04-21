package com.yamobil.ag.yamobilization.External;

import java.util.Iterator;
import java.util.List;

public class CollectionUtils {

    private CollectionUtils() {}

    public static String join(List<String> s, String delimiter)
    {
        if (s == null || s.isEmpty()) return "";
        Iterator<String> iter = s.iterator();
        StringBuilder builder = new StringBuilder(iter.next());
        while(iter.hasNext()) {
            builder.append(delimiter).append(iter.next());
        }
        return builder.toString();
    }

}
