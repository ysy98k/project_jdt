package com.raising.thread;

import org.mybatis.spring.SqlSessionTemplate;

import java.util.Map;
import java.util.concurrent.Callable;

public class DataUploadInsert implements Callable<Integer> {

    private SqlSessionTemplate sqlSessionTemplate;

    private Map paramMap;

    public DataUploadInsert(SqlSessionTemplate sqlSessionTemplate, Map paramMap){
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.paramMap = paramMap;
    }

    @Override
    public Integer call() throws Exception {
        sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
        return null;
    }
}
