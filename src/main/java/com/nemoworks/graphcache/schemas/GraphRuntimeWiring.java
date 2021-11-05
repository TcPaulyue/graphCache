package com.nemoworks.graphcache.schemas;

import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import com.nemoworks.graphcache.dataFetchers.mongodb.MongodbNodeInstanceFetcher;
import com.nemoworks.graphcache.dataFetchers.mongodb.MongodbNodeInstanceListFetcher;
import com.nemoworks.graphcache.dataFetchers.relational.RelationalDataFetcher;
import com.nemoworks.graphcache.graph.GraphNode;
import com.nemoworks.graphcache.util.GQLTemplate;
import com.nemoworks.graphcache.util.StringUtil;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GraphRuntimeWiring {
    private RuntimeWiring runtimeWiring;

    @Autowired
    Connection connection;

    @Autowired
    MongodbNodeInstanceFetcher mongodbNodeInstanceFetcher;

    @Autowired
    MongodbNodeInstanceListFetcher mongodbNodeInstanceListFetcher;

    private static final String QUERY_IN_GRAPHQL = "Query";

    @Autowired
    public GraphRuntimeWiring(){
        runtimeWiring = RuntimeWiring.newRuntimeWiring().build();
    }

    RuntimeWiring getRuntimeWiring() {
        return runtimeWiring;
    }

    void initRuntimeWiring(){
        Map<String, DataFetcher> map = new LinkedHashMap<>();
        runtimeWiring.getDataFetchers().put(QUERY_IN_GRAPHQL,map);
    }
    public void addNewSchemaDataFetcher(GraphNode graphNode) {

        //queryDocument ==>  documentDataFetcher
       // RelationalDataFetcher relationalDataFetcher = new RelationalDataFetcher(connection);
       // this.addNewEntryInQueryDataFetcher(graphNode.getName(), relationalDataFetcher);
        this.addNewEntryInQueryDataFetcher(GQLTemplate.querySingleInstance(graphNode.getName()),mongodbNodeInstanceFetcher);

        this.addNewEntryInQueryDataFetcher(GQLTemplate.queryInstanceList(graphNode.getName()),mongodbNodeInstanceListFetcher);

    }

    void addNewEntryInQueryDataFetcher(String name, DataFetcher dataFetcher){
        runtimeWiring.getDataFetchers().get(QUERY_IN_GRAPHQL).put(name,dataFetcher);
    }

}
