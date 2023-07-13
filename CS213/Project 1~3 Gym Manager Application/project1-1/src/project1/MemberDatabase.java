package project1;

/**
 * This is the class that contains the list of the members.
 * @author Ryan S. Lee, Elliott Ng
 */
public class MemberDatabase {
    private Member [] mlist;
    private int size;
    public static final int NOT_FOUND = -1;
    public static final int INCREASE_SIZE = 4;
    public static final int EMPTY = 0;

    /**
     * Default instructor that initialize the 0 list size.
     */
    public MemberDatabase() {
        this.mlist = new Member[INCREASE_SIZE];
        this.size = 0;
    }

    /**
     * Find the member is in the member list.
     * @param member member to find.
     * @return index of the list if the member is found, -1 if not found.
     */
    public int find(Member member) {
        for(int i=0;i<this.size;i++) {
            if(this.mlist[i].equals(member)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    /**
     * Same as the find but return Member instead of index.
     * return New member instead of existing one to avoid bug.
     * @param member member to find.
     * @return Member object if the member is found, null if not found.
     */
    public Member getMember(Member member) {
        if(find(member) == NOT_FOUND) {
            return null;
        }
        Member findMember = new Member(this.mlist[find(member)]);
        return findMember;

    }

    /**
     * Getter method to use find in other classes.
     * @param member member to find.
     * @return result of the find method.
     */
    public int findGetter(Member member) {
        return find(member);
    }

    /**
     * If the list is full of the members, increase the capacity of the list.
     * It creates new array with 4 more size and copy contents from the original list.
     */
    private void grow() {
        int newSize = this.size + INCREASE_SIZE;
        Member[] newMemberList = new Member[newSize];

        for(int i = 0;i<this.size;i++) {
            newMemberList[i] = this.mlist[i];
        }

        this.mlist = newMemberList;
    }

    /**
     * getter method that gets current size of the list.
     * @return size of the list.
     */
    public int sizeGetter() {
        return this.size;
    }

    /**
     * Add the member to the member list.
     * It automatically grows if the array is full.
     * @param member member to be added.
     * @return false if member is already in the list, true if the member is added.
     */
    public boolean add(Member member) {
        if(size>0 && size%4==0) {
            this.grow();
        }
        if(this.find(member) != NOT_FOUND) {
            return false;
        }
        this.mlist[this.size] = member;
        this.size = this.size + 1;
        return true;

    }

    /**
     * Remove the member from the list.
     * @param member member to be removed from the list.
     * @return false if member is not in the list, true if the member is removed from the list.
     */
    public boolean remove(Member member) {
        int findMember = this.find(member);
        if(findMember == NOT_FOUND) {
            return false;
        }

            for(int i=findMember;i<size-1;i++) {
                this.mlist[i] = this.mlist[i+1];
            }
        this.mlist[size-1] = null;
        this.size= this.size -1;

        return true;
    }

    /**
     * Print the list of the member as-is. State that the list is empty if there is no one in the list.
     */
    public void print () {
        if(this.size == 0) {
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println();
        System.out.println("-list of members-");
        for(int i=0;i<this.size;i++) {
            Member memberNow = this.mlist[i];
            System.out.println(memberNow.toString());
        }
        System.out.println("-end of list-");
        System.out.println();
    }

    /**
     * Print the list sorted by zipcode.
     */
    public void printByCounty() {
        if(this.size == 0) {
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println();
        System.out.println("-list of members sorted by country and zipcode-");
        this.sortByCountry();
        for(int i=0;i<this.size;i++) {
            Member memberNow = this.mlist[i];
            System.out.println(memberNow.toString());
        }

        System.out.println("-end of list-");
        System.out.println();
    }


    /**
     * Sort the member list by zipcode.
     * Helper method for printByCounty().
     */
    private void sortByCountry() {

        for(int i=0;i<this.size-1;i++) {
            for(int j=i+1;j<this.size;j++) {
                if(this.mlist[i].memberZipGetter().compareTo(this.mlist[j].memberZipGetter()) > 0) {
                    Member tempMember = this.mlist[i];
                    this.mlist[i] = this.mlist[j];
                    this.mlist[j] = tempMember;
                }
            }
        }
    }

    /**
     * Print the list sorted by expiration date.
     */
    public void printByExpirationDate() {
        if(this.size == 0) {
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println();
        System.out.println("-list of members sorted by membership expiration date-");
        this.sortByExpirationDate();
        for(int i=0;i<this.size;i++) {
            Member memberNow = this.mlist[i];
            System.out.println(memberNow.toString());
        }

        System.out.println("-end of list-");
        System.out.println();

    }


    /**
     * Sort the member list by expiration date.
     * Helper method for printByExpirationDate().
     */
    private void sortByExpirationDate() {

        for(int i=0;i<this.size-1;i++) {
            for(int j=i+1;j<this.size;j++) {
                if(this.mlist[i].expireCompare(this.mlist[j]) > 0) {
                    Member tempMember = this.mlist[i];
                    this.mlist[i] =this.mlist[j];
                    this.mlist[j] = tempMember;
                }
            }
        }
    }

    /**
     * Print the member list sorted by the name.
     */
    public void printByName() {
        if(this.size == 0) {
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println();
        System.out.println("-list of members sorted by last name, and first name-");
        this.sortByName();
        for(int i=0;i<this.size;i++) {
            Member memberNow = this.mlist[i];
            System.out.println(memberNow.toString());
        }

        System.out.println("-end of list-");
        System.out.println();

    }

    /**
     * Sort the member list by the name of the member.
     * Last name first and then first name.
     * Helper method for printByName().
     */
    private void sortByName() {

        for(int i=0;i<this.size-1;i++) {
            for(int j=i+1;j<this.size;j++) {
                if(this.mlist[i].compareTo(this.mlist[j]) > 0) {
                    Member tempMember = this.mlist[i];
                    this.mlist[i] = this.mlist[j];
                    this.mlist[j] = tempMember;
                }
            }
        }

    }

    /**
     * Get the String format of the participants of fitness class.
     * Used different method because the format is slightly different.
     * @return Array of Strings contains participants of the fitness class.
     */
    public String[] fitnessParticipants() {
        String[] participants = new String[this.size];
        for(int i=0;i<this.size;i++) {
            participants[i] = "        " + this.mlist[i].toString();
        }
        return participants;
    }
}