package com.rts.maker.generator.file;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 生成器
 */
public class FileGenerator {

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
        // 父级目录，项目根目录
        File parentFile = new File(projectPath).getParentFile();
        // 输入路径
        String inputPath = new File(parentFile, "rts-generator-demo-projects/acm-template").getAbsolutePath();
        // 生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, projectPath);//直接生成在根目录
        // 生成动态文件，会覆盖部分已生成的静态文件
        String dynamicInputPath = projectPath + File.separator + "src/main/resources/templates/DataModel.java.ftl.ftl";
        String dynamicOutputPath = projectPath + File.separator + "acm-template/src/com/rts/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(dynamicInputPath, dynamicOutputPath, model);
    }
}