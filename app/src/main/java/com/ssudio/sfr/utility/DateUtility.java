package com.ssudio.sfr.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {
    public static String parseToApplicationDateFormat(String feedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            Date date = format.parse(feedDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

            String datetime = dateFormat.format(date);

            return datetime;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }
}
