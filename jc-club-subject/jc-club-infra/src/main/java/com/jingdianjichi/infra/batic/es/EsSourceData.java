package com.jingdianjichi.infra.batic.es;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 返回数据
 */
@Data
public class EsSourceData implements Serializable {

    private String docId;

    private Map<String, Object> data;

}
