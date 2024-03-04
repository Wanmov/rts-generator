package com.rts.maker.meta.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Mona
 * @description 模板文件配置
 * @created in 2024/03/04
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {
        private String path;

        private List<FileFilterConfig> filterConfigList;

    }
}
