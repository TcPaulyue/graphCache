package com.nemoworks.graphcache.schemas;

import com.nemoworks.graphcache.dataFetchers.DocumentDataFetcher;
import com.nemoworks.graphcache.graph.GraphNode;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GraphRuntimeWiring {
    private RuntimeWiring runtimeWiring;

    @Autowired
    public GraphRuntimeWiring(){
        runtimeWiring = RuntimeWiring.newRuntimeWiring().build();
    }

    RuntimeWiring getRuntimeWiring() {
        return runtimeWiring;
    }

    void initRuntimeWiring(){
        Map<String, DataFetcher> map = new LinkedHashMap<>();
        runtimeWiring.getDataFetchers().put("Query",map);
    }
    public void addNewSchemaDataFetcher(MongoTemplate mongoTemplate, GraphNode graphNode) {

        //queryDocument ==>  documentDataFetcher
        DocumentDataFetcher documentDataFetcher = new DocumentDataFetcher(mongoTemplate);
        documentDataFetcher.setDocumentCollectionName(graphNode.getName());
        this.addNewEntryInQueryDataFetcher(graphNode.getName(), documentDataFetcher);
    }

    void addNewEntryInQueryDataFetcher(String name, DataFetcher dataFetcher){
        runtimeWiring.getDataFetchers().get("Query").put(lowerCase(name),dataFetcher);
    }
    private String lowerCase(String str){
        return str.substring(0,1).toLowerCase() + str.substring(1);
    }

}
