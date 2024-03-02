package com.rts.maker;

import com.rts.maker.generator.main.MainGenerator;


public class Main {
    public static void main(String[] args) throws Exception {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}
