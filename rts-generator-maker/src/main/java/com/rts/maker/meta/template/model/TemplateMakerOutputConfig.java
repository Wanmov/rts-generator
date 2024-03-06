package com.rts.maker.meta.template.model;

import lombok.Data;

/**
 * @author Mona
 * @description 模板制作器输出配置
 * @created in 2024/03/06
 */
@Data
public class TemplateMakerOutputConfig {

    // 从未分组文件中移除组内的同名文件
    private boolean removeGroupFilesFromRoot = true;
}