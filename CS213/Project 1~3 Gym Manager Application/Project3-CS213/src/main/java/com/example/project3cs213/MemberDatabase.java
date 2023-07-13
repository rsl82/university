package com.example.project3cs213;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 MemberDatabase class to store members inside a member list.
 Performs add and remove member, display members with or without order.
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class MemberDatabase {
    private Member [] mlist;
    private int size;
    /**
     Create a MemberDatabase object.
     Initialize one member list array and size of the array.
     */
    public MemberDatabase() {
        this.size = Compare.EMPTY_SIZE; //There is no members in database yet
        mlist = new Member[Compare.ARRAYGROWSIZE]; // Set the initial capacity to 4 members
    }
    /**
     Find the member from the database and return the index of the member.
     Find the member by traversing the member list.
     @param member member who needs to be found.
     @return index if the member is found, Compare.INDEXNOTFOUND otherwise.
     */
    private int find(Member member) {
        for (int index = 0; index < this.size; index++) { // Checks if member is in database
            if (mlist[index].equals(member)) {
                return index; //returns index is member is in database
            }
        }
        return Compare.NOT_FOUND; //returns "NOT_FOUND" if not
    }
    /**
     Grow capacity by 4 into new array and copy old array elements into the new array.
     Find the member from the database and return the index of the member.
     Find the member by traversing the member list.
     */
    private void grow() {
        Member []  oldList = mlist; //Temp array to hold old array
        int newCapacity = this.mlist.length + 4;
        this.mlist = new Member[newCapacity]; // new array with 4 more capacity
        for (int index = 0; index < oldList.length; index++) { //copy old array elements into new array
            mlist[index] = oldList[index];
        }
    }
    /**
     * Given location string, output time location instance
     * @param location string that contains gym location
     * @return Location instance associated with String location input
     */
    private Location assignLocation(String location) {
        if (location.equalsIgnoreCase("SOMERVILLE"))
            return Location.SOMERVILLE;
        if (location.equalsIgnoreCase("BRIDGEWATER"))
            return Location.BRIDGEWATER;
        if (location.equalsIgnoreCase("EDISON"))
            return Location.EDISON;
        if (location.equalsIgnoreCase("PISCATAWAY"))
            return Location.PISCATAWAY;
        if (location.equalsIgnoreCase("FRANKLIN"))
            return Location.FRANKLIN;
        return null;
    }
    /**
     * Load members from a given filename that contains member information.
     * @param fileName string that contains file name.
     * @throws FileNotFoundException if file is not found.
     */
    public void loadMemberFromFile(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            // process the line
            if (line == "")
                continue;
            String[] fileTokens = line.replaceAll("(^\\s+|\\s+$)", "").split("\\s+");
            String lname = fileTokens[0]; // fname
            String fname = fileTokens[1]; // lname
            Date dob = new Date(fileTokens[2]); // dob
            Date expire = new Date(fileTokens[3]); // expire
            Location location = assignLocation(fileTokens[4]); // location
            this.add(new Member(lname, fname, dob, expire, location));
        }
        scan.close();
    }
    /**
     Add a member into the member list.
     Add member who is not already in the list. Grow the list when necessary.
     @param member member to add into the member list.
     @return true if the member is successfully added, false if member is already in the list.
     */
    public boolean add(Member member) {
        for (int i = 0; i < this.size; i++) { // Checks if member is already in database
            if (mlist[i].equals(member)) {
                return false; //if not return false
            }
        }
        if ((this.size + 1) > this.mlist.length ) { //checks if database has enough room for another member
            grow();  //increases the database if it doesn't.
            mlist[size] = member;
            size++;
            return true;
        } else {
            mlist[size] = member;
            size++;
            return true;
        }
    }
    /**
     Remove a member from the member list.
     Remove a member who can be found in the list.
     After removing the member, move all subsequent members one space to the left.
     @param member member to remove from the member list.
     @return true if the member is successfully removed, false if member is not found in the list.
     */
    public boolean remove(Member member) {
        int indexToRemove = find(member);
        if (indexToRemove != Compare.NOT_FOUND) {
            mlist[indexToRemove] = mlist[this.size - 1];
            int tempIndex = indexToRemove;
            while (tempIndex + 1 <= this.size - 1) {
                this.mlist[tempIndex] = this.mlist[tempIndex + 1];
                tempIndex++;
            }
            mlist[tempIndex] = null;
            this.size--;
            return true;
        }
        return false;
    }
    /**
     Append all member information from the MemberDatabase to stringBuilder.
     Display the list of members in the database without sorting (current order in the array.).
     Corresponds to the P command from the GymManager class.
     @param stringBuilder stringBuilder to append membership information.
     @return stringBuilder with the latest information.
     */
    public StringBuilder print(StringBuilder stringBuilder) {
        for (int memberIndex = 0; memberIndex < size; memberIndex++) {
            stringBuilder.append((this.mlist[memberIndex]).toString() + "\n");
        }
        return stringBuilder;
    } //print the array contents as is
    /**
     Print all members from the MemberDatabase order by the county names and then the zip codes.
     If the locations are in the same county, ordered by the zip codes.
     Use insertion sorting algorithm. Corresponds to the PC command from the GymManager class.
     @return string with the latest information.
     */
    public String printByCounty() {
        for (int index = 1; index < this.size ; index++) { //insertion sort algo
            Member currentMem = mlist[index];
            int previousIndex = index - 1;
            while (previousIndex >= 0
                    && mlist[previousIndex].getGymLocation().compare(currentMem.getGymLocation()) < Compare.EQUAL) {
                mlist[previousIndex + 1] = mlist[previousIndex];
                previousIndex--;
            }
            mlist[previousIndex + 1] = currentMem;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n-list of members sorted by county and zipcode-\n");
        stringBuilder = print(stringBuilder);
        stringBuilder.append("-end of list-\n\n");
        return stringBuilder.toString();
    }
    /**
     Print the list of members in the database ordered by the expiration dates.
     If two expiration dates are the same, their order doesn’t matter.
     Use insertion sorting algorithm.
     Corresponds to the PD command.
     @return string with the latest information.
     */
    public String printByExpirationDate() { //works fine
        for (int index = 1; index < this.size; index++) {
            Member currentMem = mlist[index];
            int previousIndex = index - 1;
            while (previousIndex >= 0
                    && mlist[previousIndex].getExpirationDate().compareTo(currentMem.getExpirationDate()) == 1) {
                mlist[previousIndex + 1] = mlist[previousIndex];
                previousIndex--;
            }
            mlist[previousIndex + 1] = currentMem;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n-list of members sorted by membership expiration date-\n");
        stringBuilder = print(stringBuilder);
        stringBuilder.append("-end of list-\n\n");
        return stringBuilder.toString();
    }
    /**
     Print the list of members in the database ordered by the members’ last names and then first names.
     If two members have the same last name, ordered by the first name.
     Use insertion sorting algorithm.
     Corresponds to the PN command from the GymManager class.
     @return string with the latest information.
     */
    public String printByName() {
        for (int index = 1; index < this.size; index++) {
            Member currentMem = mlist[index];
            int previousIndex = index - 1;
            while (previousIndex >= 0
                    && mlist[previousIndex].compareTo(currentMem) > 0) {
                mlist[previousIndex + 1] = mlist[previousIndex];
                previousIndex--;
            }
            mlist[previousIndex + 1] = currentMem;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n-list of members sorted by last name, and first name-\n");
        stringBuilder = print(stringBuilder);
        stringBuilder.append("-end of list-\n\n");
        return stringBuilder.toString();
    }
    /**
     Print the list of members in the database with fees
     @return string with the latest information.
     */
    public String printWithFee() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n-list of members with membership fees-\n");
        for (int memberIndex = 0; memberIndex < size; memberIndex++) {
            stringBuilder.append(mlist[memberIndex] + ", Membership fee: $" + mlist[memberIndex].membershipfee() + "\n");
        }
        stringBuilder.append("-end of list-\n\n");
        return stringBuilder.toString();
    }

    /**
     Populate the expiration date and location of a member.
     Called by GymManager to populate the expiration date and location of a member,
     whose first name, last name, and date of birth are given.
     @param tempMember a temporary member who only has name and date of birth.
     @return member if the population is success, null otherwise.
     */
    public Member getMemberInformation(Member tempMember) {
        int memberIndex = find(tempMember);
        if (memberIndex == Compare.NOT_FOUND) {
            return null;
        }
        return this.mlist[memberIndex];
    }
    /**
     Check expiration date of a member has passed or not.
     Find the member and compare the expiration date with today's date.
     Called by GymManager to check the expiration date.
     @param member the member whose membership expiration date needs to be checked.
     @return true if the expiration date has passed, false otherwise.
     */
    public boolean checkExpirationDate(Member member) {
        int memberIndex = find(member);
        if (memberIndex == Compare.NOT_FOUND) {
            return false;
        }
        Member memberOnCheck = this.mlist[memberIndex];
        Date today = new Date();
        if (today.compareTo(memberOnCheck.getExpirationDate()) == Compare.MORETHAN) {
            return true; //it is expired
        }
        return false; //it is not expired
    }
    /**
     Check a member is the database or not.
     Called by GymManager to check the existence of a member in the database.
     @param member the member who needs to be found in the database.
     @return true if the member is found, false otherwise.
     */
    public boolean isInDatabase(Member member) {
        if (find(member) == Compare.NOT_FOUND) {
            return false;
        }
        return true;
    }
    /**
     Check the member database is empty or not.
     @return true if the database is empty, false otherwise.
     */
    public boolean checkEmptyDatebase() {
        if (this.size == 0)
            return true;
        return false;
    }
    /**
     Check whether member exists and decrease the pass associated with the pass by 1
     @param member input member to check and decrease the available guest pass
     @return true if decrease in guest pass is valid, false otherwise
     */
    public boolean checkAndDecreaseAvailableGuestPass(Member member) {
        int memberIndex = find(member);
        if (member instanceof Premium) {
            Premium premium = (Premium) this.mlist[memberIndex];
            if(!premium.decreaseGuessPassCount())
                return false;
            return true;
        }
        if (member instanceof Family) {
            Family family = (Family) this.mlist[memberIndex];
            if(!family.decreaseGuessPassCount())
                return false;
        }
        return true;
    }
    /**
     Check whether member exists and increase the pass associated with the pass by 1
     @param member input member to check and increase the available guest pass
     @return true if increase in guest pass is valid, false otherwise
     */
    public boolean checkAndIncreaseAvailableGuestPass(Member member) {
        int memberIndex = find(member);
        if (member instanceof Premium) {
            Premium premium = (Premium) this.mlist[memberIndex];
            if(!premium.increaseGuessPassCount()) {
                return false;
            }
            return true;
        }
        if (member instanceof Family) {
            Family family = (Family) this.mlist[memberIndex];
            if(!family.increaseGuessPassCount()) {
                return false;
            }
        }
        return true;
    }
}
