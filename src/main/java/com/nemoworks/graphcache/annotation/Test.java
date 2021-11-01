package com.nemoworks.graphcache.annotation;

import com.nemoworks.graphcache.dataFetchers.DataLoaderHelper;
import com.nemoworks.graphcache.dataFetchers.GraphData;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test{
        public static void main(String[] args) {
//            Set<String> pomXmlPaths = getPomXmlPaths();
            Reflections reflections = new Reflections("com.nemoworks.graphcache.annotation");
            Set<Class<?>> challengeClasses = reflections.getTypesAnnotatedWith(GraphType.class);
            Map challengeClassesMap = challengeClasses.stream().collect(
                    Collectors.toMap(
                            challengeClass -> challengeClass.getAnnotation(GraphType.class).key(),
                            Test::createNewInstanceOfClass
                    )
            );

            challengeClassesMap.forEach(
                    (key, challengeClass) -> System.out.println(key + " = " + challengeClass.toString())
            );
        }
    public static Set<String> getPomXmlPaths() {
        Reflections reflections = new Reflections(new ResourcesScanner());
        return reflections.getResources(Pattern.compile(".*json\\.html"));
    }
        private static <T> T createNewInstanceOfClass(Class<T> someClass) {
            try {
                Method method = someClass.getMethod("query", GraphData.class, DataLoaderHelper.class);
                method.getName();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                return someClass.newInstance();
            } catch (Exception e) {
                return null; //Bad idea but now it's waste of time
            }
        }
}

