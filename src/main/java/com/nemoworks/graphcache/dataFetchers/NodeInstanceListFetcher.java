package com.nemoworks.graphcache.dataFetchers;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface NodeInstanceListFetcher {
    List<JSONObject> queryNodeInstanceList(String nodeType);
}
