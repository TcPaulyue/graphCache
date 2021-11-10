package com.nemoworks.graphcache.groovy.impl;

import com.google.common.collect.Maps;
import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import com.nemoworks.graphcache.groovy.GroovyScriptService;
import com.nemoworks.graphcache.groovy.GroovyScriptTemplate;
import com.nemoworks.graphcache.util.MD5Util;
import com.nemoworks.graphcache.util.VelocityTemplate;
import groovy.lang.GroovyClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomNodeInstanceConstructorFactory implements GroovyScriptService {
    @Autowired
    GroovyScriptTemplate groovyScriptTemplate;

    @Autowired
    GroovyClassLoader groovyClassLoader;

    private Map<String, Map<String,Class<NodeInstanceFetcher>>> customNodeInstanceFetcherMap = Maps.newConcurrentMap();
    private Map<String,String> nameAndMd5 = Maps.newHashMap();

    @Override
    public void parseAndCache(String nodeType, String apiName, String script) {
        String scriptBuilder = groovyScriptTemplate.getScript("ScriptTemplate.groovyTemplate");
        Map<String,String> map = new HashMap<>();
        String scriptClassName = apiName;
        map.put("name",scriptClassName);
        //这一部分String的获取逻辑进行可配置化
        map.put("customCode",script);
        String fullScript = VelocityTemplate.build(scriptBuilder, map);
        String oldMd5 = nameAndMd5.get(nodeType+apiName);
        String newMd5 = MD5Util.getStringMD5(fullScript);
        if (oldMd5 != null && oldMd5.equals(newMd5)) {
            return;
        }
        Class<NodeInstanceFetcher> aClass = groovyClassLoader.parseClass(fullScript);
        customNodeInstanceFetcherMap.computeIfAbsent(nodeType, k -> new HashMap<>());
        customNodeInstanceFetcherMap.get(nodeType).put(apiName,aClass);
        nameAndMd5.put(nodeType+apiName,newMd5);
    }

    @Override
    public NodeInstanceFetcher getInstance(String nodeType,String name) {
        Class<NodeInstanceFetcher> aClass = customNodeInstanceFetcherMap.get(nodeType).get(name);
        if(aClass == null){
            throw new IllegalArgumentException("load NodeInstanceFetcher failed");
        }
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
