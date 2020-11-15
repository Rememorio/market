package com.newbee.maggie.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Year {

    public static int[] year = {-1, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020};

    private Date date;

    private List<Integer> years;

    public Year() {
        date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy");
        String current = ft.format(date).toString();
        try {
            int year = Integer.parseInt(current);
            if (year > 2020) {
                years = new ArrayList<Integer>();
                years.add(-1);
                for (int i = 2010; i <= year; i++){
                    years.add(i);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public List<Integer> getYears() {
        return years;
    }

    /**
     * 判断有没有超过2021年9月1日，没有就直接用静态的年份数组
     * @return
     */
    public int yearChanged() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String nextTerm = "2021-09-01";
        Date nextYear;
        try {
            nextYear = ft.parse(nextTerm);
            if (date.before(nextYear)) {
                return 0;
            } else {
                return 1;
            }
        } catch (ParseException e) {//应该不会有异常
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据年份变化和年份获取下标
     * @param yearChanged
     * @param yearGrade
     * @return
     */
    public int getIndex(int yearChanged, int yearGrade) {
        if (yearChanged == 0) {
            for (int index = 0; index < year.length; index++) {
                if (year[index] == yearGrade) {
                    return index;
                }
            }
        } else if (yearChanged == 1) {
            return years.indexOf(yearGrade);
        }
        return -1;
    }

    /**
     * 根据年份变化和年份下标获取年份
     * @param yearChanged
     * @param yearIndex
     * @return
     */
    public int getYear(int yearChanged, int yearIndex) {
        if (yearChanged == 0) {
            return year[yearIndex];
        } else if (yearChanged == 1) {
            return years.get(yearIndex);
        }
        return 2018;
    }
 }
