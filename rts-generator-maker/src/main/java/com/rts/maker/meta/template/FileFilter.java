package com.rts.maker.meta.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.rts.maker.meta.template.enums.FileFilterRangeEnum;
import com.rts.maker.meta.template.enums.FileFilterRuleEnum;
import com.rts.maker.meta.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mona
 * @description 文件过滤器
 * @created in 2024/03/04
 */
public class FileFilter {

    /**
     * 对文件或者文件夹进行过滤,返回对应的文件列表
     *
     * @param filePath             文件路径
     * @param fileFilterConfigList 文件过滤器配置列表
     * @return {@link List}<{@link File}>
     */
    public static List<File> doFileFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {
        // 根据路径获取文件
        return FileUtil.loopFiles(filePath).stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());
    }

    /**
     * 单文件过滤器
     *
     * @param fileFilterConfigList 文件过滤器配置列表
     * @param file                 文件
     * @return boolean
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        // 所有过滤器校验的结果
        boolean result = true;

        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            // 范围
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getByValue(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }
            String content = fileName;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            // 规则
            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getByValue(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }
            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }

            if (!result) {
                return false;
            }
        }
        return true;
    }
}
