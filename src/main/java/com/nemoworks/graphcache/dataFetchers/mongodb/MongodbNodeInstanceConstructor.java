package com.nemoworks.graphcache.dataFetchers.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceConstructor;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongodbNodeInstanceConstructor implements DataFetcher<JSONObject>, NodeInstanceConstructor {

    private final MongoTemplate mongoTemplate;
    private static final String collectionName = "articles";

    @Autowired
    public MongodbNodeInstanceConstructor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public JSONObject createNodeInstance(String nodeType, JSONObject data) {
        return mongoTemplate.insert(data, collectionName);
    }

    @Override
    public JSONObject get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {

        JSONObject content = new JSONObject(dataFetchingEnvironment.getArgument("data"));

        return this.createNodeInstance(null,content);
    }
}
