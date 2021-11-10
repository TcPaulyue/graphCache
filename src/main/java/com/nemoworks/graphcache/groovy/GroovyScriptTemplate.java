package com.nemoworks.graphcache.groovy;

import com.nemoworks.graphcache.util.VelocityTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroovyScriptTemplate  implements InitializingBean {

    private static final Map<String, String> SCRIPT_TEMPLATE_MAP = new ConcurrentHashMap<>();

    public  String  getScript(String fileName){
        String template = SCRIPT_TEMPLATE_MAP.get(fileName);
        return template;
    }

    private void scriptTemplate() throws IOException {
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
                        SCRIPT_TEMPLATE_MAP.put(fileName, template.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.scriptTemplate();
    }
}