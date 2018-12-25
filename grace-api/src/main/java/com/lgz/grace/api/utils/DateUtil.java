package com.lgz.grace.api.utils;

import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    /**
     * 获取截止到某一天24点时间秒差，1 是当天
     * @return
     */
    public  static Long getSecondDiffTo24(int days){
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (calendar.getTime().getTime()-now.getTime())/1000;
    }

}
