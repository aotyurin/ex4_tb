package ru.ex4.apibt.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {
    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static void info(String message) {

        Date date = new Date();
        String str = new SimpleDateFormat(DATE_FORMAT).format(date) + "  \t INFO   - [ \t\t " + message + " ]";

        System.out.println(str);
    }

    public static void error(String message) {
        Date date = new Date();
        String str = new SimpleDateFormat(DATE_FORMAT).format(date) + "  \t ERROR  - [ \t\t " + message + " ]";

        System.err.println(str);
    }


    public static void sql(String message) {
        System.out.println("sql: " + message.replace("\n", " "));
    }
}
