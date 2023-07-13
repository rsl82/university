package com.example.project3cs213;
/**
 MemberInformation enum class to contain enum for the membership information
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public enum MemberInformation {
    DEFAULT;
    public static final int STANDARD = 0;
    public static final int FAMILY = 1;
    public static final int PREMIUM = 2;
    public static final int NOAVAIABLEGUESSPASS = 0;
    public static final int FAMILYGUESSPASSNUMBER = 1;
    public static final int PREMIUMGUESSPASSNUMBER = 3;
    public static final double STANDARDONETIMEFEE = 29.99;
    public static final double FAMILYONETIMEFEE = 29.99;
    public static final double STANDARDMONTHLYFEE = 39.99;
    public static final double FAMILYMONTHLYFEE = 59.99;
    public static final double PREMIUMONTHLYFEE = 59.99;

    public static final int MEMBERCHECKIN = 0;
    public static final int GUESTCHECKIN = 1;
    public static final int MEMBERDROP = 0;
    public static final int GUESTDROP = 1;
    public static final String[] INSTRUCTORS = { "Jennifer", "Kim", "Denise", "Davis", "Emma" };
    public static final String[] CLASSES = { "Pilates", "Spinning", "Cardio" };

}
