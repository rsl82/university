package project1;
import java.util.Scanner;

/**
 * GymManager is the class that contains the parts that interect with the user.
 * It takes inputs, and print outputs.
 * @author Ryan S. Lee, Elliott Ng
 */
public class GymManager {

    //Avoid magic number
    public static final int SECONDLETTER = 1;
    public static final int IFLEGALAGE = 18;
    public static final int CLASS_COUNT = 3;
    public static final int PILATES = 0;
    public static final int SPINNING = 1;
    public static final int CARDIO = 2;
    public static final int NOT_VALID = -1;

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

        FitnessClass[] classList = new FitnessClass[3];
        classList[0] = new FitnessClass(Time.MORNING,"Pilates","Jennifer");
        classList[1] = new FitnessClass(Time.AFTERNOON,"Spinning","Denise");
        classList[2] = new FitnessClass(Time.AFTERNOON,"Cardio","Kim");

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
    private void commandSwitch(String[] commandToken, MemberDatabase gymMember, FitnessClass[] classList) {
        if(commandToken[0].equals("A")) {
            addMember(commandToken,gymMember);
        }
        else if(commandToken[0].equals("R")) {
            removeMember(commandToken,gymMember);
        }
        else if(commandToken[0].equals("P")) {
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
        else if(commandToken[0].equals("S")) {
            System.out.println("-Fitness classes-");
            printFitnessClass(classList);
        }
        else if(commandToken[0].equals("C")) {
            addFitnessClass(commandToken,classList,gymMember);
        }
        else if(commandToken[0].equals("D")) {
            deleteFitnessClass(commandToken,classList);
        }
        else if(commandToken[0].equals("Q")) {
            System.out.println("Gym Manager terminated.");
        }
        else {
            System.out.println(commandToken[0]+" is an invalid command!");
        }
    }

    /**
     * Adding the member to the member list.
     * @param commandToken Contains the information of the member to add
     * @param gymMember The list the member to be added.
     */
    private static void addMember(String[] commandToken,MemberDatabase gymMember) {
        String fname = commandToken[1];
        fname = nameCaseChanger(fname);
        String lname = commandToken[2];
        lname = nameCaseChanger(lname);
        Date dob = new Date (commandToken[3]);
        if(!dobChecker(dob)) {
            return;
        }
        Date expireDate = new Date (commandToken[4]);
        if(!expireDate.isValid()) {
            System.out.println("Expiration date: " + expireDate.toString() + ": invalid calendar date!");
            return;
        }
        String location = commandToken[5];
        Location locationToEnum = locationChecker(location);
        if(locationToEnum == null) {
            return;
        }
        Member newMember = new Member(fname,lname,dob,expireDate,locationToEnum);
        if(!gymMember.add(newMember)) {
            System.out.println(fname+" "+lname+" is already in the database.");
        }
        else {
            System.out.println(fname+" "+lname+" added.");
        }
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
    private static void printFitnessClass(FitnessClass[] classList) {
        for(int i=0;i<CLASS_COUNT;i++) {
            classList[i].print();
        }
        System.out.println("");
    }

    /**
     * Add the member to the fitness class.
     * It checks if member is valid first and add with fitnessAdder method.
     * @param commandToken Contains information of the member to add and the class that member would take.
     * @param classList Current list of the fitness class.
     * @param gymMember Current member list to check if the member is in the member list.
     */
    private static void addFitnessClass(String[] commandToken,FitnessClass[] classList, MemberDatabase gymMember) {
        String className = commandToken[1];
        String fname = nameCaseChanger(commandToken[2]);
        String lname = nameCaseChanger(commandToken[3]);
        Date dob = new Date(commandToken[4]);
        if(!dob.isValid()) {
            System.out.println("DOB "+ dob.toString() + ": invalid calendar date!");
            return;
        }
        Member newFitMember = new Member(fname,lname,dob);
        newFitMember = gymMember.getMember(newFitMember);
        if(newFitMember == null) {
            System.out.println(fname+ " "+lname + " "+ dob +" is not in the database.");
            return;
        }
        if(ifExpired(newFitMember.expireGetter()) == Date.EARLIER) {
            System.out.println(fname + " "+lname+ " " + newFitMember.dobGetter() + " membership expired.");
            return;
        }
        int classNumber = classChecker(className,classList);
        if(classNumber == NOT_VALID) {
            return;
        }

        fitnessAdder(newFitMember,classList,classNumber);
    }

    /**
     * Remove the member from the fitness class. It will check if there is the member in the list first.
     * @param commandToken information of the member to be removed.
     * @param classList Current list of the classes.
     */
    private static void deleteFitnessClass(String[] commandToken,FitnessClass[] classList) {
        String fitClass = commandToken[1];
        int classNumber = stringToClass(fitClass);
        if(classNumber == NOT_VALID) {
            System.out.println(fitClass + " does not exist.");
            return;
        }
        String fname = commandToken[2];
        String lname = commandToken[3];
        Date dob = new Date(commandToken[4]);
        if(!dob.isValid()) {
            System.out.println("DOB "+ dob.toString() + ": invalid calendar date!");
            return;
        }
        Member deleteMember = new Member(fname,lname,dob);
        if(classList[classNumber].findMember(deleteMember) == MemberDatabase.NOT_FOUND) {
            System.out.println(deleteMember.nameGetter()+" is not a participant in "+classList[classNumber].classNameGetter()+".");
            return;
        }

        classList[classNumber].deleteMember(deleteMember);
        System.out.println(deleteMember.nameGetter()+" dropped "+classList[classNumber].classNameGetter()+".");
    }

    /**
     * Changes string of the fitness class to the index of the fitness classes.
     * @param fitClass string of the fitness class.
     * @return -1 if string is not valid name, index of the fitness class if there is a class that matches with fitClass.
     */
    private static int stringToClass(String fitClass) {
        fitClass = fitClass.toLowerCase();
        if(fitClass.equals("pilates")) {
            return PILATES;
        }
        else if(fitClass.equals("spinning")) {
            return SPINNING;
        }
        else if(fitClass.equals("cardio")) {
            return CARDIO;
        }
        else {
            return NOT_VALID;
        }
    }

    /**
     * Check if the member is already in the class at that time. And add to the class if there is no confliction.
     * @param newFitMember Member that will be added to fitness class.
     * @param classList Current list of the fitness classes.
     * @param classNumber index of the class that will be added.
     */
    private static void fitnessAdder(Member newFitMember,FitnessClass[] classList,int classNumber) {
        if(classList[classNumber].findMember(newFitMember) != MemberDatabase.NOT_FOUND) {
            System.out.println(newFitMember.nameGetter()+" has already checked in "+classList[classNumber].classNameGetter()+".");
            return;
        }
        if(classNumber == SPINNING) {
            if(classList[CARDIO].findMember(newFitMember) != MemberDatabase.NOT_FOUND) {
                System.out.println(classList[classNumber].classNameGetter()+" time conflict -- "+newFitMember.nameGetter()+" has already checked in "+classList[CARDIO].classNameGetter()+".");
                return;
            }
        }
        else if(classNumber == CARDIO) {
            if(classList[SPINNING].findMember(newFitMember) != MemberDatabase.NOT_FOUND) {
                System.out.println(classList[classNumber].classNameGetter()+" time conflict -- "+newFitMember.nameGetter()+" has already checked in "+classList[SPINNING].classNameGetter()+".");
                return;
            }
        }
        System.out.println(newFitMember.nameGetter()+" checked in "+classList[classNumber].classNameGetter()+".");
        classList[classNumber].addMember(newFitMember);

    }

    /**
     * Changes class name to index of the class list.
     * @param className The class name to find.
     * @param classList List of the fitness classes.
     * @return -1 if className is not found in classList, index of the classList if found.
     */
    private static int classChecker(String className,FitnessClass[] classList) {
        for(int classNumber = 0; classNumber<CLASS_COUNT;classNumber ++) {
            if(className.equalsIgnoreCase(classList[classNumber].classNameGetter())) {
                return classNumber;
            }
        }

        System.out.println(className+" class does not exist.");
        return NOT_VALID;
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

