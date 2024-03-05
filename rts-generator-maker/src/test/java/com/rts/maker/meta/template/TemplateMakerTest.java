package com.rts.maker.meta.template;

import com.rts.maker.meta.Meta;
import com.rts.maker.meta.template.model.TemplateMakerFileConfig;
import com.rts.maker.meta.template.model.TemplateMakerModelConfig;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;


public class TemplateMakerTest {

    @Test
    public void testMakeTemplateBug1() {
        Meta meta = new Meta();

        // 项目基本信息
        meta.setName("acm-template-pro-generator");
        meta.setDescription("ACM 示例模板生成器");

        // 指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = new File(projectPath).getParent() + File.separator + "rts-generator-demo-projects/springboot-init";

        // 文件参数配置
        String inputFilePath1 = "src/main/resources/application.yml";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        templateMakerFileConfig.setFiles(Collections.singletonList(fileInfoConfig1));

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Collections.singletonList(modelInfoConfig1);
        templateMakerModelConfig.setModelInfoConfigList(modelInfoConfigList);

        long id = TemplateMaker.makeTemplate(meta, originalProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1735281524670181376L);
        System.out.println(id);
    }

    @Test
    public void testMakeTemplateBug2() {
        Meta meta = new Meta();

        // 项目基本信息
        meta.setName("acm-template-pro-generator");
        meta.setDescription("ACM 示例模板生成器");

        // 指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = new File(projectPath).getParent() + File.separator + "rts-generator-demo-projects/springboot-init";

        // 文件参数配置
        String inputFilePath1 = "src/main/java/com/yupi/springbootinit/common";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        templateMakerFileConfig.setFiles(Collections.singletonList(fileInfoConfig1));

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setReplaceText("BaseResponse");
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Collections.singletonList(modelInfoConfig1);
        templateMakerModelConfig.setModelInfoConfigList(modelInfoConfigList);

        long id = TemplateMaker.makeTemplate(meta, originalProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1735281524670181376L);
        System.out.println(id);
    }

}