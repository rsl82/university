
package project2;
import java.util.Calendar;

/**
 * Date class convert from mm/dd/yyyy String to Date object to use in Member class.
 * Date object can be compared.
 * @author Ryan S. Lee, Elliott Ng
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    //bunch of static variables to avoid "magic number"
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int JANUARY = 1;
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
    public static final int FIRSTDAY = 1;
    public static final int LASTDAY31 = 31;
    public static final int LASTDAY30 = 30;
    public static final int LASTLEAPFEB = 29;
    public static final int LASTFEB = 28;

    public static final int EARLIER = -1;
    public static final int EQUAL = 0;
    public static final int LATER = 1;

    /**
     * Default constructor that creates Date object with today's date.
     */
    public Date() {
        Calendar today = Calendar.getInstance();
        this.year = today.get(Calendar.YEAR);
        this.month = today.get(Calendar.MONTH) + 1; //Because month starts from 0
        this.day = today.get(Calendar.DATE);
    }

    /**
     * Overloaded constructor that creates Date object with given date String.
     * @param date String with "mm/dd/yyyy" format.
     */
    public Date(String date) {
        String[] dateToken = date.split("/");
        this.year = Integer.parseInt(dateToken[2]);
        this.month = Integer.parseInt(dateToken[0]);
        this.day = Integer.parseInt(dateToken[1]);
    }

    /**
     * Compare two date objects and determine which one is earlier.
     * @param date the object to be compared.
     * @return EARLIER if this object is earlier than comparing object, LATER if this object is later than comparing object, EQUAL if both are same.
     */
    @Override
    public int compareTo(Date date) {
        if(this.year > date.year) {
            return LATER;
        }
        else if(this.year < date.year) {
            return EARLIER;
        }
        else {
            if(this.month > date.month) {
                return LATER;
            }
            else if(this.month < date.month) {
                return EARLIER;
            }
            else {
                if(this.day > date.day) {
                    return LATER;
                }
                else if(this.day < date.day) {
                    return EARLIER;
                }
            }
        }
        return EQUAL;
    }

    /**
     * Returns Date object to String format.
     * @return "mm/dd/yyyy" format String.
     */
    @Override
    public String toString() {
        return this.month+"/"+this.day+"/"+this.year;
    }

    /**
     * Check if the date is valid date.
     * It checks about valid year, month, day depends on the month and leap year.
     * @return false if the date is not valid, true if the date is valid.
     */
    public boolean isValid() {
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        if(this.year<=0) {
            return false;
        }
        if(this.month<JANUARY || this.month>DECEMBER) {
            return false;
        }
        if(this.compareTo(new Date("1/1/1900")) == Date.EARLIER) {
            return false;
        }
        if(this.month == JANUARY || this.month == MARCH || this.month == MAY || this.month == JULY || this.month == AUGUST || this.month == OCTOBER || this.month == DECEMBER)
        {
            if(this.day < FIRSTDAY || this.day > LASTDAY31) {
                return false;
            }
        }
        else if(this.month == APRIL || this.month == JUNE || this.month == SEPTEMBER || this.month == NOVEMBER) {
            if(this.day < FIRSTDAY || this.day > LASTDAY30) {
                return false;
            }
        }
        else {
            if(this.leapYearCheck()) {
                if(this.day <FIRSTDAY || this.day > LASTLEAPFEB) {
                    return false;
                }
            }
            else {
                if(this.day < FIRSTDAY || this.day > LASTFEB) {
                    return false;
                }
            }
        }
    return true;
    }

    /**
     * Checks if this year is leap year.
     * Helper method for isValid() method.
     * @return false if this year is not leap year, true if this year is leap year.
     */
    private boolean leapYearCheck() {
        if(this.year % QUADRENNIAL == 0) {
            if(this.year % CENTENNIAL == 0) {
                if(this.year % QUATERCENTENNIAL == 0 ) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Setter method that changes Date object to specific date.
     * @param setYear year to change.
     * @param setMonth month to change.
     * @param setDay day to change.
     */
    public void dateSetter(int setYear, int setMonth, int setDay) {
        this.year = setYear;
        this.month = setMonth;
        if(this.month>=13) {
            this.month= this.month-12;
            this.year = this.year+1;
        }
        this.day = setDay;
    }

    /**
     * Getter method to get year from other classes.
     * @return year of this object.
     */
    public int yearGetter() {
        return this.year;
    }

    /**
     * Getter method to get month from other classes.
     * @return month of this object.
     */
    public int monthGetter() {
        return this.month;
    }

    /**
     * Getter method to get day from other classes.
     * @return day of this object.
     */
    public int dayGetter() {
        return this.day;
    }




}


