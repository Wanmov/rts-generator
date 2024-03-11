package com.rts.maker.generator.file;


import cn.hutool.core.io.FileUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicFileGenerator {

    /**
     * 根据相对路径生成文件
     *
     * @param outputPath        输出路径
     * @param model             模型
     * @param relativeInputPath 相对输入路径
     * @throws IOException       ioexception
     * @throws TemplateException 模板异常
     */
    public static void doGenerate(String relativeInputPath, String outputPath, Object model) throws IOException, TemplateException {

        // new 新的Configuration 对象 参数为freemarker的版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 获取模板文件的基本路径和模板文件名
        int lastSplitIndex = relativeInputPath.lastIndexOf("/");
        String basePackagePath = relativeInputPath.substring(0, lastSplitIndex);
        String templateName = relativeInputPath.substring(lastSplitIndex + 1);

        // 指定模板文件位置
        ClassTemplateLoader classTemplateLoader = new ClassTemplateLoader(DynamicFileGenerator.class, basePackagePath);
        configuration.setTemplateLoader(classTemplateLoader);

        // 创建模板对象,加载指定模板
        Template template = configuration.getTemplate(templateName);

        // 如果文件不存在则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        // 输出
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);

        // 关闭流
        out.close();
    }

    /**
     * 生成文件
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     * @param model      模型
     * @throws IOException       ioexception
     * @throws TemplateException 模板异常
     */
    public static void doGenerateByPath(String inputPath, String outputPath, Object model) throws IOException, TemplateException {

        // 指定版本 freeMarker
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 指定模板文件位置
        File TemplateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(TemplateDir);

        // 创建模板对象,加载指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        // 如果文件不存在则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        // 输出
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);

        // 关闭流
        out.close();
    }


}
