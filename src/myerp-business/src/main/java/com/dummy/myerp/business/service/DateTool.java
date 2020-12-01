package com.dummy.myerp.business.service;

import java.time.LocalDate;
import java.util.Date;

public class DateTool {

    public Integer getYearNow (){
        LocalDate now = LocalDate.now();
        Integer year = Integer.valueOf(now.getYear());
        return year;

    }

}
