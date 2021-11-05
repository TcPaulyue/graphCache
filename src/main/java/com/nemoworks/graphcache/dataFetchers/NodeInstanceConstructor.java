package com.nemoworks.graphcache.dataFetchers;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

public interface NodeInstanceConstructor {
    JSONObject createNodeInstance(String nodeType, JSONObject data);
}
