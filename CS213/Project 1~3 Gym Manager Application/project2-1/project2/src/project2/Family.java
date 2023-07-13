package project2;

/**
 * Family class extends Member class.
 * Member but has family membership.
 * @author Ryan S. Lee, Elliott Ng
 */
public class Family extends Member{

    private int guestPass;


    /**
     * Constructor that make Family object from Member object.
     * @param member object to be converted.
     */
    public Family(Member member) {
        super(member);
        this.guestPass=1;
    }

    /**
     * Override method for toSting().
     * Used super.
     * @return returns the information of the object with String format.
     */
    @Override
    public String toString() {
        return super.toString() +", (Family) Guest-pass remaining: "+this.guestPass;
    }

    /**
     * Decrease the number of the guestPass by 1 when guest check-in.
     * It tells if there is enough guestPass remaining.
     * @return true if check-in succeeded, false if check-in failed.
     */
    public boolean guestCheckIn() {
        if(this.guestPass > 0) {
            this.guestPass = this.guestPass - 1;
            return true;
        }
        return false;
    }

    /**
     * Increase the number of guestPass by 1 when the guest check out.
     */
    public void guestCheckOut() {
        this.guestPass = this.guestPass+1;
    }

    /**
     * Calculates the membership fee.
     * Override method from Member class.
     * @return Membership fee for the Family object.
     */
    @Override
    public double membershipFee() {
        double oneTimeFee = 29.99;
        double monthlyFee = 59.99;
        return oneTimeFee + monthlyFee * QUARTER;
    }
}
