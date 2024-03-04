package com.rts.maker.meta.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mona
 * @description 文件过滤器配置
 * @created in 2024/03/04
 */
@Data
@Builder
public class FileFilterConfig {

    /** 范围 */
    private String range;

    /** 规则 */
    private String rule;

    /** 过滤值 */
    private String value;
}
