package com.nemoworks.graphcache.groovy;

import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;

public interface GroovyScriptService {
    public void parseAndCache(String nodeType,String apiName,String script);

    public NodeInstanceFetcher getInstance(String nodeType,String name);
}
