package com.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.YearMonth;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <li>日期工具类, 继承org.apache.commons.lang.time.DateUtils类</li>
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	
	/**
	 * <li>参数数组</li><br/>
	 * 0:yyyyMMdd<br/>1:yyyy-MM-dd<br/>2:yyyy-MM-dd HH:mm<br/>3:yyyy-MM-dd HH:mm:ss<br/>4:yyyy/MM/dd<br/>
	 * 5:yyyy/MM/dd HH:mm<br/>6:yyyy/MM/dd HH:mm:ss<br/>7:HH:mm:ss[时间]<br/>8:yyyy[年份]<br/>9:MM[月份]<br/>
	 * 10.dd[天]<br/>11.E[星期几]<br/>
	 */
	public static String[] parsePatterns = {"yyyyMMdd","yyyy-MM-dd","yyyy-MM-dd HH:mm","yyyy-MM-dd HH:mm:ss",
		"yyyy/MM/dd","yyyy/MM/dd HH:mm","yyyy/MM/dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS","HH:mm:ss","yyyy","MM","dd","E"};
	/**
	 * <li>得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"</li>
	 *  @param  pattern 可参考参数组parsePatterns
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	//默认当前时间：yyyy-MM-dd HH:mm:ss
	public static String current() {
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * <li>得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"</li>
	 * @param  date    日期类  
	 * @param  pattern 可参考参数组parsePatterns
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date,parsePatterns[1]);
		}
		return formatDate;
	}	
	/**
	 * <li>日期型字符串转化为日期 格式</li>
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * <li>获取过去的天数</li>
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	/**
	 * <li>获取输入日期开始时间</li>
	 * @param date
	 * @return
	 */
	public static Date getDateStart(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd")+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * <li>获取输入日期结束时间</li>
	 * @param date
	 * @return
	 */
	public static Date getDateEnd(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd") +" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * <li>按输入日期加分钟</li>
	 * @param
	 * @param recordPeriodMinute 分钟数
	 * @return
	 */
	public static Date addMinutes(Date recordTime, int recordPeriodMinute) {
		Calendar c = Calendar.getInstance();
		c.setTime(recordTime);
		c.add(Calendar.MINUTE, recordPeriodMinute);
		return c.getTime();
	}
	/**
	 * <li>按输入日期加毫秒</li>
	 * @param
	 * @param recordPeriodSecond 毫秒数
	 * @return
	 */
	public static Date addSeconds(Date recordTime, int recordPeriodSecond) {
		Calendar c = Calendar.getInstance();
		c.setTime(recordTime);
		c.add(Calendar.SECOND, recordPeriodSecond);
		return c.getTime();
	}
	/**
	 * <li>校验日期是否合法</li>
	 * @param date 日期
	 * @return
	 */
	public static boolean isValidDate(String date) {
		DateFormat fmt = new SimpleDateFormat(parsePatterns[1]);
		try {
			fmt.parse(date);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}	
	  /**
     * <li>时间相减得到相差年数</li>
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat(parsePatterns[1]);
		try {
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	  /**
     * <li>功能描述：时间相减得到天数</li>
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static long getDaySub(String startTime, String endTime){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat(parsePatterns[1]);
        Date beginDate = null;
        Date endDate = null;
            try {
				beginDate = format.parse(startTime);
				endDate= format.parse(endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);   
        return day;
    }

	/**
	 *	获取起始时间，与结束时间相差的月数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getMonthSub(long start,long end) {
		Calendar startC = Calendar.getInstance();
		Calendar endC = Calendar.getInstance();
		startC.setTimeInMillis(start);
		endC.setTimeInMillis(end);

		int year = (endC.get(Calendar.YEAR) - startC.get(Calendar.YEAR)) * 12;
		int month = endC.get(Calendar.MONTH) - startC.get(Calendar.MONTH);
		return Math.abs(year + month);
	}
	/**
	 * 获得当前第几季度
	 */
	public static int getSeasonNum(long time){
		int result = 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		try{
			if(currentMonth >= 1 && currentMonth <= 3){
				result = 1;
			}else if(currentMonth >= 4 && currentMonth <= 6){
				result = 2;
			}else if(currentMonth >= 7 && currentMonth <= 9){
				result = 3;
			}else if(currentMonth >= 10 && currentMonth <= 12){
				result = 4;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 *	获取起始时间，与结束时间相差的月数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getSeasonSub(long start,long end) {
		Calendar startC = Calendar.getInstance();
		Calendar endC = Calendar.getInstance();
		startC.setTimeInMillis(start);
		endC.setTimeInMillis(end);
		int year = (endC.get(Calendar.YEAR) - startC.get(Calendar.YEAR)) * 4;
		int season =  getSeasonNum(end) - getSeasonNum(start);
		return Math.abs(year + season);
	}


	/**
	 * 获得指定时间，所在季度的开始时间
	 * @param time
	 * @return
	 */
	public static long getCurrentQuarterStartTime(long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		try{
			if(currentMonth >= 1 && currentMonth <= 3){
				c.set(Calendar.MONTH, 0);
			}else if(currentMonth >= 4 && currentMonth <= 6){
				c.set(Calendar.MONTH, 3);
			}else if(currentMonth >= 7 && currentMonth <= 9){
				c.set(Calendar.MONTH, 4);
			}else if(currentMonth >= 10 && currentMonth <= 12){
				c.set(Calendar.MONTH, 9);
			}
			c.set(Calendar.DAY_OF_MONTH, 1);
		}catch (Exception e){
			e.printStackTrace();
		}
		return c.getTimeInMillis();
	}

	/**
	 * 获得指定时间，所在季度的下一个季度开始时间
	 * @param time
	 * @return
	 */
	public static long getNextQuarterStartTime(long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		try{
			if(currentMonth >= 1 && currentMonth <= 3){
				c.set(Calendar.MONTH, 3);
			}else if(currentMonth >= 4 && currentMonth <= 6){
				c.set(Calendar.MONTH, 6);
			}else if(currentMonth >= 7 && currentMonth <= 9){
				c.set(Calendar.MONTH, 9);
			}else if(currentMonth >= 10 && currentMonth <= 12){
				int currentYear =c.get(Calendar.YEAR);
				c.set(Calendar.YEAR,currentYear+1);
				c.set(Calendar.MONTH, 0);
			}
			c.set(Calendar.DAY_OF_MONTH, 1);
		}catch (Exception e){
			e.printStackTrace();
		}
		return c.getTimeInMillis();
	}

	/**
	 * 判断两个时间是否在同一季度
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean sameQuaeter(long time1,long time2){
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTimeInMillis(time1);
		calendar2.setTimeInMillis(time2);
		int year1 = calendar1.get(Calendar.YEAR);
		int year2 = calendar2.get(Calendar.YEAR);
		if(year1 != year2){
			return false;
		}
		int num1 = getSeasonNum(time1);
		int num2 = getSeasonNum(time2);
		if(num1 == num2){
			return true;
		}
		return false;
	}

	public static int getTimeLimitForProject(String startTime, String endTime){
		int day=0;
		SimpleDateFormat format = new SimpleDateFormat(parsePatterns[1]);
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = format.parse(startTime);
			endDate= format.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day=(int)((endDate.getTime()-beginDate.getTime())/(24*60*60*1000)) +1;
		return day;
	}
    /**
     * <li>得到n天之后的日期</li>
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance();
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdfd = new SimpleDateFormat((parsePatterns[3]));
        String dateStr = sdfd.format(date);
        return dateStr;
    }

	/**
	 * 获取指定时间的，几天前或者几天后,几月前，几月后，几年前，几年后
	 * @param initData
	 * @param days
	 * @return
	 */
	public static Long getDifferenceDayTime(Long initData,int days,String type) {
    	Date date = new Date(initData);
		Calendar canlendar = Calendar.getInstance();
		canlendar.setTime(date);
		switch (type){
			case "day":
				int initDay =canlendar.get(Calendar.DATE);
				canlendar.set(Calendar.DATE,initDay+days);
				break;
			case "month":
				int initMoth =canlendar.get(Calendar.MONTH);
				canlendar.set(Calendar.MONTH,initMoth+days);
				break;
			case "year":
				int initYear =canlendar.get(Calendar.YEAR);
				canlendar.set(Calendar.YEAR,initYear+days);
				break;
			default:
				initDay =canlendar.get(Calendar.DATE);
				canlendar.set(Calendar.DATE,initDay+days);
				break;
		}

		return canlendar.getTimeInMillis();
	}


    /**
     * <li>得到n天之后是周几</li>
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance();
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(parsePatterns[11]);
        String dateStr = sdf.format(date);
        return dateStr;
    }

	/**
	 * 获得指定时间所在月的天数
	 * @param time
	 * @return
	 */
	public static int getDayOfMonth(long time){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.setTimeInMillis(time);
		int day=aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

	/**
	 * 获得指定时间所在季度的天数
	 * @param time
	 * @return
	 */
	public static int getDayOfSeason(long time){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.setTimeInMillis(time);
		int day=aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

    /**
	 * 把时间根据时、分、秒转换为时间段
	 * @param  str  可选其1值{ "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 * @author String ？日前或？小时前或？分钟前？秒前
	 */
	public static String getTimes(Object str){
		String resultTimes = "";
		Date now;
		Date date=parseDate(str);
		now = new Date();
		long times = now.getTime() - date.getTime();
		long day = times / (24 * 60 * 60 * 1000);
		long hour = (times / (60 * 60 * 1000) - day * 24);
		long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		StringBuffer sb = new StringBuffer();
		if(day>0){
			sb.append(day + "日前");
		}else if (hour > 0) {
			sb.append(hour + "小时前");
		} else if (min > 0) {
			sb.append(min + "分钟前");
		} else if (sec >0){
			sb.append(sec + "秒前");
		}else if(times>=0){
			sb.append(times + "毫秒前");
		}else{
			sb.append("超前毫秒数:"+times);
		}
		resultTimes = sb.toString();
	    return resultTimes;
	}
		
    /** 
     * 将微信消息中的CreateTime转换成标准格式的时间Date
     * @param createTime 消息创建时间 
     * @return 
     */  
    public static Date formatTime(long createTime) {
    	long msgCreateTime =createTime*1000L;  
        return new Date(msgCreateTime);
    }  
	
	public static void main(String[] args) throws ParseException {

	}
}
