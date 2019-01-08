package com.lgz.grace.api.utils.pojo;

import java.util.Map;

/**
 * Created by lgz on 2019/1/8.
 */

public class RequestParam {

    private Map<String,Object> paramMap;

    private Boolean fileFlag;

    private FileMap fileMap;

    public RequestParam(){

    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Boolean getFileFlag() {
        return fileFlag;
    }

    public void setFileFlag(Boolean fileFlag) {
        this.fileFlag = fileFlag;
    }

    public FileMap getFileMap() {
        return fileMap;
    }

    public void setFileMap(FileMap fileMap) {
        this.fileMap = fileMap;
    }
}
