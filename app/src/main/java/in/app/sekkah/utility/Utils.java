package in.app.sekkah.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public static String calculateDiff(String t1, String t2) {
        Date date1 = null;
        Date date2 = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            date1 = simpleDateFormat.parse(t1);
            date2 = simpleDateFormat.parse(t2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

        return (hours < 0 ? "00" : hours) + ":" + (min < 0 ? "00" : min) + ":00";

    }
}
