package project2;

/**
 * Premium class that extends member class.
 * Member with premium membership will use this class.
 * @author Ryan S. Lee, Elliott Ng
 */
public class Premium extends Member {

    private int guestPass;

    /**
     * Constructor that gets Member object.
     * @param member Member object to copy.
     */
    public Premium(Member member) {
        super(member);
        this.guestPass=3;

    }

    /**
     * Override toString method to get string format of the Premium object.
     * @return String format of the information.
     */
    @Override
    public String toString() {
        return super.toString() +", (Premium) Guest-pass remaining: "+this.guestPass;
    }

    /**
     * Decrease the guestPass by 1 when check-in.
     * Checks if object has enough pass.
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
     * Increase the guestPass by 1 when the guest check-out.
     */
    public void guestCheckOut() {
        this.guestPass = this.guestPass+1;
    }

    /**
     * Calculate the membership fee of Premium object.
     * Override method of Member class.
     * @return membership fee of the Premium object.
     */
    @Override
    public double membershipFee() {
        double monthlyFee = 59.99;
        return monthlyFee * ANNUAL;
    }
}
