package com.nemoworks.graphcache.dataFetchers;

public interface GraphDataLoader<T> {

    T query(GraphData graphData,DataLoaderHelper dataLoaderHelper);

    T create(GraphData graphData,DataLoaderHelper dataLoaderHelper);
}
