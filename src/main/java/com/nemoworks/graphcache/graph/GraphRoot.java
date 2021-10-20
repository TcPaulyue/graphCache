package com.nemoworks.graphcache.graph;

import graphql.language.FieldDefinition;
import graphql.language.ListType;

public class GraphRoot {
    String id;
    String name;
    FieldDefinition definition;
    String node;
    RootType rootType;

    public GraphRoot(String name, FieldDefinition definition, String node) {
        this.name = name;
        this.definition = definition;
        this.node = node;
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

    public FieldDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(FieldDefinition definition) {
        this.definition = definition;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public RootType getRootType() {
        return rootType;
    }

    public void setRootType(RootType rootType) {
        this.rootType = rootType;
    }

    public static final class Builder{
        private FieldDefinition definition;
        private String name;
        private String nodeType;

        protected Builder(){

        }

        protected Builder(FieldDefinition definition){
            this.definition = definition;
            this.name = definition.getName();
            if(definition.getType() instanceof ListType){
                this.nodeType = ((ListType) definition.getType()).getType().toString();
            }
            else this.nodeType = definition.getType().toString();
        }

        public GraphRoot build(){
            return new GraphRoot(name,definition,nodeType);
        }
    }
}
