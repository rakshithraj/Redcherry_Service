package com.app.redcherry.Ulility;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public final class TimeStamp {


    public static String getCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis();


        return tsLong.toString();
    }


    public static long getSecondDifference(String timeStamp) {
        Long tsLong = System.currentTimeMillis();
        Long diffInMilis = Long.valueOf(timeStamp);
        diffInMilis = tsLong - diffInMilis;

        return diffInMilis / 1000;
    }

    public static long getMinDifference(String timeStamp) {
        Long tsLong = System.currentTimeMillis();
        Long diffInMilis = Long.valueOf(timeStamp);
        diffInMilis = tsLong - diffInMilis;
        return diffInMilis / (60 * 1000);
    }

    public static long getHourDifference(String timeStamp) {
        Long tsLong = System.currentTimeMillis();
        Long diffInMilis = Long.valueOf(timeStamp);
        diffInMilis = tsLong - diffInMilis;
        return diffInMilis / (60 * 60 * 1000);
    }

    public static long getDaysDifference(String timeStamp) {
        Long tsLong = System.currentTimeMillis();
        Long diffInMilis = Long.valueOf(timeStamp);
        diffInMilis = tsLong - diffInMilis;
        return diffInMilis / (24 * 60 * 60 * 1000);
    }

    public static int getDate(String timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timeStamp));
        return cal.get(Calendar.DATE);
    }

    public static int getMonth(String timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timeStamp));
        return cal.get(Calendar.MONTH);
    }

    public static int getYear(String timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timeStamp));
        return cal.get(Calendar.YEAR);
    }

    public static int getHour(String timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timeStamp));
        return cal.get(Calendar.HOUR);
    }

    public static int getMinute(String timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timeStamp));
        return cal.get(Calendar.MINUTE);
    }

    public static int getSecond(String timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timeStamp));
        return cal.get(Calendar.SECOND);
    }

    public static String getDateTime(String timeStamp) {
        long dv = Long.valueOf(timeStamp);
        Date df = new java.util.Date(dv);
        return new SimpleDateFormat("hh:mm::ssa").format(df);
        //return new SimpleDateFormat("MM/dd/yyyy hh:mma").format(df);
    }


    public static String getTimeStamp(String str_date) {

        SimpleDateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            date = formatter.parse(str_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return String.valueOf(date.getTime());

    }


    public static String convertTimeStampToDateTime(String timeStamp) {
        long dv = Long.valueOf(timeStamp);
        Date df = new java.util.Date(dv);

        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(df);


    }

    public static String getAge(String dateofbirthTimeStamp) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dobDate;
            Calendar dobcal = Calendar.getInstance();
            try {
                dobDate = formatter.parse(dateofbirthTimeStamp);

                dobcal.setTime(dobDate);

            } catch (ParseException e) {
            }


            return getAge(dobcal.get(Calendar.YEAR), dobcal.get(Calendar.MONTH), dobcal.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {

        }
        return "";
    }


    private static String getAge(int _year, int _month, int _day) {

        Log.d("newtag", "dob= " + _year + " " + _month + " " + _day);
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0) {
            a = 0;
        }
        return a + "";
    }


    public static ArrayList<String> getTimeSolts(String time,boolean isToady) {

        ArrayList<String> timeSlot = new ArrayList<>();
        try {
            String startTimeAmPm = time.split("-")[0].trim().trim();
            String endTimeAmPm = time.split("-")[1].trim();

            long longStartTimestamp = getTimeStamp_new(startTimeAmPm);
            long longEndTimestamp = getTimeStamp_new(endTimeAmPm);

            if(isToady) {
                Calendar cal = Calendar.getInstance();
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("hh:mma");
                String localTime = date.format(currentLocalTime);
                localTime=localTime.replace(".","");
                long currTimestamp = getTimeStamp_new(localTime);

                while (longStartTimestamp < currTimestamp) {
                    longStartTimestamp = longStartTimestamp + 30 * 60 * 1000;
                }
                longStartTimestamp=longStartTimestamp+ 30 * 60 * 1000;
            }

            long tempTimeStamp = longStartTimestamp;


            while (tempTimeStamp <= longEndTimestamp) {
                timeSlot.add(addHalfHour(tempTimeStamp));
                tempTimeStamp = tempTimeStamp + (30 * 60 * 1000);


            }
        } catch (Exception e) {

        }
        return timeSlot;
    }


    private static String addHalfHour(long tempTimeStamp) {
        String time = "";
        try {
            time = convertTimeStampToDateTime(tempTimeStamp);
            Log.d("", time);

        } catch (Exception e) {

        }
        return time;

    }

    private static String convertTimeStampToDateTime(long timeStamp) {

        Date df = new java.util.Date(timeStamp);
        return new SimpleDateFormat("hh:mm a").format(df);


    }


    private static long getTimeStamp_new(String str_date) {
        // str_date="2015-6-22 11:31:00 am";
        SimpleDateFormat formatter;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());

        String strDate = cal.get(Calendar.YEAR) + "-" +
                cal.get(Calendar.MONTH) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
        str_date = strDate + " " + str_date;
        Date date = null;
        formatter = new SimpleDateFormat("yyyy-M-dd hh:mma", Locale.ENGLISH);
        try {
            date = formatter.parse(str_date.toLowerCase());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();

    }

}
