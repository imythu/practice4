package top.imyth.practice4.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndStringConverter {

    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Date getDateFromString(String dateString) throws ParseException {
        return dateFormater.parse(dateString);
    }

    public String getStringFromDate(Date date) {
        return dateFormater.format(date);
    }
}
