package com.nemoworks.graphcache.graph;

import graphql.language.Definition;
import graphql.language.ObjectTypeDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphInstance {

    public static List<Definition> definitions = new ArrayList<>();

    public static final Map<String,GraphNode> graphNodeMap = new HashMap<>();

    public GraphInstance(List<Definition> definitions) {
        this.merge(definitions);
    }

    public GraphInstance() {
    }

    public static void merge(List<Definition> definitions){
        GraphInstance.definitions.addAll(definitions);
        GraphInstance.definitions.forEach(definition -> {
            GraphNode node = new GraphNode.Builder((ObjectTypeDefinition) definition).build();
            graphNodeMap.put(((ObjectTypeDefinition) definition).getName(),node);
        });
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        GraphInstance.definitions = definitions;
    }

    public Map<String, GraphNode> getGraphNodeMap() {
        return graphNodeMap;
    }
}
