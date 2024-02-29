package com.rts.generator;

import com.rts.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 生成器
 */
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("test");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("最终の求和结果：");
        doGenerate(mainTemplateConfig);
    }

    /**
     * 生成静态文件和动态文件
     *
     * @param model
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Object model) throws TemplateException, IOException {
        // 获取当前模块目录 generator-basic 路径，取决于打开的工程目录！
        String projectPath = System.getProperty("user.dir");
        // 输入路径
        String inputPath = new File(projectPath + File.separator + "rts-generator-demo-projects/acm-template").getAbsolutePath();
        // 生成静态文件
        StaticGenerator.copyFilesByRecursive(inputPath, projectPath);//直接生成在根目录


        // 生成动态文件，会覆盖部分已生成的静态文件
        String dynamicInputPath = projectPath + File.separator + "rts-generator-basic" + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutputPath = projectPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicGenerator.doGenerator(dynamicInputPath, dynamicOutputPath, model);
    }
}