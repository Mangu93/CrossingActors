package com.mangu.crossingactors.Utils;


import com.mangu.crossingactors.Model.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ComparatorFactory {
    public static final String MOVIE_POSTER_KEY = "MOVIE_POSTER";
    private static final Comparator<Result> ACTOR_COMPARATOR = (result, t1) -> result.getName().compareTo(t1.getName());

    public static Comparator<Result> getActorComparator() {
        return ACTOR_COMPARATOR;
    }

    public static class CoincidenceMap {
        private Map<String, Integer> coincidenceMap; // Movie -> Coincidences
        private Set<String> alreadyProcessed; //To avoid errors on the API, like duplicates movies for the same actor
        private Map<String, String> posters; //Movie -> Posters

        public CoincidenceMap() {
            this.coincidenceMap = new TreeMap<>();
            this.alreadyProcessed = new HashSet<>();
            this.posters = new TreeMap<>();
        }

        public void restartCoincidences() {
            this.coincidenceMap.clear();
        }

        public void restartProcessed() {
            this.alreadyProcessed.clear();
        }

        public void add(String movie, String poster_path) {
            if (coincidenceMap.containsKey(movie) && !alreadyProcessed.contains(movie)) {
                Integer count = coincidenceMap.get(movie) + 1;
                posters.put(movie, poster_path);
                coincidenceMap.put(movie, count);
            } else {
                coincidenceMap.put(movie, 1);
                alreadyProcessed.add(movie);
            }
        }

        @SuppressWarnings("Convert2streamapi")
        public List<String> getPosters() {
            List<String> results = new ArrayList<>();
            for (Map.Entry<String, String> entry : posters.entrySet()) {
                results.add(entry.getValue());
            }
            return results;
        }

        public List<String> getCoincidences(int cut) {
            List<String> results = new ArrayList<>();
            for (Object movie : coincidenceMap.keySet()) {
                Integer value = coincidenceMap.get(movie);
                if (value == cut) {
                    results.add((String) movie);
                } else {
                    posters.remove(movie);
                }
            }
            return results;
        }
    }
}
