package com.nemoworks.graphcache.schemas;

import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import com.nemoworks.graphcache.dataFetchers.mongodb.MongodbNodeInstanceConstructor;
import com.nemoworks.graphcache.dataFetchers.mongodb.MongodbNodeInstanceFetcher;
import com.nemoworks.graphcache.dataFetchers.mongodb.MongodbNodeInstanceListFetcher;
import com.nemoworks.graphcache.dataFetchers.relational.RelationalDataFetcher;
import com.nemoworks.graphcache.graph.GraphNode;
import com.nemoworks.graphcache.util.GQLTemplate;
import com.nemoworks.graphcache.util.StringUtil;
import graphql.language.ListType;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.HashMap;
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

    @Autowired
    MongodbNodeInstanceConstructor mongodbNodeInstanceConstructor;

    private static final String QUERY_IN_GRAPHQL = "Query";

    private static final String MUTATION_IN_GRAPHQL = "Mutation";

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
        runtimeWiring.getDataFetchers().put(MUTATION_IN_GRAPHQL,new LinkedHashMap<>());
    }
    public void addNewSchemaDataFetcher(GraphNode graphNode) {

        //queryDocument ==>  documentDataFetcher
       // RelationalDataFetcher relationalDataFetcher = new RelationalDataFetcher(connection);
       // this.addNewEntryInQueryDataFetcher(graphNode.getName(), relationalDataFetcher);
        this.addNewEntryInQueryDataFetcher(GQLTemplate.querySingleInstance(graphNode.getName()),mongodbNodeInstanceFetcher);

        this.addNewEntryInQueryDataFetcher(GQLTemplate.queryInstanceList(graphNode.getName()),mongodbNodeInstanceListFetcher);

        this.addNewEntryInQueryDataFetcher(GQLTemplate.createNodeInstance(graphNode.getName()),mongodbNodeInstanceConstructor);

        if(!graphNode.getLinkedTypeMap().isEmpty()){
            Map<String,DataFetcher> dataFetcherMap = new HashMap<>();
            graphNode.getLinkedTypeMap().keySet().forEach(field->{
                Type nodeInstanceType = graphNode.getLinkedTypeMap().get(field);
                if(nodeInstanceType instanceof ListType){
                    dataFetcherMap.put(field,mongodbNodeInstanceListFetcher);
                }else{
                    dataFetcherMap.put(field,mongodbNodeInstanceFetcher);
                }
            });
            this.addDataFetchers(graphNode.getName(),dataFetcherMap);
        }
        //this.addNewEntryInMutation(GQLTemplate.createNodeInstance(graphNode.getName()),mongodbNodeInstanceConstructor);
    }

    void addNewEntryInQueryDataFetcher(String name, DataFetcher dataFetcher){
        runtimeWiring.getDataFetchers().get(QUERY_IN_GRAPHQL).put(name,dataFetcher);
    }

    void addNewEntryInMutation(String name,DataFetcher dataFetcher){
        runtimeWiring.getDataFetchers().get(MUTATION_IN_GRAPHQL).put(name,dataFetcher);
    }

    void addDataFetchers(String name, Map<String, DataFetcher> dataFetcherMap){
        runtimeWiring.getDataFetchers().put(name,dataFetcherMap);
    }
}
