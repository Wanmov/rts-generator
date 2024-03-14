package com.rts.web.model.dto.generator;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Mona
 * @description 缓存代码生成器
 * @created in 2024/03/14
 */
@Data
public class GeneratorCacheRequest implements Serializable {
    /** 生成器的 id */
    private Long id;
    private static final long serialVersionUID = 1L;
}