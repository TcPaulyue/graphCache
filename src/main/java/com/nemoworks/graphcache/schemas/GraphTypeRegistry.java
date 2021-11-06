package com.nemoworks.graphcache.schemas;

import com.nemoworks.graphcache.graph.GraphNode;
import com.nemoworks.graphcache.util.GQLTemplate;
import com.nemoworks.graphcache.util.StringUtil;
import graphql.language.*;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GraphTypeRegistry {
    private final TypeDefinitionRegistry typeDefinitionRegistry;

    public static Map<String, Map<String, Type>> typeDefinitionsMap = new HashMap<>();

    private final List<FieldDefinition> fieldDefinitionListInQuery = new ArrayList<>();

    @Autowired
    private GraphTypeRegistry() {
        this.typeDefinitionRegistry = new TypeDefinitionRegistry();
    }

    TypeDefinitionRegistry getTypeDefinitionRegistry() {
        return typeDefinitionRegistry;
    }


    void initSchemaDefinition() {
        SchemaDefinition.Builder builder = SchemaDefinition.newSchemaDefinition();
        OperationTypeDefinition operationTypeDefinition = new OperationTypeDefinition("query", new TypeName("Query"));
        SchemaDefinition schemaDefinition = builder.operationTypeDefinition(operationTypeDefinition).build();
        typeDefinitionRegistry.add(schemaDefinition);
    }

    void buildTypeRegistry(){
        typeDefinitionRegistry.getType("Query").ifPresent(typeDefinition -> {
            if(typeDefinition instanceof ObjectTypeDefinition)
                typeDefinitionRegistry.remove(typeDefinition);
        });
        ObjectTypeDefinition query = ObjectTypeDefinition.newObjectTypeDefinition().name("Query").fieldDefinitions(fieldDefinitionListInQuery).build();
        typeDefinitionRegistry.add(query);
    }

    public void addGraphNode(GraphNode graphNode){
        if (!typeDefinitionsMap.keySet().contains(graphNode.getName())) {

            typeDefinitionsMap.put(graphNode.getName(),graphNode.getTypeMap());

            this.addTypeDefinition(graphNode.getName(), graphNode.getTypeMap());

            this.addDocumentTypeInQuery(graphNode.getName());

            this.addDocumentListTypeInQuery(graphNode.getName());

            this.addCreateNodeInstanceInQuery(graphNode.getName(),graphNode.getInputTypeMap());

        }
    }

    //在GraphQL的Schema中的Query类中增加一个访问定义的对象的字段
    private void addDocumentTypeInQuery(String name){
        List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();
        inputValueDefinitions.add(new InputValueDefinition("id",new TypeName("String")));
        //orderDocument(id:String):OrderDocument
        this.addFieldDefinitionsInQueryType(GQLTemplate.querySingleInstance(name)
                ,new TypeName(name)
                ,inputValueDefinitions);
    }

    private void addDocumentListTypeInQuery(String name){
        this.addFieldDefinitionsInQueryType(GQLTemplate.queryInstanceList(name),new ListType(new TypeName(name)),
                new ArrayList<>());
    }

    private void addCreateNodeInstanceInQuery(String name,Map<String,Type> typeMap){
        List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();
        typeMap.forEach((key, value) -> inputValueDefinitions.add(new InputValueDefinition(key, value)));
        InputObjectTypeDefinition inputObjectTypeDefinition = InputObjectTypeDefinition.newInputObjectDefinition()
                .name(GQLTemplate.inputTypeForNodeInstance(name))
                .inputValueDefinitions(inputValueDefinitions).build();
        typeDefinitionRegistry.add(inputObjectTypeDefinition);

        List<InputValueDefinition> inputValueDefinition = new ArrayList<>();
        inputValueDefinition.add(new InputValueDefinition("data",new TypeName(GQLTemplate.inputTypeForNodeInstance(name))));

        this.addFieldDefinitionsInQueryType(GQLTemplate.createNodeInstance(name)
                ,new TypeName(name)
                ,inputValueDefinition);
    }


    void addInputObjectTypeDefinition(String name, Map<String, Type> typeMap) {

        List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();
        typeMap.forEach((key, value) -> inputValueDefinitions.add(new InputValueDefinition(key, value)));
        InputObjectTypeDefinition inputObjectTypeDefinition = InputObjectTypeDefinition.newInputObjectDefinition().name(name)
                .inputValueDefinitions(inputValueDefinitions).build();
        typeDefinitionRegistry.add(inputObjectTypeDefinition);
    }

    void addFieldDefinitionsInQueryType(String name, Type type, List<InputValueDefinition> inputValueDefinitions) {
        FieldDefinition definition = FieldDefinition.newFieldDefinition().inputValueDefinitions(inputValueDefinitions)
                .name(name).type(type).build();
        fieldDefinitionListInQuery.add(definition);
    }


    void addTypeDefinition(String name, Map<String, Type> typeMap) {
        typeDefinitionRegistry.add(newObjectTypeDefinition(name, newFieldDefinitions(typeMap)));
    }

    private List<FieldDefinition> newFieldDefinitions(Map<String, Type> typeMap) {
        List<FieldDefinition> fieldDefinitions = new ArrayList<>();
        typeMap.forEach((name, Type) -> fieldDefinitions.add(new FieldDefinition(name, Type)));
        return fieldDefinitions;
    }

    private ObjectTypeDefinition newObjectTypeDefinition(String name, List<FieldDefinition> fieldDefinitions) {
        ObjectTypeDefinition.Builder builder = ObjectTypeDefinition.newObjectTypeDefinition();
        return builder.name(name).fieldDefinitions(fieldDefinitions).build();
    }

}
