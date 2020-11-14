package com.newbee.maggie.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
