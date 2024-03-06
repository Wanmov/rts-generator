package com.rts.maker.meta.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Mona
 * @description 模板制作器模型配置
 * @created in 2024/03/05
 */
@Data
public class TemplateMakerModelConfig {

    private ModelGroupConfig modelGroupConfig;

    private List<ModelInfoConfig> models;


    @Data
    @NoArgsConstructor
    public static class ModelInfoConfig {
        private String fieldName;
        private String type;
        private String description;
        private Object defaultValue;
        private String abbr;

        // 替换文本
        private String replaceText;
    }

    @Data
    public static class ModelGroupConfig {
        private String groupKey;
        private String groupName;
        private String condition;
        private String description;
        private String type;
    }
}
