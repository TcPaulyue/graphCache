package com.nemoworks.graphcache.groovy;

import com.alibaba.fastjson.JSONObject;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class DemoDataFetcher implements NodeInstanceFetcher {
    @Override
    public JSONObject queryNodeInstanceById(String id, String nodeType) {
        System.out.println("queryNodeInstanceById");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","tcpaul");
        return jsonObject;
    }
}
