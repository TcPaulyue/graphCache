package com.nemoworks.graphcache.dataFetchers;

import com.alibaba.fastjson.JSONObject;

public interface NodeInstanceFetcher {
    JSONObject queryNodeInstanceById(String id, String nodeType);
}
