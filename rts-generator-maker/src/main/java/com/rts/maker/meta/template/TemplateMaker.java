package com.rts.maker.meta.template;

import cn.hutool.Hutool;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.rts.maker.meta.Meta;
import com.rts.maker.meta.enums.FileGenerateTypeEnum;
import com.rts.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Mona
 * @description 模板制作工具
 * @created in 2024/03/04
 */
public class TemplateMaker {
    public static void main(String[] args) {
        // 指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParent() + File.separator + "rts-generator-demo-projects/acm-template");

        // 复制目录
        long id = IdUtil.getSnowflakeNextId();// 雪花算法生成id
        String tempDirPath = projectPath + File.separator + ".temp";
        String tempProjectPath = tempDirPath + File.separator + id;
        if (FileUtil.exist(tempProjectPath)) {
            FileUtil.mkdir(tempProjectPath);
        }
        FileUtil.copy(originalProjectPath, tempProjectPath, true);


        // 一、输入信息
        // 项目基本信息
        String name = "acm-template-pro-generator";
        String description = "ACM 示例模板生成器";

        // 输入文件信息
        // 需要挖坑的项目根路径
        String sourceRootPath = tempProjectPath + File.separator + FileUtil.getLastPathEle(Paths.get(originalProjectPath));
        String fileInputPath = "src/com/rts/acm/MainTemplate.java";
        String fileOutputPath = fileInputPath + ".ftl";

        // 输入模型参数信息
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("${outputText}");
        modelInfo.setType("String");
        modelInfo.setDefaultValue("Sum: ");

        // 二、使用字符串替换、生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        String replacement = String.format("%s", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, "Sum: ", replacement);

        // 三、输出模板文件
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

        // 四、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        // 构造配置参数对象
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);

        Meta.FileConfig fileConfig = new Meta.FileConfig();
        meta.setFileConfig(fileConfig);
        fileConfig.setSourceRootPath(sourceRootPath);


        ArrayList<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        fileConfig.setFiles(fileInfoList);

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        fileInfoList.add(fileInfo);

        Meta.ModelConfig modelConfig = new Meta.ModelConfig();
        meta.setModelConfig(modelConfig);
        ArrayList<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
        modelConfig.setModels(modelInfoList);
        modelInfoList.add(modelInfo);

        // 输出配置文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), metaOutputPath);
    }
}
