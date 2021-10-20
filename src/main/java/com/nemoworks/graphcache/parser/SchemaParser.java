package com.nemoworks.graphcache.parser;

import graphql.language.Definition;
import graphql.language.Document;
import graphql.parser.Parser;

import java.util.List;

public class SchemaParser {
    private static final Parser parser = new Parser();

    public static List<Definition> parse(String schema){
        Document document = parser.parseDocument(schema);
        return document.getDefinitions();
    }


}
