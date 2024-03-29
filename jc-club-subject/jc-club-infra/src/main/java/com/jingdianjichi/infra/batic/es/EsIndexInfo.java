package com.jingdianjichi.infra.batic.es;

import lombok.Data;

import java.io.Serializable;

/**
 * 索引类
 */
@Data
public class EsIndexInfo implements Serializable {

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 索引名称
     */
    private String indexName;

}
