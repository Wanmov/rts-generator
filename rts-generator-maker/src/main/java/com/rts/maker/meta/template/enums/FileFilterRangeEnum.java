package com.rts.maker.meta.template.enums;

import lombok.Getter;

/**
 * @author Mona
 * @description 文件过滤器范围enum
 * @created in 2024/03/04
 */
@Getter
public enum FileFilterRangeEnum {

    FILE_NAME("文件名", "fileName"),
    FILE_CONTENT("文件内容", "fileContent");


    private final String text;

    private final String value;

    FileFilterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }


    /**
     * 根据value获取枚举
     *
     * @param value 值
     * @return {@link FileFilterRangeEnum}
     */
    public static FileFilterRangeEnum getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (FileFilterRangeEnum fileFilterRangeEnum : FileFilterRangeEnum.values()) {
            if (fileFilterRangeEnum.value.equals(value)) {
                return fileFilterRangeEnum;
            }
        }
        return null;
    }
}
