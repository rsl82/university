package project2;

/**
 * Member class is the class that contains the information of one member.
 * Contains name, dob, expiration date, and location.
 * @author Ryan S. Lee, Elliott Ng
 */
public class Member implements Comparable<Member>{
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;

    public static final int EARLIER = -1;
    public static final int EQUAL = 0;
    public static final int LATER = 1;

    public static final int QUARTER = 3;
    public static final int ANNUAL = 11;

    /**
     * Overloaded Constructor that has full information of the member.
     * @param fname first name of the member.
     * @param lname last name of the member.
     * @param dob date of birth of the member.
     * @param expire expiration date of membership.
     * @param location location of the gym.
     */
    public Member(String fname, String lname, Date dob, Date expire, Location location) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = expire;
        this.location = location;
    }

    /**
     * Overloaded constructor but has no expiration date and location.
     * Uses for special cases like comparing.
     * @param fname first name of the member.
     * @param lname last name of the member.
     * @param dob date of birth of the member.
     */
    public Member(String fname,String lname,Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = new Date();
        this.location = Location.BRIDGEWATER;
    }

    /**
     * Clone constructor that just clones the member with new object.
     * @param member member to be copied.
     */
    public Member(Member member) {
        this.fname = new String(member.fname);
        this.lname = new String(member.lname);
        this.dob = new Date(member.dob.toString());
        this.expire = new Date(member.expire.toString());
        this.location = member.location;
    }


    /**
     * toString method that changes member information to string format.
     * @return the information of the member with specific format that required on the program.
     */
    @Override
    public String toString() {
        String dobToString = this.dob.toString();
        String expireToString = this.expire.toString();
        String returnString = this.fname+" "+this.lname+", DOB: "+dobToString+", Membership expire";
        Date today = new Date();
        if(today.compareTo(this.expire) == EARLIER) {
            returnString= returnString + "s ";
        }
        else {
            returnString = returnString + "d ";
        }
        returnString = returnString + expireToString+", Location: "+this.location +", "+this.location.zipcodeGetter()+", "+this.location.countyGetter();
        return returnString;
    }

    /**
     * Check if the member object are equal.
     * @param obj The object to be compared.
     * @return false if they are different, true if they are same.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Member)) {
            return false;
        }
        if(this.fname.equals(((Member) obj).fname) && this.lname.equals(((Member) obj).lname) && (this.dob.compareTo(((Member) obj).dob)) ==0) {
            return true;
        }

        return false;
    }

    /**
     * Compares the member with the name and determine which one is earlier (in the lexicographical order).
     * Compare last name first, if they are same, then compare first name.
     * @param member the member to be compared.
     * @return -1 if this member is earlier, 1 if this member is later, 0 if both are same.
     */
    @Override
    public int compareTo(Member member) {
        if(member.lname.compareTo(this.lname) < 0) {
            return LATER;
        }
        else if(member.lname.compareTo(this.lname) > 0) {
            return EARLIER;
        }
        else {
            if(member.fname.compareTo(this.fname) < 0) {
                return LATER;
            }
            else if(member.fname.compareTo(this.fname) > 0) {
                return EARLIER;
            }
            else {
                return EQUAL;
            }
        }
    }

    /**
     * getter Method that gets the member's Location.
     * @return Location of the gym that the member is in.
     */
    public Location memberLocationGetter() {

        return this.location;
    }

    /**
     * Compares the member with the expiration date.
     * It is used for sorting method in another class.
     * @param member member to be compared.
     * @return result of the compareTo method in Date class.
     */
    public int expireCompare(Member member) {

        return this.expire.compareTo(member.expire);
    }

    /**
     * getter method that gets expiration date with string format.
     * @return expiration date of the member.
     */
    public String expireGetter() {
        return this.expire.toString();
    }

    /**
     * getter method that gets date of birth with string format.
     * @return date of birth of the member.
     */
    public String dobGetter() {
        return this.dob.toString();
    }

    /**
     * getter method that gets the name of the member.
     * @return String of the name with "first last" format.
     */
    public String nameGetter() {
        return this.fname+" "+this.lname;
    }


    /**
     * Calculates the membership fee of the standard membership.
     * @return Next membership fee.
     */
    public double membershipFee() {
        double oneTimeFee = 29.99;
        double monthlyFee = 39.99;
        return oneTimeFee + monthlyFee * QUARTER;
    }
}
