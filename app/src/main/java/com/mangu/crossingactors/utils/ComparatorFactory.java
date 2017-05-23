package com.mangu.crossingactors.utils;


import com.mangu.crossingactors.models.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ComparatorFactory {
    public static final String MOVIE_POSTER_KEY = "MOVIE_POSTER";
    private static final Comparator<Result> ACTOR_COMPARATOR =
            (result, t1) -> result.getName().compareTo(t1.getName());

    @SuppressWarnings("unused")
    public static Comparator<Result> getActorComparator() {
        return ACTOR_COMPARATOR;
    }

    public static class CoincidenceMap {
        private final Map<String, Integer> mCoincidenceMap; // Movie -> Coincidences
        //To avoid errors on the API, like duplicates movies for the same actor
        private final Set<String> mAlreadyProcessed;
        private final Map<String, String> mPosters; //Movie -> Posters

        public CoincidenceMap() {
            this.mCoincidenceMap = new TreeMap<>();
            this.mAlreadyProcessed = new HashSet<>();
            this.mPosters = new TreeMap<>();
        }

        public void restartCoincidences() {
            this.mCoincidenceMap.clear();
        }

        public void restartProcessed() {
            this.mAlreadyProcessed.clear();
        }

        public void restartPosters() {
            this.mPosters.clear();
        }
        public void add(String movie, String posterPath) {
            if (mCoincidenceMap.containsKey(movie) &&
                    !mAlreadyProcessed.contains(movie)) {
                Integer count = mCoincidenceMap.get(movie) + 1;
                mPosters.put(movie, posterPath);
                mCoincidenceMap.put(movie, count);
            } else {
                mCoincidenceMap.put(movie, 1);
                mAlreadyProcessed.add(movie);
            }
        }

        @SuppressWarnings("Convert2streamapi")
        public List<String> getPosters() {
            List<String> results = new ArrayList<>();
            for (Map.Entry<String, String> entry : mPosters.entrySet()) {
                results.add(entry.getValue());
            }
            return results;
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        public List<String> getCoincidences(int cut) {
            List<String> results = new ArrayList<>();
            for (Object movie : mCoincidenceMap.keySet()) {
                Integer value = mCoincidenceMap.get(movie);
                if (value == cut) {
                    results.add((String) movie);
                } else {
                    mPosters.remove(movie);
                }
            }
            return results;
        }
    }
}
