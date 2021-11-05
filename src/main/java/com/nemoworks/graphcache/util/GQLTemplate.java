package com.nemoworks.graphcache.util;

public class GQLTemplate {
    //query single node instance
    private static final String QUERY_SINGLE_INSTANCE_PRE = "query_";
    private static final String QUERY_SINGLE_INSTANCE_SUFFIX = "";

    //query node instance list
    private static final String QUERY_INSTANCE_LIST_PRE = "query_";
    private static final String QUERY_INSTANCE_LIST_SUFFIX = "_list";



    public static String querySingleInstance(String graphNodeName){
        return QUERY_SINGLE_INSTANCE_PRE + StringUtil.lowerCase(graphNodeName) + QUERY_SINGLE_INSTANCE_SUFFIX;
    }

    public static String queryInstanceList(String graphNodeName){
        return QUERY_INSTANCE_LIST_PRE + StringUtil.lowerCase(graphNodeName) + QUERY_INSTANCE_LIST_SUFFIX;
    }
}
