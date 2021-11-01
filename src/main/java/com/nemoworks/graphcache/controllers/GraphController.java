package com.nemoworks.graphcache.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nemoworks.graphcache.schemas.SchemaAPIBuilder;
import com.nemoworks.graphcache.schemas.SchemaController;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.language.Document;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;
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
        JsonObject jsonObject = new Gson().fromJson(query,JsonObject.class);
        String schema = jsonObject.get("query").getAsString();
        if(schema.startsWith("type")){
            this.graphQL = schemaController.addTypeInGraphQL(schema);
            return ResponseEntity.ok(SchemaAPIBuilder.build(schema));
        }else if(schema.startsWith("create")){
            return ResponseEntity.ok("waiting...");
        }else{
            Parser parser = new Parser();
            Document document = parser.parseDocument(schema);
            if(document.getDefinitions().get(0) instanceof OperationDefinition){
                ((OperationDefinition) document.getDefinitions().get(0)).getOperation().name();
            }
            ExecutionResult result = graphQL.execute(schema);
            if(result.getErrors().isEmpty())
                return ResponseEntity.ok(result.getData());
            return ResponseEntity.badRequest().body(result.getErrors());
        }
    }

}