package com.texuna.simpletest;

/**
 * Created by Sbt-tumaykin-kk on 25.08.2016.
 */
public class Checker {
    public static void checkSettings() {
        try {
            if (Parser.getPageWidth() <= 0 || Parser.getPageHeight() <= 0) throw new SettingException("ERROR:Page setting is incorrect");
            if (Parser.getActualPageWidth() > Parser.getPageWidth()) throw new SettingException("ERROR:Page width isnt enought");
            for (Integer size : Parser.getColumnSizes()) {
                if (size <= 0) throw new SettingException("ERROR:Column size <= 0");
            }
        }
        catch (SettingException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    static class SettingException extends Exception {
        public SettingException(String message) {
            super(message);
        }
    }
}
