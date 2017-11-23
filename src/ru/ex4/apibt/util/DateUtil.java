package ru.ex4.apibt.util;

import ru.ex4.apibt.IExConst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String format (Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(IExConst.DATE_FORMAT).format(date);
    }

    public static Date parse (String source) throws ParseException {
        if (source == null || source.equals("")) {
            return null;
        }
        return new SimpleDateFormat(IExConst.DATE_FORMAT).parse(source);
    }

}
