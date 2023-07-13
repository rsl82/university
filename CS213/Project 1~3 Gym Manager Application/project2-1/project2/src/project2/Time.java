package project2;

/**
 * Time class contains the time that the fitness classes start.
 * Currently only MORNING and AFTERNOON in the program.
 * @author Ryan S. Lee, Elliott Ng
 */
public enum Time {
    MORNING (9, 30), AFTERNOON (14, 00), EVENING(18,30);
    private final int hour;
    private final int minute;

    /**
     * Constructor for enum class, no use in other classes.
     * @param hour hour of the class.
     * @param minute minute of the class.
     */
    Time (int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * It does the same thing to the toString().
     * @return "hh:mm" format String.
     */
    public String printTime() {
        return this.hour+":"+String.format("%02d",this.minute);
    }

}