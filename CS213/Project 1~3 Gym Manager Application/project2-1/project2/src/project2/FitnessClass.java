package project2;

import java.util.ArrayList;

/**
 * FitnessClass class contains information for fitness classes and members of the classes.
 * @author Ryan S. Lee, Elliott Ng
 */
public class FitnessClass {

    private Time classTime;
    private String className;
    private String instructorName;

    private Location location;
    private MemberDatabase classMemberList;
    private ArrayList<Member> guestList;

    /**
     * Overloaded constructor that makes new fitness class
     * @param classTime Time object that contains the time to start the class.
     * @param className Name of the class.
     * @param instructorName Name of the instructor who teaches this class.
     */
    public FitnessClass(Time classTime, String className, String instructorName, Location location) {
        this.classTime = classTime;
        this.className = className;
        this.instructorName = instructorName;
        this.location = location;
        this.classMemberList = new MemberDatabase();
        this.guestList = new ArrayList<>();
    }

    /**
     * Constructor that takes fitness class and copy the information.
     * @param fitClass Fitness class to copy.
     */
    public FitnessClass(FitnessClass fitClass) {
        this.classTime = fitClass.classTime;
        this.className = new String(fitClass.className);
        this.instructorName = new String(fitClass.instructorName);
        this.location = fitClass.location;
        this.classMemberList = fitClass.classMemberList;
        this.guestList = fitClass.guestList;
    }

    /**
     * Override equals method that says if two fitness classes are same or not.
     * @param obj Object to compare.
     * @return Return true if both objects are same, return false if they are different.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FitnessClass)) {
            return false;
        }
        if(this.className.equalsIgnoreCase(((FitnessClass) obj).className) && this.instructorName.equalsIgnoreCase(((FitnessClass) obj).instructorName) && this.location == ((FitnessClass) obj).location) {
            return true;
        }

        return false;
    }

    /**
     * Override toString method to print fitness class.
     * @return String of the information of fitness class.
     */
    @Override
    public String toString() {
        return this.className+" - " + this.instructorName + ", " + this.classTime.printTime()+", "+this.location.toString();
    }

    /**
     * Prints the information of the class.
     * If there is a member/guest enrolled to this class, then print all members in this class.
     */
    public void print() {
        System.out.println(this.toString());
        if(this.classMemberList.sizeGetter() != MemberDatabase.EMPTY) {
            System.out.println("    ** participants **");
            String[] participants = this.classMemberList.fitnessParticipants();
            for(int i=0;i<this.classMemberList.sizeGetter();i++) {
                System.out.println(participants[i]);
            }
        }
        if(this.guestList.size() != 0) {
            System.out.println("    ** Guests **");
            for(Member i: this.guestList) {
                System.out.println("        "+i.toString());
            }
        }
    }

    /**
     * Find the specific member in the class.
     * @param member Member to find.
     * @return -1 if no member found, index of the classMemberList if there is a member.
     */
    public int findMember(Member member) {
        return this.classMemberList.find(member);
    }

    /**
     * Delete the specific member from the class.
     * @param member Member to delete.
     */
    public boolean deleteMember(Member member) {
       return this.classMemberList.remove(member);
    }

    /**
     * Add the member to the class.
     * @param member Member to add.
     */
    public void addMember(Member member) {
        this.classMemberList.add(member);
    }


    /**
     * Add the guest to the fitness class.
     * @param member Member who will offer guest pass.
     */
    public void addGuest(Member member) {
        this.guestList.add(member);
    }

    /**
     * Delete guest from the guest List.
     * @param member Guest of this member would be deleted.
     * @return true if delete succeeded, false if delete failed.
     */
    public boolean deleteGuest(Member member) {
        return this.guestList.remove(member);
    }

    /**
     * Getter method that gets location of the fitness class.
     * @return Location of the fitness class.
     */
    public Location locationGetter() {
        return this.location;
    }

    /**
     * Getter method that gets time of the fitness class.
     * @return Time of the fitness class.
     */
    public Time timeGetter() {
        return this.classTime;
    }

}
