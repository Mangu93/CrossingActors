package com.mangu.crossingactors.Utils;


import com.mangu.crossingactors.Model.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Singleton;

public class ComparatorFactory {
    private static final Comparator<Result> ACTOR_COMPARATOR = (result, t1) -> result.getName().compareTo(t1.getName());

    public static Comparator<Result> getActorComparator() {
        return ACTOR_COMPARATOR;
    }

    public static class CoincidenceMap {
        private Map<String, Integer> coincidenceMap;

        public CoincidenceMap() {
            this.coincidenceMap = new HashMap<>();
        }

        public void restart() {
            this.coincidenceMap.clear();
        }

        public void add(String movie) {
            if(coincidenceMap.containsKey(movie)) {
                Integer count = coincidenceMap.get(movie) + 1;
                coincidenceMap.put(movie, count);
            }else {
                coincidenceMap.put(movie, 1);
            }
        }
        public void bulkAdd(List<String> list) {
            for(String s: list) {
                add(s);
            }
        }
        public List<String> getCoincidences(int cut) {
            List<String> results = new ArrayList<>();
            for (Object movie : coincidenceMap.keySet()) {
                Integer value = coincidenceMap.get(movie);
                if (value == cut) {
                    results.add((String)movie);
                }
            }
            return results;
        }
    }
}
