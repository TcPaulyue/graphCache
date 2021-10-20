package com.nemoworks.graphcache.graph;

import graphql.language.Definition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;

import java.util.HashMap;
import java.util.Map;

public class GraphNode {

    private String id;

    private String name;

    private Definition definition;

    private Map<String, Type> typeMap = new HashMap<>();

    public GraphNode(String name, Definition definition, Map<String, Type> typeMap) {
        this.name = name;
        this.definition = definition;
        this.typeMap = typeMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public Map<String, Type> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<String, Type> typeMap) {
        this.typeMap = typeMap;
    }

    public static final class Builder{
        private String name;
        private Definition definition;
        private Map<String, Type> typeMap = new HashMap<>();

        public Builder(ObjectTypeDefinition definition){
            this.name = definition.getName();
            this.definition = definition;
            definition.getFieldDefinitions().forEach(fieldDefinition -> {
                typeMap.put(fieldDefinition.getName(),fieldDefinition.getType());
            });
        }

        public GraphNode build(){
            return new GraphNode(name,definition,typeMap);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Definition getDefinition() {
            return definition;
        }

        public void setDefinition(Definition definition) {
            this.definition = definition;
        }

        public Map<String, Type> getTypeMap() {
            return typeMap;
        }

        public void setTypeMap(Map<String, Type> typeMap) {
            this.typeMap = typeMap;
        }
    }
}
