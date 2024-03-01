package com.rts.maker.generator.file;


import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicFileGenerator {

    /**
     * 做发电机
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     * @param model      模型
     * @throws IOException       ioexception
     * @throws TemplateException 模板异常
     */
    public static void doGenerate(String inputPath, String outputPath,Object model)  throws IOException, TemplateException {

        //指定版本 freeMarker
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        //指定模板文件位置
        File TemplateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(TemplateDir);

        //创建模板对象,加载指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        //如果文件不存在则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        //输出
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);

        //关闭流
        out.close();
    }


}
