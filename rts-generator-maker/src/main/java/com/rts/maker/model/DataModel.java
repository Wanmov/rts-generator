package com.rts.maker.model;


import lombok.Data;

/**
 * 动态模板配置
 */
@Data
public class DataModel {
    /**
     * 是否生成循环
     */
    public boolean loop;
    /**
     * 作者注释
     */
    public String author;
    /**
     * 输出信息
     */
    public String outputText;
}