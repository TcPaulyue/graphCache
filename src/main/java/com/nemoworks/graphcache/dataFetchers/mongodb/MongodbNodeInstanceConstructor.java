package com.nemoworks.graphcache.dataFetchers.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongodbNodeInstanceConstructor implements NodeInstanceConstructor {

    private final MongoTemplate mongoTemplate;
    private static final String collectionName = "jieshixing";

    @Autowired
    public MongodbNodeInstanceConstructor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public JSONObject createNodeInstance(String nodeType, JSONObject data) {
        data.put("nodeType",nodeType);
        return mongoTemplate.insert(data, collectionName);
    }

}
