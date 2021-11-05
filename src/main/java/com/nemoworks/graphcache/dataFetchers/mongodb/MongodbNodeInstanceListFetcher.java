package com.nemoworks.graphcache.dataFetchers.mongodb;

import com.alibaba.fastjson.JSONObject;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceListFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbNodeInstanceListFetcher implements DataFetcher<List<JSONObject>>, NodeInstanceListFetcher {
    private final MongoTemplate mongoTemplate;

    private static final String collectionName = "articles";

    @Autowired
    public MongodbNodeInstanceListFetcher(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<JSONObject> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {

        String fieldType = ((GraphQLObjectType)((GraphQLList) dataFetchingEnvironment.getFieldType()).getWrappedType()).getName();
        return queryNodeInstanceList(fieldType);
    }

    @Override
    public List<JSONObject> queryNodeInstanceList(String nodeType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_class").regex(nodeType));
        List<JSONObject> jsonObjects = mongoTemplate.find(query, JSONObject.class, collectionName);
        return jsonObjects;
    }

}