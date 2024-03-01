package com.rts.maker.generator;

import java.io.*;
import java.util.Map;

public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 清理之前的构建并打包
        String MavenCommand = "mvn clean package -DskipTests=true";

        // 这里一定要拆分！
        ProcessBuilder processBuilder = new ProcessBuilder(MavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        System.out.println(environment);
        Process process = processBuilder.start();

        // 读取命令的输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行结束，退出码：" + exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("/Users/iooi/Project/rts-generator/rts-generator-maker/generated/acm-template-pro-generator");
    }
}