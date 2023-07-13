package com.example.project3cs213;
/**
 Premium class to represent a Premium instance.
 Extends from Family class
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class Premium extends Family {
    /**
     * Create a Premium object to populate the instance variables.
     *
     * @param fname    the string for the first name
     * @param lname    the string for the last name
     * @param dob      the date of birth for the member
     * @param expire   the expiration date for the member
     * @param location the gym location for the membership
     * @param guestPassCount the number of guest pass for premium object
     */
    public Premium(String fname, String lname, Date dob, Date expire, Location location,
                   int guestPassCount) {
        super(fname, lname, dob, expire, location, guestPassCount);
    }

    /**
     * Get the membershipfee for premium instance
     * @return double for premium membershipfee
     */
    @Override
    public double membershipfee() {
        return ((double) Time.ELEVENMONTHS) * MemberInformation.PREMIUMONTHLYFEE;
    }
    /**
     * Override the increaseGuessPassCount method
     * Add one to the getGuessPassCount variable
     * @return true if success, false if reach the maximum of guest pass number
     */
    @Override
    public boolean increaseGuessPassCount() {
        if ((this.getGuessPassCount() + 1) > MemberInformation.PREMIUMGUESSPASSNUMBER)
            return false;
        this.addOneGuessPassCount();
        return true;
    }
    /**
     * Print the string for the premium instance
     * @return string to print the instance variables for the premium instance
     */
    @Override
    public String toString() {
        String temp = super.toString();
        return temp.substring(0, temp.indexOf("(") - 1) + " (Premium) Guess-pass remaining: " + this.getGuessPassCount();
    }
}
