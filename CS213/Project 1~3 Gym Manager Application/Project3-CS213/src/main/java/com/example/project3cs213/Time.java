package com.example.project3cs213;
/**
 Time enum class to contain class start time, and constants for checking valid dates
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public enum Time {
    MORNING ("9", "30"),
    AFTERNOON ("14", "00"),
    EVENING("18", "30");
    private final String hour;
    private final String minute;
    /**
     Time constructor to initialize a Time instance for each of the MORNING and AFTERNOON enums.
     @param hour the hour for the MORNING or the AFTERNOON session.
     @param minute the minute for the MORNING or the AFTERNOON session.
     */
    Time(String hour, String minute){
        this.hour = hour;
        this.minute = minute;
    }
    /**
     Override the toString method and print Time object's hour and minute.
     @return string containing hours and minute.
     */
    @Override
    public String toString() {
        return this.hour + ":" + this.minute;
    }
    //list of static constants to help to check valid calendar dates.
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int THREEMONTHS = 3;
    public static final int ELEVENMONTHS = 11;
    public static final int ONEYEAR = 1;
    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;
    public static final int THIRTYDAYSMONTH = 30;
    public static final int THIRTYONEDAYSMONTH = 31;
    public static final int DAYSINLEAPFEBRUARY = 29;
    public static final int DAYSINNONLEAPFEBRUARY = 28;
}