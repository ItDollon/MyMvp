package www.tencent.com.superframe.utils;

import android.text.format.DateUtils;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @description
 * @author Fang Yucun
 * @created 2013年11月20日
 */
public class TimeUtils {

	public static final long SECOND_IN_MILLIS = 1000;
	public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
	public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
	public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
	public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;

	public static final long MINUTE_IN_SECOND = 60;
	public static final long HOUR_IN_SECOND = MINUTE_IN_SECOND * 60;
	public static final long DAY_IN_SECOND = HOUR_IN_SECOND * 24;
	public static final long WEEK_IN_SECOND = DAY_IN_SECOND * 7;

	public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_FULL = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DATE_TIME = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_IGNORE_SECOND = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat(
			"HH:mm:ss", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_TIME_IGNORE_SECOND = new SimpleDateFormat(
			"HH:mm", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_STRING_FULL = new SimpleDateFormat(
			"yyyyMMddHHmmssS", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DATE_TIME_STRING = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DATE_STRING = new SimpleDateFormat(
			"yyyyMMdd", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_TIME_STRING = new SimpleDateFormat(
			"HHmmss", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH_STRING = new SimpleDateFormat(
			"yyyyMM", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_YEAR_STRING = new SimpleDateFormat(
			"yyyy", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_MONTH_STRING = new SimpleDateFormat(
			"MM", Locale.CHINA);
	public static final SimpleDateFormat DATE_FORMAT_DAY_STRING = new SimpleDateFormat(
			"dd", Locale.CHINA);

	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	public static String getCurrentTime(String dateFormat) {
		return getTime(getCurrentTimeInLong(), new SimpleDateFormat(dateFormat,
				Locale.CHINA));
	}

	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	public static String getCurrentDataTimeFull() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME_FULL);
	}
	public static String getCurrentDataTimeHHMM() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_TIME_IGNORE_SECOND);
	}

	public static String getCurrentDateTime() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME);
	}

	public static String getCurrentDate() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE);
	}

	public static String getCurrentDateInString() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_STRING);
	}

	public static String getCurrentTime() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_TIME);
	}

	public static String getCurrentDateTimeFullInString() {
		return getTime(getCurrentTimeInLong(),
				DATE_FORMAT_DATE_TIME_STRING_FULL);
	}

	public static String getCurrentDateTimeInString() {
		return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE_TIME_STRING);
	}

	public static String getDateTime(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_DATE_TIME);
	}

	public static String getDate(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_DATE);
	}

	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_TIME);
	}

	public static String getDateTimeInString(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_DATE_TIME);
	}

	public static String getDateInString(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_DATE);
	}

	public static String getTimeInString(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_TIME);
	}

	/**
	 * 判断当前日期是否是当天
	 * 
	 * @return
	 */
	public static boolean isToday(long timeInMillis) {
		return DateUtils.isToday(timeInMillis);
	}

	/**
	 * 判断当前日期是否是当月
	 * 
	 * @return
	 */
	public static boolean isThisMonth(long timeInMillis) {
		Time time = new Time();
		time.set(timeInMillis);
		int thenYear = time.year;
		int thenMonth = time.month;
		time.set(System.currentTimeMillis());

		return (thenYear == time.year) && (thenMonth == time.month);
	}

	/**
	 * 
	 * @param month
	 * @return
	 */
	public static String getMonthDate(int month) {
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), month - 1 < 1 ? 0 : month - 1, 1, 0, 0);
		return DATE_FORMAT_DATE_TIME_STRING.format(c.getTime());
	}

	/**
	 * 判断当前日期是否是当年
	 * 
	 * @return
	 */
	public static boolean isThisYear(long timeInMillis) {
		Time time = new Time();
		time.set(timeInMillis);

		int thenYear = time.year;

		time.set(System.currentTimeMillis());
		return thenYear == time.year;
	}

	/**
	 * 
	 * @param vDate1
	 * @param vDate2
	 * @return vDate1>=vDate2 为true 否则为false
	 */
	public static boolean compare(Calendar vDate1, Calendar vDate2) {
		return vDate1.after(vDate2);
	}
	public static boolean isSameDay(Calendar vDate1, Calendar vDate2) {
		if (vDate1.get(Calendar.YEAR) == vDate2.get(Calendar.YEAR)
				&& vDate1.get(Calendar.MONTH) == vDate2.get(Calendar.MONTH)
				&& vDate1.get(Calendar.DAY_OF_MONTH) == vDate2
						.get(Calendar.DAY_OF_MONTH))
			return true;
		return false;
	}
	public static int compare_date(String DATE1, String DATE2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;

	}

	public static int getDateDiff(String date1) {
		Calendar vDate = Calendar.getInstance();
		try {
			Date date = DATE_FORMAT_DATE.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
			vDate.setTime(date);
		} catch (Exception e) {
		}
		return getDateDiff(vDate);
	}

	public static int getDateDiff(Calendar vDate) {
		long betweenTime = 0;
		try {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			vDate.set(Calendar.HOUR_OF_DAY, 0);
			vDate.set(Calendar.MINUTE, 0);
			vDate.set(Calendar.SECOND, 0);
			betweenTime = vDate.getTimeInMillis() / 1000
					- today.getTimeInMillis() / 1000;
			betweenTime = betweenTime / (60 * 60 * 24);
		} catch (Exception e) {
		}
		return (int) betweenTime;
	}

	public static int[] getBirthDayDiff(String date1) {
		Calendar vDate = Calendar.getInstance();
		Date date = null;
		try {
			date = DATE_FORMAT_DATE.parse(date1);
			vDate.setTime(date);
		} catch (ParseException e) {
		}
		return getBirthDayDiff(vDate);
	}

	public static int[] getBirthDayDiff(Calendar vDate) {
		return getBirthDayDiff(vDate, 0);
	}

	public static int[] getBirthDayDiff(Calendar vDate, int addYear) {
		long betweenTime = 0;
		int year = 0;
		try {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);

			year = today.get(Calendar.YEAR) - vDate.get(Calendar.YEAR)
					+ addYear;
			vDate.set(Calendar.YEAR, today.get(Calendar.YEAR) + addYear);
			vDate.set(Calendar.HOUR_OF_DAY, 0);
			vDate.set(Calendar.MINUTE, 0);
			vDate.set(Calendar.SECOND, 0);

			betweenTime = vDate.getTimeInMillis() / 1000
					- today.getTimeInMillis() / 1000;
			betweenTime = betweenTime / (60 * 60 * 24);
		} catch (Exception e) {
		}
		return new int[] { (int) betweenTime, year };
	}

	public static Calendar getCalendar(String sDate) {
		Calendar mCalendar = Calendar.getInstance();
		try {
			Date date = DATE_FORMAT_DATE_TIME.parse(sDate);
			mCalendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return mCalendar;
	}

	public static String formatTime(Calendar calendar) {
		return DATE_FORMAT_DATE_TIME.format(calendar.getTime());
	}

	public static String formatDate(Calendar calendar) {
		return DATE_FORMAT_DATE.format(calendar.getTime());
	}

	public static String formatStandardTime(String sDate) {
		try {
			Date date = DATE_FORMAT_DATE_TIME.parse(sDate);
			return date.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}


	public static  String  getCurrentTimeName(){
     SimpleDateFormat sf = new SimpleDateFormat(
				"HH:mm", Locale.CHINA);
		try {
			long date =sf.parse(getCurrentDataTimeHHMM()).getTime();
			long parse5 = sf.parse("05:00").getTime();
			long parse7 = sf.parse("07:00").getTime();
			long parse9 = sf.parse("09:00").getTime();
			long parse12 = sf.parse("12:00").getTime();
			long parse14 = sf.parse("14:00").getTime();
			long parse18 = sf.parse("18:00").getTime();
			long parse19 = sf.parse("19:00").getTime();
			long parse24 = sf.parse("24:00").getTime();
			if (date>parse5&&date<=parse7){
				return "清晨";
			}
			else if (date>parse7&&date<=parse9){
				return "早上";
			}
			else if (date>parse9&&date<=parse12){
				return "上午";
			}
			else if (date>parse12&&date<=parse14){
				return "中午";
			}
			else if (date>parse14&&date<=parse18){
				return "下午";
			}
			else if (date>parse18&&date<=parse19){
				return "傍晚";
			}
			else if (date>parse19&&date<=parse24){
				return "晚上";
			}
			else if (date<=parse5){
				return "凌晨";
			}else{
				return "您";
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}


		return  "您";
	}



	private TimeUtils() {/* Do not new me */
	}


}
