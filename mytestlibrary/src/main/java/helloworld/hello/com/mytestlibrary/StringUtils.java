package helloworld.hello.com.mytestlibrary;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat();
    private static final String HHMM_FORMAT_STRING = "HH:mm";
    private static final String MMDDHHMM_FORMAT_STRING = "MM-dd HH:mm";
    private static final String YYYYMMDDHHMM_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern IMG_URL = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)");

    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
    public static String longToString(long currentTime, String formatType){
        Date date = null; // long类型转成Date类型
        try {
            date = longToDate(currentTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }


    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param time
     * @return
     */
    public static String formatFriendly(long time) {
        Date date = getCurrentTime(time);
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        if (diff >1000){
            r = (diff / 1000);
            return r + "秒前";
        }
        return "刚刚";
    }

    /**
     * 获取是未来多久---几天后
     * @param time
     * @return
     */
    public static String formatFuture(String time) {
        Date date = parseDate(time+" 23:59:59");
        String s = getDateString(date);
        if (date == null) {
            return null;
        }
        long diff = date.getTime() - new Date().getTime();
        long r = 0;
//        if (diff > year) {
//            r = (diff / year);
//            return r + "年后";
//        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月后";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天后";
        }
//        if (diff > hour) {
//            r = (diff / hour);
//            return r + "小时后";
//        }
//        if (diff > minute) {
//            r = (diff / minute);
//            return r + "分钟后";
//        }
//        if (diff >1000){
//            r = (diff / 1000);
//            return r + "秒后";
//        }
        return "今天";
    }
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> timeFormater1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };
    private static final int HOUR = 60 * 60 * 1000;
    private static final int MIN = 60 * 1000;
    private static final int SEC = 1000;
    public static Date getCurrentTime(long time) {
        mSimpleDateFormat.applyPattern(YYYYMMDDHHMM_FORMAT_STRING);
        return toDate(mSimpleDateFormat.format(time));
    }
    /**
     * 把duration数值转换为字符串格式，ec.01:02:03， 02:03
     */
    public static String formatDuration(int duration) {
        int hour = duration / HOUR;

        int min = duration % HOUR / MIN;

        int sec = duration % MIN / SEC;

        if (hour == 0) {
            // 没有小时数，02:03
            return String.format("%02d:%02d", min, sec);
        } else {
            // 有小时数，01:02:03
            return String.format("%02d:%02d:%02d", hour, min, sec);
        }
    }

    /**
     * 计算两个时间之间相差多少时间
     * @param old
     * @return
     */
    public static String timeBetWeen(String old){
        String time ="";
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin= null;
        try {
            begin = dfs.parse(old);
            Date date = new Date();
            Date end = dfs.parse(dfs.format(date));
            long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
            long day1=between/(24*3600);
            long hour1=between%(24*3600)/3600;
            long minute1=between%3600/60;
            if (day1==0){
                if (hour1==0){
                    if (minute1==0){
                        time = "刚刚";
                    }else {
                        time = minute1+"分钟前";
                    }
                }else {
                    time = hour1+"小时前";
                }
            }else {
                time = day1+"天前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /** 格式化当前系统时间为 01:01:01 */
    public static String formatSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 将或得到的图片地址转换为集合
     */
    public static ArrayList<String> getList(String url){
        if (StringUtils.isEmpty(url)){
            return new ArrayList<>();
        }
        int loc;
        url = url+";";
        ArrayList<String> imgUrl = new ArrayList<>();
        loc = url.indexOf(";");
        while(loc!=-1){
            String fir = url.substring(0,loc);
            imgUrl.add(fir);
            url = url.substring(loc+1);
            loc = url.indexOf(";");
        }
        return imgUrl;
    }

    /**
     *  	传递一个 集合** ,返回  ";" 好的 字符串
     * @return
     */
    public  static String splitArrayList(List<String> selectList){
        String[] usersUuids=new String[selectList.size()];

        for (int i = 0; i < selectList.size(); i++) {
            usersUuids[i]=selectList.get(i);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < usersUuids.length; i++) { // 把数组 给 用; 给分割下 ;
            if (i == usersUuids.length - 1) {
                sb.append(usersUuids[i]);
            } else {
                sb.append(usersUuids[i] + ";");
            }
        }
        return sb.toString();
    }
    /**
     * 判断一个url是否为图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }

    /** sdcard文件 **/
    public static final String RES_SDCARD = "sdcard://";
    /** 网络文件 **/
    public static final String RES_HTTP = "http://";
    /** 网络文件 **/
    public static final String RES_HTTPS = "https://";

    /**
     * 判断字符串是否为空
     * @param str
     * @return true：为空; false：不为空
     */
    public static boolean isEmpty(String str){
        boolean result=false;
        if(str==null || str.trim().length()==0){
            result=true;
        }
        return result;
    }
    public static boolean isNotEmpty(String str){
        boolean result=true;
        if(str==null || str.trim().length()==0){
            result=false;
        }
        return result;
    }

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串;  为空返回true
     *
     * @param input
     * @return true：为空 ; false：不为空     ' ' 也返回true;
     */
    public static boolean isStrongEmpty(String input) {
        if (input == null || "".equals(input)||input.trim().length()==0)
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /***
     *   不等于0.0
     * @param input
     * @return
     */
    public static boolean isTwoStrongEmpty(String input) {
        if (input == null || "".equals(input)||input.trim().length()==0||"0.0".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }







    /**
     * 字符串最大长度验证
     * @param str
     * @param length  不能超过的最大值 比如  20;
     * @return true：通过；false：不通过
     */
    public static boolean maxLength(String str, int length){
        if(isEmpty(str)){
            return false;
        }else	if(str.trim().length()<=length){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 字符串最小长度验证
     * @param str
     * @param length
     * @return true：通过；false：不通过
     */
    public static boolean minLength(String str, int length){
        if(isEmpty(str)){
            return false;
        }else if(str.trim().length()>=length){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isPassword(String pass){
        if(minLength(pass, 6)&&maxLength(pass, 20)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断字符串是否是合法的手机号
     * @param str
     * @return true：合法; false：不合法
     */
    public static boolean isPhone(String str){
        if(isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    /**
     * 判断2个非空字符串是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean twoStringisEquals(String str1, String str2){
        if(!isEmpty(str1)||!isEmpty(str2)){
            if(isEmpty(str1)==isEmpty(str2)){
                return true;
            }
            return false;
        }
        return str1.trim()==str2.trim();
    }
    /**
     * 是否为网络文件
     *
     * @param url
     * @return
     */
    public static boolean isNetworkFile(String url) {
        if(StringUtils.isEmpty(url)){
            return false;
        }
        if (url.startsWith(RES_HTTP) || url.startsWith(RES_HTTPS)) {
            return true;
        }
        return false;
    }
    /**
     * 把大图URL地址转换为200的缩略图地址（和服务器约定好的）
     * @param destUrl
     * @return
     */
    public static String imageUrlConvert200ImageUrl(String destUrl){
        int fileFormat = destUrl.lastIndexOf(".");
        String fileUrl =destUrl + "_200."+ destUrl.substring(fileFormat + 1, destUrl.length());
        return fileUrl;
    }


    /**
     * 判断email格式是否正确
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 以空格截取，返回list.get(0),省，list.get(1)市
     */
    public static List<String> getCity(String cityId) {
        List<String> city = new ArrayList<String>();
        int trim = cityId.indexOf(" ");
        String provice = cityId.substring(0, trim);
        String provice_ = cityId.substring(trim+1);
        city.add(provice);
        city.add(provice_);
        return city;
    }
    public static List<String> getSubString(String url,String code) {
        int loc;
        List<String> imgUrl = new ArrayList<>();
        loc = url.indexOf(code);
        while(loc!=-1){
            String fir = url.substring(0,loc);
            imgUrl.add(fir);
            url = url.substring(loc+1);
            loc = url.indexOf(code);
        }
        return imgUrl;
    }
//	 ----------------------------------
    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }
    /**
     * 将日期字符串转成日期
     *
     * @param strDate
     *            字符串日期
     * @return java.util.date日期类型
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
        }
        return returnDate;
    }
    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateString(Date date) {
        return dateFormater.get().format(date);
    }

    /**
     *  显示的样子不一样
     * @param sdate
     * @return
     */
    public static String friendly_time2(String sdate) {
        String res = "";
        if (isEmpty(sdate))
            return "";

        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        String currentData = StringUtils.getDataTime("MM-dd");
        int currentDay = toInt(currentData.substring(3));
        int currentMoth = toInt(currentData.substring(0, 2));

        int sMoth = toInt(sdate.substring(5, 7));
        int sDay = toInt(sdate.substring(8, 10));
        int sYear = toInt(sdate.substring(0, 4));
        Date dt = new Date(sYear, sMoth - 1, sDay - 1);

        if (sDay == currentDay && sMoth == currentMoth) {
            res = "今天 / " + weekDays[getWeekOfDate(new Date())];
        } else if (sDay == currentDay + 1 && sMoth == currentMoth) {
            res = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
        } else {
            if (sMoth < 10) {
                res = "0";
            }
            res += sMoth + "/";
            if (sDay < 10) {
                res += "0";
            }
            res += sDay + " / " + weekDays[getWeekOfDate(dt)];
        }

        return res;
    }


    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null || "".equals(obj))
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
    /**
     * 智能格式化 , 不同的显示样子
     */
    public static String friendly_time3(String sdate) {
        String res = "";
        if (isEmpty(sdate))
            return "";

        Date date = StringUtils.toDate(sdate);
        if (date == null)
            return sdate;

        SimpleDateFormat format = dateFormater2.get();

        if (isToday(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "上午 hh:mm" : "下午 hh:mm");
            res = format.format(date);
        } else if (isYesterday(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "昨天 上午 hh:mm" : "昨天 下午 hh:mm");
            res = format.format(date);
        } else if (isCurrentYear(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "MM-dd 上午 hh:mm" : "MM-dd 下午 hh:mm");
            res = format.format(date);
        } else {
            format.applyPattern(isMorning(date.getTime()) ? "yyyy-MM-dd 上午 hh:mm" : "yyyy-MM-dd 下午 hh:mm");
            res = format.format(date);
        }
        return res;
    }

    /**
     * @return 判断一个时间是不是上午
     */
    public static boolean isMorning(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int hour = time.hour;
        return (hour >= 0) && (hour < 12);
    }

    /**
     * @return 判断一个时间是不是今天
     */
    public static boolean isToday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    /**
     * @return 判断一个时间是不是昨天
     */
    public static boolean isYesterday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (time.monthDay - thenMonthDay == 1);
    }

    /**
     * @return 判断一个时间是不是今年
     */
    public static boolean isCurrentYear(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }
    /**
     * 返回String类型的今天的日期
     *
     * @return
     */
    public static String getStringToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", ".");
        return curDate;
    }
    public static String getStringToday_() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        return curDate;
    }

    /**
     * 返回String类型的_的日期
     * @param date 传递一个 date
     * @return
     */
    public static String getStringFormatToday(Date date) {
        String curDate = dateFormater2.get().format(date);
        curDate = curDate.replace("-", ".");
        return curDate;
    }



    public static String getCurTimeStr() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater.get().format(cal.getTime());
        return curDate;
    }

    public static String getCurTime() {
        Calendar cal = Calendar.getInstance();
        String curDate = timeFormater1.get().format(cal.getTime());
        return curDate;
    }

    /***
     * 计算两个时间差，返回的是的秒s
     *
     * @author 火蚁 2015-2-9 下午4:50:06
     *
     * @return long
     * @param dete1
     * @param date2
     * @return
     */
    public static long calDateDifferent(String dete1, String date2) {

        long diff = 0;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = dateFormater.get().parse(dete1);
            d2 = dateFormater.get().parse(date2);

            // 毫秒ms
            diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff / 1000;
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @return
     */
    public static int getWeekOfYear() {
        return getWeekOfYear(new Date());
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        int week = c.get(Calendar.WEEK_OF_YEAR) - 1;
        week = week == 0 ? 52 : week;
        return week > 0 ? week : 1;
    }

    public static int[] getCurrentDate() {
        int[] dateBundle = new int[3];
        String[] temp = getDataTime("yyyy-MM-dd").split("-");

        for (int i = 0; i < 3; i++) {
            try {
                dateBundle[i] = Integer.parseInt(temp[i]);
            } catch (Exception e) {
                dateBundle[i] = 0;
            }
        }
        return dateBundle;
    }



    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }


    public static String getString(String s) {
        return s == null ? "" : s;
    }

    public static int compare_date(String date1, String date2)
    {
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            if (d1.getTime() > d2.getTime())
            {
                return 1;
            }
            else if (d1.getTime() < d2.getTime())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compareDate(String date1, String date2)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            if (d1.getTime() > d2.getTime())
            {
                return 1;
            }
            else if (d1.getTime() < d2.getTime())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    public static String getNextDay(String date, int type) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = df.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, type);
            date1 = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateStr = df.format(date1);
        return dateStr;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static List<String> getLable(String lable){
        lable = "#"+lable+"#";
        List<String> lables = new ArrayList<>();
        int lenght = lable.length();
        while (lenght>1){
            int index = lable.indexOf("#");
            int index_2 = lable.indexOf("#",index+1);
            String lable_temp = lable.substring(index+1,index_2);
            lable = lable.substring(index_2);
            if (!isEmpty(lable_temp)){
                lables.add(lable_temp);
            }
            lenght = lable.length();
        }
        return lables;
    }

    public static String getDistance(String distance){
        String dis = distance;
        float distan = Float.parseFloat(dis);
        if (distan<1000){
            dis = dis+"m";
        }else {
            distan = distan/1000;
            float  b   =  (float)(Math.round(distan*100))/100;
            dis = b+"km";
        }
        return dis;
    }

    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }


    static class CommentURLSpan extends ClickableSpan {
        public Context mContext;
        private String mUrl;
        private String userId;
        CommentURLSpan(String url,String userId,Context mContext) {
            mUrl =url;
            this.mContext = mContext;
            this.userId = userId;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            //点击标签跳转界面
//            ToastHelper.shortShow(mContext,"SSSS");
//            Intent intent = new Intent(mContext, OtherWishActivity.class);
//            intent.putExtra("otherUserId",userId);
//            mContext.startActivity(intent);
        }
    }

    /**
     * 时间转换
     * strDate 2016-08-18 10:25:20
     * 如果是今天展示12:00
     * 如果不是今天是今年展示08-18 12:00
     * 不是今年2016-08-18 12:00
     */
    public static String setShowTime(String str){
        String strDate = getDateString(getCurrentTime(Long.parseLong(str)));
        if (StringUtils.isEmpty(strDate)){
            return "";
        }
        String result = "";
        Calendar now = Calendar.getInstance();
        Date date = parseDate(strDate);
        if (date==null){
            return "";
        }
        Calendar target = Calendar.getInstance();
        target.setTime(date);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate= null;
        try {
            nowDate = sdf.parse(sdf.format(now.getTime()));
            now.setTime(nowDate);
            target.setTime(sdf.parse(sdf.format(target.getTime())));
            if(now.compareTo(target)==0){
                result=strDate.substring(11,16);
            }else{
                SimpleDateFormat sd=new SimpleDateFormat("yyyy");
                nowDate = sd.parse(sd.format(now.getTime()));
                now.setTime(nowDate);
                target.setTime(sd.parse(sd.format(target.getTime())));
                if(now.compareTo(target)!=0){
                    result=strDate.substring(0,16);
                }else{
                    result=strDate.substring(5,16);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *判断当前应用程序处于前台还是后台
     *
     * @param context

     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
