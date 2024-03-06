package com.rts.maker.meta.template.model;

import com.rts.maker.meta.Meta;
import lombok.Data;

/**
 * @author Mona
 * @description 模板制作器配置
 * @created in 2024/03/05
 */
@Data
public class TemplateMakerConfig {
    private Long id;

    private Meta meta = new Meta();

    private String originProjectPath;

    TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();

    TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();

}
