package com.texuna.simpletest;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by kirill on 15.08.2016.
 */
public class main {

    public static void main(String[] args) throws IOException {

//        String fileName = "C:\\Users\\SBT-Tumaykin-KK\\Desktop\\jv\\projects\\Simple test task\\settings.xml";
//        String sourceFile = "C:\\Users\\SBT-Tumaykin-KK\\Desktop\\jv\\projects\\Simple test task\\source-data.tsv";
//        String targetFile = "C:\\Users\\SBT-Tumaykin-KK\\Desktop\\jv\\projects\\Simple test task\\report.txt";
        String settingsFilename = args[0];
        String sourceFile = args[1];
        String targetFile = args[2];
        Parser.parseXML(settingsFilename);
        Checker.checkSettings();

        FileInputStream fis = new FileInputStream(sourceFile);
        FileOutputStream fos = new FileOutputStream(targetFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-16")));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-16")));
        ReportGenerator gen = new ReportGenerator(reader, writer);

        gen.run();

        writer.flush();
        writer.close();
    }
}
