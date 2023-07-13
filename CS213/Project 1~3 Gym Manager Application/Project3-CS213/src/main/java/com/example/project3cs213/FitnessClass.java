package com.example.project3cs213;

import java.util.ArrayList;

/**
 * FitnessClass class to store guests list and students list
 * Performs add and remove member, or guests from the class
 * @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class FitnessClass {
    private String className;
    private Time startTime;
    private String instructor;
    private ArrayList<Member> studentList;
    private ArrayList<Member> guestList;
    private Location location;
    /**
     * Initialize a FitnessClass object.
     * Initialize one FitnessClass object with class name, start time, and
     * instructor.
     * 
     * @param className  the name of the fitness class
     * @param startTime  the start time of the fitness class, it can be either
     *                   morning or afternoon
     * @param instructor the name of the instructor of the fitness class
     * @param location the location of the fitness class
     */
    public FitnessClass(String className, Time startTime, String instructor, Location location) {
        this.className = className;
        this.startTime = startTime;
        this.studentList = new ArrayList<Member>();
        this.guestList = new ArrayList<Member>();
        this.instructor = instructor;
        this.location = location;
    }

    /**
     * Check whether the member is in the student list.
     * Traverse the student list to find the student.
     * 
     * @param member the member that needs to be found.
     * @return index if the member is found, Compare.NOT_FOUND otherwise.
     */
    private int findMember(Member member) {
        for (int index = 0; index < this.studentList.size(); index++) { // Checks if member is in student list
            if (member.equals(this.studentList.get(index))) {
                return index; // returns index is member is in array
            }
        }
        return Compare.NOT_FOUND; // returns "NOT FOUND" if not
    }
    /**
     * Check whether the guest is in the guest list.
     * Traverse the guest list to find the student.
     *
     * @param member the guest that needs to be found.
     * @return index if the guest is found, Compare.NOT_FOUND otherwise.
     */
    private int findGuest(Member member) {
        for (int index = 0; index < this.guestList.size(); index++) { // Checks if member is in student list
            if (member.equals(this.guestList.get(index))) {
                return index; // returns index is member is in array
            }
        }
        return Compare.NOT_FOUND; // returns "NOT FOUND" if not
    }

    /**
     * Allow member to check into a class if the student has not checked in.
     * Find whether the student is in the class student list.
     * Add the student into the list by appending to the end of the list.
     * 
     * @param member the member that needs to be checked in.
     * @return true if the member is check in successfully, false if the member is
     *         already checked in.
     */
    public boolean checkInClass(Member member) {
        // checks if member is checked in
        if (this.findMember(member) == Compare.NOT_FOUND) { // if member is not checked in
            this.studentList.add(member);
            return true;
        }
        return false;
    }
    /**
     * Allow guest to check into a class if the guest has not checked in.
     * @param member the member that needs to be checked in.
     */
    public void checkInGuestClass(Member member) {
        this.guestList.add(member);
    }
    /**
     * Update guest information of all other guests given the inputMember
     * @param inputMember the member that has already checked in
     * @return true if information is updated, false if otherwise
     */
    public boolean updateGuestInformation(Member inputMember) {
        for (Member member : studentList) {
            if (member instanceof Premium && inputMember instanceof Premium
                    && member.equals(inputMember)) {
                Premium inputPremium = (Premium) inputMember;
                Premium premium = (Premium) member;
                premium.setGuestPassCount(inputPremium.getGuessPassCount());
                return true;
            }
            if (member instanceof Family && inputMember instanceof Family
                    && member.equals(inputMember)) {
                Family inputFamily = (Family) inputMember;
                Family family = (Family) member;
                family.setGuestPassCount(inputFamily.getGuessPassCount());
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the student list is empty.
     * 
     * @return true if the class has no student, false if otherwise.
     */
    public boolean checkEmptyStudentList() {
        if (this.studentList.size() == 0)
            return true;
        return false;
    }

    /**
     * Check whether the guest list is empty.
     * @return true if the guest list has no guest, false if otherwise.
     */
    public boolean checkGuestStudentList() {
        if (this.guestList.size() == 0)
            return true;
        return false;
    }
    /**
     * Get the guest list
     * @return guestList
     */
    public ArrayList<Member> getGuestList() {
        return this.guestList;
    }

    /**
     * Drop a class for a member.
     * If member exists in the class, drop the member from the student list.
     * Move every subsequence members one position to the left of the array.
     * 
     * @param member who wants to drop the class.
     * @return true if the class has been dropped, false if student is not in the
     *         class.
     */
    public boolean dropMemberClass(Member member) {
        // drop the member from the fitnessclass list
        int indexToRemove = findMember(member);
        if (indexToRemove != Compare.NOT_FOUND) {
            this.studentList.remove(member);
            return true;
        }
        return false;
    }
    /**
     * Drop a class from a guest list.
     * @param member who wants to drop the class.
     * @return true if the class has been dropped, false if student is not in the class.
     */
    public boolean dropGuestClass(Member member) {
        int indexToRemove = findGuest(member);
        if (indexToRemove != Compare.NOT_FOUND) {
            this.guestList.remove(member);
            return true;
        }
        return false;
    }
    /**
     * Get the list of students checked into the class.
     * @return string student list containing list of students checked into the class.
     */
    public ArrayList<Member> getStudentList() {
        return this.studentList;
    }
    /**
     * Get the name of the class
     * @return class name
     */
    public String getClassName() {
        return this.className;
    }
    /**
     * Get the start time of the class
     * @return start time of the class
     */
    public Time getStartTime() {
        return this.startTime;
    }
    /**
     * Get the location of the class
     * @return location of the class
     */
    public Location getLocation() {
        return this.location;
    }
    /**
     * Get the instructor of the class
     * @return instructor of the class
     */
    public String getInstructor() {
        return this.instructor;
    }
    /**
     * Override the toString method and print class name, instructor, start time, location
     * of the class.
     *
     * @return string containing class name, instructor, start time, and location of the class.
     */
    @Override
    public String toString() {
        return this.className.toUpperCase() + " - " + this.instructor.toUpperCase() + ", " + this.startTime.toString()
                + ", " + this.location.name();
    }
    /**
     * Override the equals method and identify the unique fitness class
     * @return true if parameter object and current object are equal, false if otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FitnessClass) {
            FitnessClass fitnessClass = (FitnessClass) obj; // casting
            if (fitnessClass.className.toLowerCase().equals(this.className.toLowerCase()) &&
                    fitnessClass.instructor.toLowerCase().equals(this.instructor.toLowerCase()) &&
                    fitnessClass.location.compare(this.location) == Compare.EQUAL) {
                return true;
            }
        }
        return false;
    }
}
