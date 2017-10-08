package ru.ex4.apibt.log;

import ru.ex4.apibt.IExConst;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {

    public static void info(String message) {
        Date date = new Date();
        String str = new SimpleDateFormat(IExConst.DATE_FORMAT).format(date) + "  \t INFO   - [ \t\t " + message + " ]";

        System.out.println(str);
    }

    public static void error(String message) {
        Date date = new Date();
        String str = new SimpleDateFormat(IExConst.DATE_FORMAT).format(date) + "  \t ERROR  - [ \t\t " + message + " ]";

        System.err.println(str);
    }

    public static void debug(String className, String message) {
        if (IExConst.DEBUG) {
            Date date = new Date();
            String str = String.format("%1$s  \t %2$s  - [ %3$s: %4$s ]", new SimpleDateFormat(IExConst.DATE_FORMAT).format(date), "DEBUG", className, message);

            System.out.println(str);
        }
    }

    public static void sql(String message) {
        if (IExConst.DEBUG) {
            System.out.println("sql: " + message.replace("\n", " "));
        }
    }

}
