package com.example.project3cs213;
/**
 Family class to represent a Family instance.
 Extends from Member class
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class Family extends Member {
    private int guessPassCount;
    /**
     Create a Family object to populate the instance variables.
     @param fname    the string for the first name
     @param lname    the string for the last name
     @param dob      the date of birth for the member
     @param expire   the expiration date for the member
     @param location the gym location for the membership
     @param guessPassCount the number of guest pass for family object
     */
    public Family(String fname, String lname, Date dob, Date expire, Location location,
                  int guessPassCount) {
        super(fname, lname, dob, expire, location);
        this.guessPassCount = guessPassCount;
    }
    /**
     * Get the membership fee for family instance
     * @return double for family membership fee
     */
    @Override
    public double membershipfee() {
        return MemberInformation.FAMILYONETIMEFEE + (((double) Time.THREEMONTHS) * MemberInformation.FAMILYMONTHLYFEE);
    }
    /**
     * Decrease the number of guest pass count
     * @return true if success, false if no more guest pass left
     */
    public boolean decreaseGuessPassCount() {
        if ((this.guessPassCount - 1) < MemberInformation.NOAVAIABLEGUESSPASS)
            return false;
        this.guessPassCount --;
        return true;
    }
    /**
     * Increase the number of guest pass count
     * @return true if success, false if reach the max guest pass number
     */
    public boolean increaseGuessPassCount() {
        if ((this.guessPassCount + 1) > MemberInformation.FAMILYGUESSPASSNUMBER)
            return false;
        this.addOneGuessPassCount();
        return true;
    }
    /**
     * Add one guest pass to the current guest pass variable
     */
    public void addOneGuessPassCount() {
        this.guessPassCount ++;
    }
    /**
     * Get the number of guest pass count for the family instance
     * @return guestPassCount output guest pass count
     */
    public int getGuessPassCount() {
        return this.guessPassCount;
    }
    /**
     * Set the number of guest pass count for the family instance
     * @param guestPassCount input guest pass count
     */
    public void setGuestPassCount(int guestPassCount) {
        this.guessPassCount = guestPassCount;
    }
    /**
     * Print the string for the family instance
     * @return string to print the instance variables for the family instance
     */
    @Override
    public String toString() {
        return super.toString() + ", (Family) guess-pass remaining: " + this.getGuessPassCount();
    }
}
