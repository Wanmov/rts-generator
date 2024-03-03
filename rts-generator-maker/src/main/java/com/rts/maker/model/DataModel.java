package com.rts.maker.model;


import lombok.Data;

/**
 * 动态模板配置
 */
@Data
public class DataModel {
    /**
     * 是否生成 .gitignore 文件
     */
    public boolean needGit = true;
    /**
     * 是否生成循环
     */
    public boolean loop;

    /**
     * 核心模板
     */
}