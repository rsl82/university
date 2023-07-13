
package project1;
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

    /**
     * Testbed main method to test isValid() method.
     * @param args required for main function.
     */
    public static void main(String[] args)
    {
        //Test 1
        Date date = new Date ("3/39/3022");
        boolean expectedOutput = false;
        boolean actualOutput = date.isValid();
        System.out.println("Test Case #1: A month with an invalid date.");
        testResult(date,expectedOutput,actualOutput);

        //Test 2
        date = new Date("02/29/2022");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #2: This date is not valid, this year is NOT a leap year.");
        testResult(date,expectedOutput,actualOutput);

        //Test 3
        date = new Date("35/05/2022");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #3: A month with an invalid date");
        testResult(date,expectedOutput,actualOutput);

        //Test 4
        date = new Date("00/09/2000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #4: A month cannot be all zeros");
        testResult(date,expectedOutput,actualOutput);

        //Test 5
        date = new Date("06/00/2000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #5: A date cannot be all zeros");
        testResult(date,expectedOutput,actualOutput);

        //Test 6
        date = new Date("06/15/0000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #6: A year cannot be all zeros");
        testResult(date,expectedOutput,actualOutput);

        //Test 7
        date = new Date("00/00/0000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #7: A month or date cannot be all zeros");
        testResult(date,expectedOutput,actualOutput);

        //Test 8
        date = new Date("2/29/2024");
        expectedOutput = true;
        actualOutput = date.isValid();
        System.out.println("Test Case #8: Valid date with leap year");
        testResult(date,expectedOutput,actualOutput);

        //Test 9
        date = new Date("3/1/1997");
        expectedOutput = true;
        actualOutput = date.isValid();
        System.out.println("Test case #9: Valid date");
        testResult(date,expectedOutput,actualOutput);
    }

    /**
     * Printing format for helper method.
     * @param date date to print.
     * @param expected expected output for the test.
     * @param actual actual output for the test.
     */
    private static void testResult(Date date, boolean expected, boolean actual) {
        System.out.println(date.month+"/"+date.day+"/"+date.year+"\t Expected:"+expected+" Actual:"+actual);
    }
}


