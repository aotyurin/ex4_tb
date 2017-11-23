package ru.ex4.apibt.log;

import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.util.DateUtil;

import java.util.Date;

public class Logs {

    public static void info(String message) {
        Date date = new Date();
        String str = DateUtil.format(date) + "  \t INFO   - [ \t\t " + message + " ]";

        System.out.println(str);
    }

    public static void error(String message) {
        Date date = new Date();
        String str = DateUtil.format(date) + "  \t ERROR  - [ \t\t " + message + " ]";

        System.err.println(str);
    }

    public static void debug(String className, String message) {
        if (IExConst.DEBUG) {
            Date date = new Date();
            String str = String.format("%1$s  \t %2$s  - [ %3$s: %4$s ]", DateUtil.format(date), "DEBUG", className, message);

            System.out.println(str);
        }
    }

    public static void sql(String message) {
        if (IExConst.DEBUG) {
            System.out.println("sql: " + message.replace("\n", " "));
        }
    }

}
