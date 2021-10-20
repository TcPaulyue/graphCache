package com.nemoworks.graphcache.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nemoworks.graphcache.schemas.SchemaAPIBuilder;
import com.nemoworks.graphcache.schemas.SchemaController;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GraphController {

    private GraphQL graphQL;

    private final SchemaController schemaController;

    @Autowired
    public GraphController(SchemaController schemaController){
        this.schemaController = schemaController;
        this.graphQL = schemaController.createGraphQL();
    }

    @PostMapping(value = "/graphql")
    public ResponseEntity query(@RequestBody String query){
        JSONObject jsonObject = JSON.parseObject(query);
        String schema = jsonObject.getString("query");
        if(schema.startsWith("type")){
            this.graphQL = schemaController.addTypeInGraphQL(schema);
            return ResponseEntity.ok(SchemaAPIBuilder.build(schema));
        }else if(schema.startsWith("create")){
            return ResponseEntity.ok("waiting...");
        }else{
            ExecutionResult result = graphQL.execute(schema);
            if(result.getErrors().isEmpty())
                return ResponseEntity.ok(result.getData());
            return ResponseEntity.badRequest().body(result.getErrors());
        }
    }

}