package com.mangu.crossingactors.Utils;


import com.mangu.crossingactors.Model.Result;

import java.util.Comparator;

public class ComparatorFactory {
    private static final Comparator<Result> ACTOR_COMPARATOR = (result, t1) -> result.getName().compareTo(t1.getName());

    public static Comparator<Result> getActorComparator() {
        return ACTOR_COMPARATOR;
    }
}
