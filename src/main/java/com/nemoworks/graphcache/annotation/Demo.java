package com.nemoworks.graphcache.annotation;

import com.nemoworks.graphcache.dataFetchers.DataLoaderHelper;
import com.nemoworks.graphcache.dataFetchers.GraphData;
import com.nemoworks.graphcache.dataFetchers.GraphDataLoader;

@GraphType(key = "Article")
public class Demo implements GraphDataLoader<String> {
    @Override
    public String query(GraphData graphData, DataLoaderHelper dataLoaderHelper) {
        return null;
    }

    @Override
    public String create(GraphData graphData, DataLoaderHelper dataLoaderHelper) {
        return null;
    }
}