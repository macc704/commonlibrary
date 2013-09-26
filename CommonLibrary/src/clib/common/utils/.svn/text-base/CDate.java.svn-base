package clib.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CDate {

	public static DateFormat FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
	public static DateFormat FORMATTERMS = new SimpleDateFormat(
			"yyyyMMddHHmmssMMMM");

	public static String current() {
		return format(new Date());
	}

	public static String format(Date d) {
		return FORMATTER.format(d);
	}

}
