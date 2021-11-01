package com.nemoworks.graphcache.graph;

import graphql.language.Type;
import graphql.language.TypeName;

import java.util.Arrays;
import java.util.List;

public class Scalars {
    public static List<String> getScalars() {
        return scalars;
    }

    private static final List<String> scalars = Arrays.asList(
            "String"
            ,"Int"
            ,"Boolean"
            ,"Float");
}
