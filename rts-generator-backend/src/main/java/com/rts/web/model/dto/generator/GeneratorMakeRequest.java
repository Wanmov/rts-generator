package com.rts.web.model.dto.generator;

import com.rts.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Mona
 * @description 发电机发出请求
 * @created in 2024/03/11
 */
@Data
public class GeneratorMakeRequest implements Serializable {
    /** Zip文件路径 */
    private String zipFilePath;
    /** 元 */
    private Meta meta;
    private static final long serialVersionUID = 1L;
}