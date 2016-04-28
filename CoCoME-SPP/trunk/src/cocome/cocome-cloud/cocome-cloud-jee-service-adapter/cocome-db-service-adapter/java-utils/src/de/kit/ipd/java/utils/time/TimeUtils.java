package de.kit.ipd.java.utils.time;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class TimeUtils {
	
	private TimeUtils(){}
	
	/**
	 * The expected format is dd-mm-yyyy
	 * 
	 * @param date
	 * @return null if the format was not okay!
	 * @throws Exception
	 *             when something went wrong. Wrong can be the format dd.mm.yyyy
	 */
	public static Date convertToDateObject(String strdate){
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
		Date date = null;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(strdate);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTime();
	}
	
	/**
	 * Convert the given date to a well formatted string like dd.mm.yyyy
	 * @param date
	 * @return
	 */
	public static String convertToStringDate(Date date){
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date);
	}
	
	public static String getTime(){
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
		Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return formatter.format(calendar.getTime());
	}

}
