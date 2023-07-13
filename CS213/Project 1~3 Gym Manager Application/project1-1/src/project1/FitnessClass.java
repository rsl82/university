package project1;

/**
 * FitnessClass class contains information for fitness classes and members of the classes.
 * @author Ryan S. Lee, Elliott Ng
 */
public class FitnessClass {

    private Time classTime;
    private String className;
    private String instructorName;
    private MemberDatabase classMemberList;

    /**
     * Overloaded constructor that makes new fitness class
     * @param classTime Time object that contains the time to start the class.
     * @param className Name of the class.
     * @param instructorName Name of the instructor who teaches this class.
     */
    public FitnessClass(Time classTime, String className, String instructorName) {
        this.classTime = classTime;
        this.className = className;
        this.instructorName = instructorName;
        this.classMemberList = new MemberDatabase();
    }

    /**
     * Prints the information of the class.
     * If there is a member enrolled to this class, then print all members in this class.
     */
    public void print() {
        System.out.println(this.className+" - " + this.instructorName + " " + this.classTime.printTime());
        if(this.classMemberList.sizeGetter() != MemberDatabase.EMPTY) {
            System.out.println("    ** participants **");
            String[] participants = this.classMemberList.fitnessParticipants();
            for(int i=0;i<this.classMemberList.sizeGetter();i++) {
                System.out.println(participants[i]);
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
    public void deleteMember(Member member) {
        this.classMemberList.remove(member);
    }

    /**
     * Add the member to the class.
     * @param member Member to add.
     */
    public void addMember(Member member) {
        this.classMemberList.add(member);
    }

    /**
     * Getter method that gets name of the class.
     * @return Name of the class.
     */
    public String classNameGetter() {
        return this.className;
    }

}
