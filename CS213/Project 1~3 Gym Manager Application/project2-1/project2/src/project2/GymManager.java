package project2;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.StringTokenizer;

/**
 * GymManager is the class that contains the parts that interect with the user.
 * It takes inputs, and print outputs.
 * @author Ryan S. Lee, Elliott Ng
 */
public class GymManager {

    //Avoid magic number
    public static final int SECONDLETTER = 1;
    public static final int IFLEGALAGE = 18;


    /**
     * The method that runs the program.
     * Core of the program.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Gym Manger running...");
        String command = "";

        String[] commandToken = {};
        MemberDatabase gymMember = new MemberDatabase();

        ClassSchedule classList = new ClassSchedule();

        do {
            command = sc.nextLine();
            commandToken = command.split(" ");
            if(command.isBlank()) {
                continue;
            }
            commandSwitch(commandToken,gymMember,classList);

        }while(!(commandToken[0].equals("Q")));
    }

    /**
     * Response to given comment, activate the method that do the work for it.
     * Similar to the switch.
     * @param commandToken Command of the input which is split by " ".
     * @param gymMember Member list of the gym.
     * @param classList List of the fitness classes that members can take.
     */
    private void commandSwitch(String[] commandToken, MemberDatabase gymMember, ClassSchedule classList) {
        if(commandToken[0].equals("A") || commandToken[0].equals("LM") || commandToken[0].equals("AF") || commandToken[0].equals("AP")) {
            addMember(commandToken,gymMember);
        }
        else if(commandToken[0].equals("LS")) {
            readClasses(classList);
        }
        else if(commandToken[0].equals("R")) {
            removeMember(commandToken,gymMember);
        }
        else if(commandToken[0].equals("P") || commandToken[0].equals("PC") || commandToken[0].equals("PN") || commandToken[0].equals("PD") || commandToken[0].equals("PF")) {
            memberPrinter(commandToken,gymMember);
        }
        else if(commandToken[0].equals("S")) {
            printFitnessClass(classList);
        }
        else if(commandToken[0].equals("C") || commandToken[0].equals("D") || commandToken[0].equals("CG") || commandToken[0].equals("DG")) {
           addDeleteFitnessClass(commandToken,classList,gymMember);
        }
        else if(commandToken[0].equals("Q")) {
            System.out.println("Gym Manager terminated.");
        }
        else {
            System.out.println(commandToken[0]+" is an invalid command!");
        }
    }

    /**
     * Switch method that has print methods.
     * @param commandToken Command of the input split by " ".
     * @param gymMember Member of the list to print.
     */
    private static void memberPrinter(String[] commandToken, MemberDatabase gymMember) {
        if(commandToken[0].equals("P")) {
            gymMember.print();
        }
        else if(commandToken[0].equals("PC")) {
            gymMember.printByCounty();
        }
        else if(commandToken[0].equals("PN")) {
            gymMember.printByName();
        }
        else if(commandToken[0].equals("PD")) {
            gymMember.printByExpirationDate();
        }
        else if(commandToken[0].equals("PF")) {
            gymMember.printWithFee();
        }
    }

    /**
     * Read the fitness classes from txt file.
     * @param classList ClassSchedule object to add the classes.
     */
    private static void readClasses(ClassSchedule classList) {
        try {
            File classFile = new File("classSchedule.txt");
            Scanner sc = new Scanner(classFile);
            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                if(nextLine.isBlank()) {
                    continue;
                }
                FitnessClass newFitnessClass = classGenerator(nextLine);
                if(classList.findClass(newFitnessClass) == null) {
                    classList.addClass(newFitnessClass);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("There is no classSchedule.txt");
            return;
        }

        System.out.println("");
        System.out.println("-Fitness classes loaded-");
        classList.printClasses();
    }

    /**
     * Make the fitness class from the String format.
     * @param classString String object that contains information of the fitness class.
     * @return FitnessClass object created with offered information.
     */
    private static FitnessClass classGenerator(String classString) {
        StringTokenizer newClass = new StringTokenizer(classString);
        String className = newClass.nextToken().toUpperCase();
        String instructorName = newClass.nextToken().toUpperCase();

        Time classTime = null;
        String classTimeString = newClass.nextToken();
        if(classTimeString.equalsIgnoreCase("morning")) {
            classTime = Time.MORNING;
        }
        else if(classTimeString.equalsIgnoreCase("afternoon")) {
            classTime = Time.AFTERNOON;
        }
        else if(classTimeString.equalsIgnoreCase("evening")) {
            classTime = Time.EVENING;
        }

        Location location = locationChecker(newClass.nextToken());
        return new FitnessClass(classTime,className,instructorName,location);

    }

    /**
     * Switch method for adding methods.
     * @param commandToken Input split by " ".
     * @param gymMember Member list of the program.
     */
    private static void addMember(String[] commandToken, MemberDatabase gymMember) {
        if(commandToken[0].equals("A")) {
            addMemberStandard(commandToken,gymMember);
        }
        else if(commandToken[0].equals("LM")) {
            addMemberList(gymMember);
        }
        else if(commandToken[0].equals("AF")) {
            addMemberFamily(commandToken,gymMember);
        }
        else if(commandToken[0].equals("AP")) {
            addMemberPremium(commandToken,gymMember);
        }
    }

    /**
     * Add the member to the list with Family membership.
     * @param commandToken input split by " ".
     * @param gymMember List of the member to add the new member to this list.
     */
    private static void addMemberFamily(String[] commandToken, MemberDatabase gymMember) {
        Member member = memberGenerator(commandToken);
        if(member == null) {
            return;
        }
        Family newMember = new Family(member);


        if(!gymMember.add(newMember)) {
            System.out.println(newMember.nameGetter()+" is already in the database.");
        }
        else {
            System.out.println(newMember.nameGetter()+" added.");
        }
    }

    /**
     * Add the member to the list with Premium membership.
     * @param commandToken input split by " ".
     * @param gymMember List of the member to add the member to this list.
     */
    private static void addMemberPremium(String[] commandToken, MemberDatabase gymMember) {
        Member member = memberGenerator(commandToken);
        if(member == null) {
            return;
        }
        Premium newMember = new Premium(member);

        if(!gymMember.add(newMember)) {
            System.out.println(newMember.nameGetter()+" is already in the database.");
        }
        else {
            System.out.println(newMember.nameGetter()+" added.");
        }
    }

    /**
     * Make the Member object from the information of the command.
     * @param commandToken input split by " ".
     * @return Member object created by String object.
     */
    private static Member memberGenerator(String[] commandToken) {
        String fname = commandToken[1];
        fname = nameCaseChanger(fname);
        String lname = commandToken[2];
        lname = nameCaseChanger(lname);
        Date dob = new Date (commandToken[3]);
        if(!dobChecker(dob)) {
            return null;
        }
        Date expireDate = new Date ();
        if(commandToken[0].equals("AP")) {
            expireDate.dateSetter(expireDate.yearGetter()+1, expireDate.monthGetter(), expireDate.dayGetter());
        }
        else {
            expireDate.dateSetter(expireDate.yearGetter(), expireDate.monthGetter()+3,expireDate.dayGetter());
        }
        String location = commandToken[4];
        Location locationToEnum = locationChecker(location);
        if(locationToEnum == null) {
            return null;
        }
        return new Member(fname,lname,dob,expireDate,locationToEnum);
    }

    /**
     * Add the member to the list.
     * It is same as project 1's adding member method.
     * @param commandToken input split by " ".
     * @param gymMember List of the member to be added.
     */
    private static void addMemberStandard(String[] commandToken,MemberDatabase gymMember) {

        Member newMember = memberGenerator(commandToken);
        if(newMember == null) {
            return;
        }
        if(!gymMember.add(newMember)) {
            System.out.println(newMember.nameGetter()+" is already in the database.");
        }
        else {
            System.out.println(newMember.nameGetter()+" added.");
        }
    }

    /**
     * Adding the members from txt file.
     * @param gymMember List of the member to be added.
     */
    private static void addMemberList(MemberDatabase gymMember) {
        try {
            File memberFile = new File("memberList.txt");
            Scanner sc = new Scanner(memberFile);
            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                if(nextLine.isBlank()) {
                    continue;
                }
                StringTokenizer newClass = new StringTokenizer(nextLine);
                String fname = nameCaseChanger(newClass.nextToken());
                String lname = nameCaseChanger(newClass.nextToken());
                Date dob = new Date(newClass.nextToken());
                if(!dobChecker(dob)) {
                    continue;
                }
                Date expireDate = new Date(newClass.nextToken());
                if(!expireDate.isValid()) {
                    continue;
                }
                Location location = locationChecker(newClass.nextToken());
                if(location == null) {
                    continue;
                }
                Member newMember = new Member(fname,lname,dob,expireDate,location);
                gymMember.add(newMember);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("There is no memberList.txt");
            return;
        }
        System.out.println("-list of members loaded-");
        gymMember.print();
    }

    /**
     * Check the location provided is valid. It ignores letter cases.
     * @param location the String variable to check.
     * @return the Location object if the String is valid, null if the String in invalid.
     */
    private static Location locationChecker(String location) {
        for(Location gymLocation: Location.values()) {
            if (gymLocation.name().equalsIgnoreCase(location)) {
                return gymLocation;
            }
        }
        System.out.println(location+": invalid location!");
        return null;
    }

    /**
     * Checks if the dob is ok to add to the member.
     * It checks if the dob is valid, if the dob is later than today, and member is the legal age.
     * @param dob Date object to check.
     * @return false if the dob is not qualified for the member, true if dob has no problem.
     */
    private static boolean dobChecker(Date dob) {
        if(!dob.isValid()) {
            System.out.println("DOB "+ dob.toString() + ": invalid calendar date!");
            return false;
        }
        Date today = new Date();
        if(dob.compareTo(today) != Date.EARLIER) {
            System.out.println("DOB "+ dob.toString() + ": cannot be today or a future date!");
            return false;
        }
        Date legalAge = today;
        legalAge.dateSetter(legalAge.yearGetter()-IFLEGALAGE,legalAge.monthGetter(),legalAge.dayGetter());
        if(dob.compareTo(legalAge) > Date.EQUAL ) {
            System.out.println("DOB "+ dob.toString() + ": must be 18 or older to join!");
            return false;
        }


        return true;
    }

    /**
     * Converts the String with capital letter for the first character and small letter for the others.
     * @param name String to be converted.
     * @return Converted String.
     */
    public static String nameCaseChanger(String name) {
        String result = "";
        result = result + Character.toUpperCase(name.charAt(0));
        for(int i=SECONDLETTER;i<name.length();i++) {
            result = result + Character.toLowerCase(name.charAt(i));
        }
        return result;
    }

    /**
     * Removes the member from the member list.
     * @param commandToken Contains the information of the member to remove.
     * @param gymMember Current list of the member.
     */
    private static void removeMember(String[] commandToken,MemberDatabase gymMember) {
        String fname = commandToken[1];
        fname = nameCaseChanger(fname);
        String lname = commandToken[2];
        lname = nameCaseChanger(lname);
        //not sure if I need to check for valid date here
        Date dob = new Date (commandToken[3]);

        Member quitMember = new Member(fname,lname,dob);
        if(!gymMember.remove(quitMember)) {
            System.out.println(fname + " " + lname + " is not in the database.");
        }
        else {
            System.out.println(fname + " " + lname + " removed.");
        }
    }

    /**
     * Print all current fitness classes and members of them.
     * @param classList current list of fitness classes.
     */
    private static void printFitnessClass(ClassSchedule classList) {
        classList.printClasses();
    }

    /**
     * Add or delete the member to the fitness class.
     * It checks if member is valid first and add with fitnessAdder method.
     * @param commandToken Contains information of the member to add and the class that member would take.
     * @param classList Current list of the fitness classes.
     * @param gymMember Current member list to check if the member is in the member list.
     */
    public static void addDeleteFitnessClass(String[] commandToken,ClassSchedule classList, MemberDatabase gymMember) {
        FitnessClass classIndex = classValidChecker(commandToken,classList);
        if(classIndex == null) {
            return;
        }
        Member newFitMember = fitnessMemberChecker(commandToken,gymMember);
        if(newFitMember == null) {
            return;
        }
        if(commandToken[0].equals("C")) {
            if (ifExpired(newFitMember.expireGetter()) == Date.EARLIER) {
                System.out.println(newFitMember.nameGetter() + " " + newFitMember.dobGetter() + " membership expired.");
                return;
            }
            fitnessMemberAdder(newFitMember, classIndex, classList);
        }
        else if(commandToken[0].equals("D")) {
           fitnessMemberDelete(newFitMember,classIndex);
        }

        else if(commandToken[0].equals("CG")) {
            if (ifExpired(newFitMember.expireGetter()) == Date.EARLIER) {
                System.out.println(newFitMember.nameGetter() + " " + newFitMember.dobGetter() + " membership expired.");
                return;
            }
            fitnessGuestAdd(newFitMember,classIndex);
        }
        else if(commandToken[0].equals("DG")) {
            fitnessGuestDelete(newFitMember,classIndex);
        }
    }

    /**
     * Add the guest member to the fitness class.
     * @param member Member to add to the fitness class.
     * @param fitnessClass Fitness class to add the member.
     */
    private static void fitnessGuestAdd(Member member,FitnessClass fitnessClass) {
        if(member instanceof Premium) {
            if(member.memberLocationGetter().equals(fitnessClass.locationGetter()) == false ) {
                System.out.println("Guest location restriction - "+ member.nameGetter()+" Guest checking in "+fitnessClass.locationGetter().toString());
                return;
            }
            if(((Premium)member).guestCheckIn()) {

                fitnessClass.addGuest(member);
                System.out.print(member.nameGetter()+" (guest) checked in ");
                fitnessClass.print();
            }
            else {
                System.out.println(member.nameGetter() + " ran out of guest pass.");
            }
        }
        else if(member instanceof Family) {
            if(member.memberLocationGetter().equals(fitnessClass.locationGetter()) == false ) {
                System.out.println("Guest location restriction - "+ member.nameGetter()+" Guest checking in "+fitnessClass.locationGetter().toString());
                return;
            }
            if(((Family)member).guestCheckIn()) {
                fitnessClass.addGuest(member);
                System.out.print(member.nameGetter()+" (guest) checked in ");
                fitnessClass.print();
            }
            else {
                System.out.println(member.nameGetter() + " ran out of guest pass.");
            }
        }
        else {
            System.out.println("Standard membership - guest check-in is not allowed.");
        }
    }

    /**
     * Delete guest from the fitness class.
     * @param member Member to be deleted.
     * @param fitnessClass Fitness class to delete member.
     */
    private static void fitnessGuestDelete(Member member, FitnessClass fitnessClass) {
        if(fitnessClass.deleteGuest(member)) {
            System.out.println(member.nameGetter()+" Guest done with the class.");
            if(member instanceof Family) {
                ((Family)member).guestCheckOut();
            }
            else if(member instanceof Premium) {
                ((Premium)member).guestCheckOut();
            }
        }
        else {
            System.out.println(member.nameGetter() +" Guest did not check in.");
        }
    }

    /**
     * Helper method to add or delete fitness member.
     * It checks if the command is valid, if the member is in the gymMember database.
     * @param commandToken input split by " ".
     * @param gymMember List of the member to check if the member is in the database.
     * @return null if input is not valid, Member object if member is valid.
     */
    private static Member fitnessMemberChecker(String[] commandToken,MemberDatabase gymMember) {
        String fname = nameCaseChanger(commandToken[4]);
        String lname = nameCaseChanger(commandToken[5]);
        Date dob = new Date(commandToken[6]);
        if(!dob.isValid()) {
            System.out.println("DOB "+ dob.toString() + ": invalid calendar date!");
            return null;
        }
        Member newFitMember = new Member(fname,lname,dob);
        newFitMember = gymMember.getMember(newFitMember);
        if(newFitMember == null) {
            System.out.println(fname+ " "+lname + " "+ dob +" is not in the database.");
            return null;
        }

        return newFitMember;
    }

    /**
     * Delete the fitness member to the fitness class.
     * @param member Member to be deleted.
     * @param fitnessClass Fitness class to delete the member.
     */
    private static void fitnessMemberDelete(Member member,FitnessClass fitnessClass) {
        if(fitnessClass.deleteMember(member) == true) {
            System.out.println(member.nameGetter() + " done with the class.");
        }
        else {
            System.out.println(member.nameGetter() + " did not check in.");
        }
    }

    /**
     * Helper method to add the member to the fitness class.
     * @param member Member to be added.
     * @param fitnessClass Fitness class to add the member to the list.
     * @param classList List of the classes to check time conflict of the member.
     */
    private static void fitnessMemberAdder(Member member,FitnessClass fitnessClass, ClassSchedule classList) {
        if((member instanceof Family == false && member instanceof Premium == false && member.memberLocationGetter().equals(fitnessClass.locationGetter()) == false )) {
            System.out.println(member.nameGetter()+ " checking in "+fitnessClass.locationGetter()+" - standard membership location restriction.");
            return;
        }
        if(fitnessClass.findMember(member) != MemberDatabase.NOT_FOUND) {
            System.out.println(member.nameGetter()+" already checked in.");
            return;
        }
        FitnessClass[] fitClasses = classList.timeConflictChecker(fitnessClass);
        for(FitnessClass i: fitClasses) {
            if(i == null) {
                continue;
            }
            if(i.findMember(member) != MemberDatabase.NOT_FOUND) {
                System.out.println("Time Conflict - " + fitnessClass.toString());
                return;
            }
        }
        fitnessClass.addMember(member);
        System.out.print(member.nameGetter() +" checked in ");
        fitnessClass.print();
    }

    /**
     * Find the String format fitness class from the list of the classes.
     * Method to check if the input of the class is valid.
     * @param commandToken input split by " ".
     * @param classList List of the classes.
     * @return Target fitness class object.
     */
    private static FitnessClass classValidChecker(String[] commandToken,ClassSchedule classList) {
        String className = commandToken[1];
        String instructorName = commandToken[2];
        String location = commandToken[3];

        if(!className.equalsIgnoreCase("cardio") && !className.equalsIgnoreCase("spinning") && !className.equalsIgnoreCase("pilates")) {
            System.out.println(className + " - class does not exist.");
            return null;
        }
        if(!instructorName.equalsIgnoreCase("jennifer") && !instructorName.equalsIgnoreCase("kim") && !instructorName.equalsIgnoreCase("denise") && !instructorName.equalsIgnoreCase("davis") && !instructorName.equalsIgnoreCase("emma")) {
            System.out.println(instructorName + " - instructor does not exist.");
            return null;
        }
        Location enumLocation = locationChecker(location);
        if(enumLocation == null) {
            return null;
        }
        FitnessClass classPlaceHolder = new FitnessClass(Time.EVENING,className.toUpperCase(),instructorName.toUpperCase(),enumLocation);

        FitnessClass classIndex = classList.findClass(classPlaceHolder);
        if(classIndex == null) {
            System.out.println(className + " by " + instructorName + " does not exist at "+location);
            return null;
        }

        return classIndex;
    }



    /**
     * Check the membership is expired or not.
     * @param expireDate the date of expiration
     * @return -1 if expired, 0 or 1 if not expired.
     */
    private static int ifExpired(String expireDate) {
        Date today = new Date();
        Date expiration = new Date(expireDate);
        return expiration.compareTo(today);
    }

}

