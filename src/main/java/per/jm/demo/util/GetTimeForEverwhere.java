package per.jm.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeForEverwhere {

	public static String getTime() {

		Date d = new Date();

		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return time.format(d);
	}

	public static Date getDate() {

		Date d = new Date();		
		return d;
	}
}
