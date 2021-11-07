package com.nemoworks.graphcache.dataFetchers.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MongodbNodeInstanceFetcher implements DataFetcher<JSONObject>, NodeInstanceFetcher {
    private final MongoTemplate mongoTemplate;

    private static final String collectionName = "jieshixing";

    @Autowired
    public MongodbNodeInstanceFetcher(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public JSONObject get(DataFetchingEnvironment dataFetchingEnvironment){
        if(dataFetchingEnvironment.getSource()!=null){
            String name = dataFetchingEnvironment.getFieldDefinition().getName();
            JSONObject source = dataFetchingEnvironment.getSource();
            ArrayList<String> ids = source.getObject(name, ArrayList.class);
            return queryNodeInstanceById(ids.get(0),null);
        }
        String id = dataFetchingEnvironment.getArgument("id");
        String fieldType = ((GraphQLObjectType) dataFetchingEnvironment.getFieldType()).getName();
        return queryNodeInstanceById(id,fieldType);
    }

    @Override
    public JSONObject queryNodeInstanceById(String id, String nodeType) {
        JSONObject result = mongoTemplate.findById(id, JSONObject.class, collectionName);
        return result;
    }
}
