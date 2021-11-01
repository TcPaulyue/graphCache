package com.nemoworks.graphcache.dataFetchers;

import com.alibaba.fastjson.JSONObject;

public class MyDataLoader implements GraphDataLoader<JSONObject>{
    @Override
    public JSONObject query(GraphData graphData, DataLoaderHelper dataLoaderHelper) {
        return null;
    }

    @Override
    public JSONObject create(GraphData graphData, DataLoaderHelper dataLoaderHelper) {
        return null;
    }
}
