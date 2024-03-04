package com.rts.maker.meta.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.rts.maker.meta.Meta;
import com.rts.maker.meta.enums.FileGenerateTypeEnum;
import com.rts.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
     * @param meta                元信息
     * @param originalProjectPath 原项目路径
     * @param fileInputPath       文件输入路径
     * @param modelInfo           模型信息
     * @param searchStr           搜索字符串
     * @param id                  id
     * @return long
     */
    private static long makeTemplate(Meta meta, String originalProjectPath, String fileInputPath, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id) {

        // 如果id为空 则生成新id
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();// 雪花算法生成id
        }

        // 复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String tempProjectPath = tempDirPath + File.separator + id;
        if (FileUtil.exist(tempProjectPath)) {
            FileUtil.mkdir(tempProjectPath);
            FileUtil.copy(originalProjectPath, tempProjectPath, true);
        }


        // 一、输入信息

        // 输入文件信息
        // 需要挖坑的项目根路径
        String sourceRootPath = tempProjectPath + File.separator + FileUtil.getLastPathEle(Paths.get(originalProjectPath));
        String fileOutputPath = fileInputPath + ".ftl";


        // 二、使用字符串替换、生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;

        // 如果已有模板文件 - 表示不是第一次制作 则在原有模板文件上修改
        String fileContent;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String replacement = String.format("#{%s}", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, searchStr, replacement);

        // 输出模板文件
        FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        // 三、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        // 如果已有配置文件 - 表示不是第一次制作 则在原有配置文件上修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            // 追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = oldMeta.getFileConfig().getFiles();
            fileInfoList.add(fileInfo);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = oldMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);

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

            fileInfoList.add(fileInfo);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            meta.setModelConfig(modelConfig);
            ArrayList<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);

            // 输出配置文件
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), metaOutputPath);
        }
        return id;
    }

    /**
     * 文件信息去重
     *
     * @param fileInfoList 文件信息
     * @return {@link List}<{@link Meta.FileConfig.FileInfo}>
     */
    private static List<Meta.FileConfig.FileInfo> distinctFileInfo(List<Meta.FileConfig.FileInfo> fileInfoList) {
        return new ArrayList<>(fileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, Function.identity(), (o1, o2) -> o2))
                .values());
    }

    /**
     * 模型信息去重
     *
     * @param ModelInfoList 模型信息
     * @return {@link List}<{@link Meta.ModelConfig.ModelInfo}>
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModelInfo(List<Meta.ModelConfig.ModelInfo> ModelInfoList) {
        return new ArrayList<>(ModelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, Function.identity(), (o1, o2) -> o2))
                .values());
    }

    public static void main(String[] args) {
        Meta meta = new Meta();

        // 项目基本信息
        meta.setName("acm-template-pro-generator");
        meta.setDescription("ACM 示例模板生成器");

        // 指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParent() + File.separator + "rts-generator-demo-projects/acm-template");
        String fileInputPath = "src/com/rts/acm/MainTemplate.java";

        // 输入模型参数信息
        // Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        // modelInfo.setFieldName("outputText");
        // modelInfo.setType("String");
        // modelInfo.setDefaultValue("Sum: ");

        // 输入模型参数信息 - 第二个
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        // 替换变量
        // String searchStr = "Sum: ";
        String searchStr = "MainTemplate";


        makeTemplate(meta, originalProjectPath, fileInputPath, modelInfo, searchStr, 1764519250022490112L);
    }

}
