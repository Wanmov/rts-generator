package com.rts.maker.meta.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.rts.maker.meta.Meta;
import com.rts.maker.meta.enums.FileGenerateTypeEnum;
import com.rts.maker.meta.enums.FileTypeEnum;
import com.rts.maker.meta.template.model.TemplateMakerFileConfig;
import com.rts.maker.meta.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Mona
 * @description 模板制作工具
 * @created in 2024/03/04
 */
public class TemplateMaker {
    /**
     * 制作模板
     *
     * @param meta                     元信息
     * @param originalProjectPath      原项目路径
     * @param id                       id
     * @param templateMakeFileConfig   模板文件配置
     * @param templateMakerModelConfig 模板制作器模型配置
     * @return long
     */
    public static long makeTemplate(Meta meta, String originalProjectPath, TemplateMakerFileConfig templateMakeFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id) {

        // 如果id为空 则生成新id
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();// 雪花算法生成id
        }

        // 复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String tempProjectPath = tempDirPath + File.separator + id;

        // 是否为首次制作
        // 如果首次制作则复制原始项目到临时目录
        if (!FileUtil.exist(tempProjectPath)) {
            FileUtil.mkdir(tempProjectPath);
            FileUtil.copy(originalProjectPath, tempProjectPath, true);
        }

        // 遍历模型进行多轮替换
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = templateMakerModelConfig.getModelInfoConfigList();
        // 转换为 ModelInfo
        List<Meta.ModelConfig.ModelInfo> inputModelList = modelInfoConfigList.stream().map(modelConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());

        // 本次新增的模型配置列表
        List<Meta.ModelConfig.ModelInfo> newModeInfoList = new ArrayList<>();

        // 如果是模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            Meta.ModelConfig.ModelInfo groupModelInfo = getModelInfo(modelGroupConfig, inputModelList);
            newModeInfoList.add(groupModelInfo);
        } else {
            // 不分组 - 直接添加所有的
            newModeInfoList.addAll(inputModelList);
        }

        // 输入文件信息
        // 需要挖坑的项目根路径
        String sourceRootPath = tempProjectPath + File.separator + FileUtil.getLastPathEle(Paths.get(originalProjectPath));

        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakeFileConfig.getFiles();

        // 遍历输入文件
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {

            String inputFileAbsolutePath = sourceRootPath + File.separator + fileInfoConfig.getPath();
            // 得到过滤后的文件列表
            List<File> fileList = FileFilter.doFileFilter(inputFileAbsolutePath, fileInfoConfig.getFilterConfigList());
            // 不处理已经产生的ftl文件
            fileList = fileList.stream().filter(file -> !file.getAbsolutePath().endsWith(".ftl")).collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }

        // 如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakeFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            Meta.FileConfig.FileInfo groupFileInfo = getFileInfo(fileGroupConfig, newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }

        // 三、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        // 如果已有配置文件 - 表示不是第一次制作 则在原有配置文件上修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            // 追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = oldMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = oldMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModeInfoList);

            // 去重
            oldMeta.getFileConfig().setFiles(distinctFileInfo(fileInfoList));
            oldMeta.getModelConfig().setModels(distinctModelInfo(modelInfoList));

            // 输出配置文件
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(oldMeta), metaOutputPath);
        } else {

            // 构造配置参数对象
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            meta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);

            ArrayList<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);

            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            meta.setModelConfig(modelConfig);
            ArrayList<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModeInfoList);

            // 输出配置文件
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), metaOutputPath);
        }
        return id;
    }

    /**
     * 获取文件信息
     *
     * @param fileGroupConfig 文件组配置
     * @param newFileInfoList 新增文件信息列表
     * @return {@link Meta.FileConfig.FileInfo}
     */
    private static Meta.FileConfig.FileInfo getFileInfo(TemplateMakerFileConfig.FileGroupConfig fileGroupConfig, List<Meta.FileConfig.FileInfo> newFileInfoList) {
        String groupKey = fileGroupConfig.getGroupKey();
        String groupName = fileGroupConfig.getGroupName();
        String condition = fileGroupConfig.getCondition();

        // 文件组配置信息
        Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
        groupFileInfo.setCondition(condition);
        groupFileInfo.setGroupKey(groupKey);
        groupFileInfo.setGroupName(groupName);
        // 放入文件组
        groupFileInfo.setFiles(newFileInfoList);
        return groupFileInfo;
    }

    /**
     * 获取模型信息
     *
     * @param modelGroupConfig 模型组配置
     * @param inputModelList   输入模型信息列表
     * @return {@link Meta.ModelConfig.ModelInfo}
     */
    private static Meta.ModelConfig.ModelInfo getModelInfo(TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig, List<Meta.ModelConfig.ModelInfo> inputModelList) {
        String groupKey = modelGroupConfig.getGroupKey();
        String groupName = modelGroupConfig.getGroupName();
        String condition = modelGroupConfig.getCondition();

        // 模型组配置信息
        Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
        groupModelInfo.setGroupKey(groupKey);
        groupModelInfo.setGroupName(groupName);
        groupModelInfo.setCondition(condition);

        // 放入同一分组
        groupModelInfo.setModels(inputModelList);
        return groupModelInfo;
    }


    /**
     * 制作模板文件
     *
     * @param sourceRootPath           源根路径
     * @param inputFile                输入文件
     * @param templateMakerModelConfig 模板制作器模型配置
     * @return {@link Meta.FileConfig.FileInfo}
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile) {
        // 要挖坑的文件绝对路径（用于制作模板）
        String fileInputAbsolutePath = inputFile.getAbsolutePath();
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        // 文件输入输出相对路径（用于生成配置）
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";

        // 如果已有模板文件 - 表示不是第一次制作 则在原有模板文件上修改
        String fileContent;
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if (hasTemplateFile) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfo : templateMakerModelConfig.getModelInfoConfigList()) {
            // 不是分组
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", modelInfo.getFieldName());
            } else {
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}", groupKey, modelInfo.getFieldName());
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfo.getReplaceText(), replacement);
        }


        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        // 是否修改了文件内容
        boolean contentEquals = fileContent.equals(newFileContent);
        // 文件不存在
        if (!hasTemplateFile) {
            // 不需要挖坑
            if (contentEquals) {
                // 输出 = 输入
                fileInfo.setOutputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                // 文件不存在 需要挖坑
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        } else if (!contentEquals) {
            // 文件存在 需要挖坑
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    /**
     * 文件信息去重
     *
     * @param fileInfoList 文件信息
     * @return {@link List}<{@link Meta.FileConfig.FileInfo}>
     */
    private static List<Meta.FileConfig.FileInfo> distinctFileInfo(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 策略: 同组文件merge 不同分组保留

        // 1.以groupKey为key
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyInfoListMap = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));

        // 2.同组文件merge
        HashMap<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();

            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, Function.identity(), (o1, o2) -> o2))
                    .values());
            // 使用新的 group 配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);

            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        // 3.将文件分组添加到结果列表
        ArrayList<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4.将未分组文件添加到结果列表
        resultList.addAll(new ArrayList<>(fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, Function.identity(), (o1, o2) -> o2)
                ).values()));
        return resultList;
    }

    /**
     * 模型信息去重
     *
     * @param modelInfoList 模型信息
     * @return {@link List}<{@link Meta.ModelConfig.ModelInfo}>
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModelInfo(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        // 策略：同分组内模型 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey)
                );

        // 2. 同组内的模型配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(
                            Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        // 3. 将模型分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        // 4. 将未分组的模型添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }
}
