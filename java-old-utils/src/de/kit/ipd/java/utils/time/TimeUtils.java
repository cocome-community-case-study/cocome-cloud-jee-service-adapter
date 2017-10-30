package de.kit.ipd.java.utils.time;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class TimeUtils {

    private TimeUtils() {
    }

    /**
     * The expected format is dd-mm-yyyy
     *
     * @param strdate the string representation of the given date
     * @return null if the format was not okay!
     */
    public static Date convertToDateObject(String strdate) {
        final Calendar calendar = GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
        try {
            final Date date = new SimpleDateFormat("dd-MM-yyyy").parse(strdate);
            calendar.setTime(date);
        } catch (ParseException e) {
            throw new IllegalStateException("Cannot convert string to date: " + strdate, e);
        }
        return calendar.getTime();
    }

    /**
     * Convert the given date to a well formatted string like dd.mm.yyyy
     *
     * @param date the date object to convert
     * @return a string representation of the given date
     */
    public static String convertToStringDate(Date date) {
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    /**
     * Convert the given date to a well formatted string like dd.mm.yyyy.
     * It returns "00-00-0000" if the given
     *
     * @param date the date object to convert
     * @return a string representation of the given date
     */
    public static String convertNullableToStringDate(Date date) {
        return date == null ? "00-00-0000"
                : TimeUtils.convertToStringDate(date);
    }

    public static String getTime() {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
        Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(calendar.getTime());
    }

}
