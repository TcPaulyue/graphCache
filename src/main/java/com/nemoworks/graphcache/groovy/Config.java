package com.nemoworks.graphcache.groovy;

import groovy.lang.GroovyClassLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public GroovyClassLoader groovyClassLoader(){
        return new GroovyClassLoader();
    }

}
