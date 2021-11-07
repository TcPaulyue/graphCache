package com.nemoworks.graphcache.schemas;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nemoworks.graphcache.graph.GraphInstance;
import com.nemoworks.graphcache.graph.GraphNode;
import com.nemoworks.graphcache.parser.DSLParser;
import com.nemoworks.graphcache.parser.JsonSchemaParseStrategy;
import graphql.GraphQL;
import graphql.language.Definition;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static graphql.GraphQL.newGraphQL;

@Component
public class SchemaController {

    final GraphRuntimeWiring graphRuntimeWiring;

    final GraphTypeRegistry graphTypeRegistry;

    private GraphQLSchema graphQLSchema;

    private final SchemaGenerator schemaGenerator;

    @Autowired
    MongoTemplate mongoTemplate;

    public SchemaController(GraphRuntimeWiring graphRuntimeWiring, GraphTypeRegistry graphTypeRegistry) {
        this.graphRuntimeWiring = graphRuntimeWiring;
        this.graphTypeRegistry = graphTypeRegistry;
        this.schemaGenerator = new SchemaGenerator();
    }


    //@PostConstruct
    public GraphQL createGraphQL(){
        graphTypeRegistry.initSchemaDefinition();
        graphRuntimeWiring.initRuntimeWiring();
        List<JSONObject> graphNodes = mongoTemplate.findAll(JSONObject.class, "graphNode");
        graphNodes.forEach(graphNode->{
            String schema = graphNode.getString("schemaDefinition");
            List<Definition> parse = DSLParser.parseSchema(schema);
            GraphNode node = new GraphNode.Builder((ObjectTypeDefinition) parse.get(0)).build();
            GraphInstance.merge(node);
            this.addNewTypeAndDataFetcherInGraphQL(node);
        });
        graphTypeRegistry.buildTypeRegistry();
        this.graphQLSchema = schemaGenerator.makeExecutableSchema(graphTypeRegistry.getTypeDefinitionRegistry()
                , graphRuntimeWiring.getRuntimeWiring());
        return  newGraphQL(graphQLSchema).build();
    }

    public GraphQL addTypeInGraphQL(String schema){
        List<Definition> parse = DSLParser.parseSchema(schema);
        //GraphInstance.merge(parse);
        this.addTypeInDB(schema);
        GraphNode node = new GraphNode.Builder((ObjectTypeDefinition) parse.get(0)).build();
        this.addNewTypeAndDataFetcherInGraphQL(node);
        graphTypeRegistry.buildTypeRegistry();
        this.graphQLSchema = schemaGenerator.makeExecutableSchema(graphTypeRegistry.getTypeDefinitionRegistry()
                , graphRuntimeWiring.getRuntimeWiring());
        return  newGraphQL(graphQLSchema).build();
    }


    private void addNewTypeAndDataFetcherInGraphQL(GraphNode graphNode){
        graphTypeRegistry.addGraphNode(graphNode);
        graphRuntimeWiring.addNewSchemaDataFetcher(graphNode);
    }

    private void addTypeInDB(String schema){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schemaDefinition",schema);
        mongoTemplate.insert(jsonObject,"graphNode");
    }
}
