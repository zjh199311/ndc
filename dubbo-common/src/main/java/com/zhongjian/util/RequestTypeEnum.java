package com.zhongjian.util;

/**
 * thub cn.lifewallet.thub.common.constants.enums : 请求类型 Created by roy on
 * 16/12/14.
 */
public enum RequestTypeEnum {
    /**
     * XML
     */
    XML("XML", "xml"),
    /**
     * JSON
     */
    JSON("JSON", "json");
    private String key;
    private String des;

    private RequestTypeEnum(String key, String des) {
        this.key = key;
        this.des = des;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
