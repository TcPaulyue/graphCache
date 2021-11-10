package com.nemoworks.graphcache.groovy;

import com.nemoworks.graphcache.dataFetchers.NodeInstanceFetcher;
import com.nemoworks.graphcache.util.VelocityTemplate;
import groovy.lang.GroovyClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class dataFetcherConstructor {
    @Autowired
    GroovyClassLoader groovyClassLoader;

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {
        ConcurrentHashMap<String,String> concurrentHashMap = new ConcurrentHashMap(128);
        final String path = "classpath*:*.groovyTemplate";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Arrays.stream(resolver.getResources(path))
                .parallel()
                .forEach(resource -> {
                    try {
                        String fileName = resource.getFilename();
                        InputStream input = resource.getInputStream();
                        InputStreamReader reader = new InputStreamReader(input);
                        BufferedReader br = new BufferedReader(reader);
                        StringBuilder template = new StringBuilder();
                        for (String line; (line = br.readLine()) != null; ) {
                            template.append(line).append("\n");
                        }
                        concurrentHashMap.put(fileName, template.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        String scriptBuilder = concurrentHashMap.get("ScriptTemplate.groovyTemplate");
        Map<String,String> map = new HashMap<>();
        String scriptClassName = "testGroovy";
        map.put("name",scriptClassName);
        //这一部分String的获取逻辑进行可配置化
        String strategyLogicUnit = " System.out.println(\"queryNodeInstanceById\");\n" +
                "        return null;";
        map.put("customCode",strategyLogicUnit);
        String s = VelocityTemplate.build(scriptBuilder, map);


        GroovyClassLoader loader = new GroovyClassLoader();
        Class<NodeInstanceFetcher> aClass = loader.parseClass(s);
        NodeInstanceFetcher o = (NodeInstanceFetcher) aClass.getDeclaredConstructor().newInstance();
        o.queryNodeInstanceById(null,null);
    }
}
