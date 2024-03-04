package com.rts.maker.meta.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * @author Mona
 * @description 文件过滤规则enum
 * @created in 2024/03/04
 */
@Getter
public enum FileFilterRuleEnum {

    CONTAINS("包含", "contains"),
    STARTS_WITH("前缀匹配", "startsWith"),
    ENDS_WITH("后缀匹配", "endsWith"),
    REGEX("正则匹配", "regex"),
    EQUALS("等于", "equals");

    private final String text;
    private final String value;

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     *
     * @param value 值
     * @return {@link FileFilterRuleEnum}
     */
    public static FileFilterRuleEnum getByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (FileFilterRuleEnum fileFilterRuleEnum : FileFilterRuleEnum.values()) {
            if (fileFilterRuleEnum.value.equals(value)) {
                return fileFilterRuleEnum;
            }
        }
        return null;
    }
}
