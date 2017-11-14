package com.mangu.crossingactors.utils;


import com.mangu.crossingactors.models.Cast;
import com.mangu.crossingactors.models.Credits;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ModelFactory {
    public static Credits joinCredits(Credits credits1,
                                      com.mangu.crossingactors.models.tv.Credits credits2) {
        Set<Cast> castSet = new LinkedHashSet<>(credits1.getCast());
        List<com.mangu.crossingactors.models.tv.Cast> castSet2 = credits2.getCast();
        List<Cast> castList = new ArrayList<>();
        for (com.mangu.crossingactors.models.tv.Cast movieCast : castSet2) {
            Cast castToAdd = new Cast();
            castToAdd.setTitle(movieCast.getOriginalName());
            castToAdd.setPosterPath(movieCast.getPosterPath());
            castList.add(castToAdd);
        }
        castSet.addAll(castList);
        credits1.setCast(new ArrayList<>(castSet));
        return credits1;
    }
}
