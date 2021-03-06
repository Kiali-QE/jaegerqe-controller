package io.jaegertracing.qe.controller.reporter;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class IdGenerator {

    private static final String _REF_GLOBAL = "global";
    private static final ConcurrentHashMap<String, ArrayList<Integer>> _ID = new ConcurrentHashMap<>();

    public static void clear() {
        _ID.clear();// clear all the id's
    }

    private static Integer get(String key) {
        ArrayList<Integer> idList = _ID.get(key);
        if (idList == null) {
            idList = new ArrayList<>();
        }
        Integer id = nextAvailableId(key);
        // add new id in to list
        idList.add(id);
        return id;
    }

    public static Integer nextAvailableId(String key) {
        ArrayList<Integer> idList = _ID.get(key);
        if (idList == null) {
            idList = new ArrayList<>();
        }
        _ID.put(key, idList);
        // check existing ids
        Integer id = 1;
        while (idList.contains(id)) {
            id++;
        }
        return id;
    }

    public static Integer generate(String key) {
        if (key != null) {
            return get(key);
        }
        return get(_REF_GLOBAL);
    }

    public static void remove(String key, Integer id) {
        ArrayList<Integer> idList = _ID.get(key);
        if (idList != null) {
            idList.remove(id);
        }
    }
}
