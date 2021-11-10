package com.nemoworks.graphcache.dataFetchers.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


@Component
public class MongodbNodeInstanceFetcher implements  NodeInstanceFetcher {
    private final MongoTemplate mongoTemplate;

    private static final String collectionName = "jieshixing";

    @Autowired
    public MongodbNodeInstanceFetcher(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public JSONObject queryNodeInstanceById(String id, String nodeType) {
        JSONObject result = mongoTemplate.findById(id, JSONObject.class, collectionName);
        return result;
    }
}
