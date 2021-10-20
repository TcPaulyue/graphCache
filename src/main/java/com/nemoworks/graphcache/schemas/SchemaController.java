package com.nemoworks.graphcache.schemas;

import com.nemoworks.graphcache.graph.GraphNode;
import com.nemoworks.graphcache.parser.SchemaParser;
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


    @PostConstruct
    public GraphQL createGraphQL(){
        graphTypeRegistry.initSchemaDefinition();
        graphRuntimeWiring.initRuntimeWiring();
        String s = "type User {\n" +
                "    id: String\n" +
                "    name: String\n" +
                "    age: Int\n" +
                "    nationality: String\n" +
                "    createdAt: String\n" +
                "    friends: [User]\n" +
                "    articles: [Article]\n" +
                "}\n" +
                "\n" +
                "type Article {\n" +
                "   id: String\n" +
                "   title: String\n" +
                "   minutesRead: Int\n" +
                "}";
        List<Definition> parse = SchemaParser.parse(s);
        GraphNode node = new GraphNode.Builder((ObjectTypeDefinition) parse.get(1)).build();
        this.addNewTypeAndDataFetcherInGraphQL(node);
        graphTypeRegistry.buildTypeRegistry();
        this.graphQLSchema = schemaGenerator.makeExecutableSchema(graphTypeRegistry.getTypeDefinitionRegistry()
                , graphRuntimeWiring.getRuntimeWiring());
        return  newGraphQL(graphQLSchema).build();
    }

    public GraphQL addTypeInGraphQL(String schema){
        List<Definition> parse = SchemaParser.parse(schema);
        GraphNode node = new GraphNode.Builder((ObjectTypeDefinition) parse.get(0)).build();
        this.addNewTypeAndDataFetcherInGraphQL(node);
        graphTypeRegistry.buildTypeRegistry();
        this.graphQLSchema = schemaGenerator.makeExecutableSchema(graphTypeRegistry.getTypeDefinitionRegistry()
                , graphRuntimeWiring.getRuntimeWiring());
        return  newGraphQL(graphQLSchema).build();
    }

    private void addNewTypeAndDataFetcherInGraphQL(GraphNode graphNode){
        graphTypeRegistry.addGraphNode(graphNode);
        graphRuntimeWiring.addNewSchemaDataFetcher(mongoTemplate,graphNode);
    }
}
