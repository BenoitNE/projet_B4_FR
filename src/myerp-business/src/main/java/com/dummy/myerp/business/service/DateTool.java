package com.dummy.myerp.business.service;

import java.time.LocalDate;
import java.util.Date;

public class DateTool {

    public String getYearNow (){
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        return year;

    }

}
