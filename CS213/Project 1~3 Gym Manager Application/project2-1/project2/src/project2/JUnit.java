package project2;

/**
 * Class that test specific methods.
 * Including testbed main.
 * @author Ryan S. Lee, Elliott Ng
 */
public class JUnit {

    /**
     * Testing method to test isValid() method.
     */
    private static void isValidTest() {
        //Test 1
        Date date = new Date ("3/39/3022");
        boolean expectedOutput = false;
        boolean actualOutput = date.isValid();
        System.out.println("Test Case #1: A month with an invalid date.");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 2
        date = new Date("02/29/2022");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #2: This date is not valid, this year is NOT a leap year.");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 3
        date = new Date("35/05/2022");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #3: A month with an invalid date");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 4
        date = new Date("00/09/2000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #4: A month cannot be all zeros");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 5
        date = new Date("06/00/2000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #5: A date cannot be all zeros");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 6
        date = new Date("06/15/0000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #6: A year cannot be all zeros");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 7
        date = new Date("00/00/0000");
        expectedOutput = false;
        actualOutput = date.isValid();
        System.out.println("Test Case #7: A month or date cannot be all zeros");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 8
        date = new Date("2/29/2024");
        expectedOutput = true;
        actualOutput = date.isValid();
        System.out.println("Test Case #8: Valid date with leap year");
        testResultDate(date,expectedOutput,actualOutput);

        //Test 9
        date = new Date("3/1/1997");
        expectedOutput = true;
        actualOutput = date.isValid();
        System.out.println("Test case #9: Valid date");
        testResultDate(date,expectedOutput,actualOutput);
    }

    /**
     * Printing format for testing isValid().
     * @param date date to print.
     * @param expected expected output for the test.
     * @param actual actual output for the test.
     */
    private static void testResultDate(Date date, boolean expected, boolean actual) {
        System.out.println(date.monthGetter()+"/"+date.dayGetter()+"/"+date.yearGetter()+"\t Expected:"+expected+" Actual:"+actual);
    }

    /**
     * Testing method to test membershipFee() method.
     */
    private static void membershipFeeTest() {
        Member member = new Member("Ryan","Lee",new Date("03/01/1997"));
        Premium newMember = new Premium(member);

        System.out.println("Testing membership fee for premium membership.");
        System.out.println("Expected Output: $659.89");
        System.out.println("Actual Output: $"+ String.format("%.2f",newMember.membershipFee()));
    }

    /**
     * Test method to test add / delete member or guest to the fitness class.
     * This method can ignore 40 lines restriction, because this is for test.
     */
    private static void addDeleteFitnessTest() {
        FitnessClass class1 = new FitnessClass(Time.MORNING,"Pilates","Kim",Location.BRIDGEWATER);
        FitnessClass class2 = new FitnessClass(Time.MORNING,"Cardio","Davis",Location.BRIDGEWATER);
        FitnessClass class3 = new FitnessClass(Time.MORNING,"Cardio","Emma",Location.EDISON);
        ClassSchedule classList = new ClassSchedule();
        classList.addClass(class1);
        classList.addClass(class2);
        classList.addClass(class3);

        MemberDatabase memberList = new MemberDatabase();
        Member member1 = new Member("Ryan","Lee",new Date("03/01/1997"),new Date("11/11/2023"),Location.BRIDGEWATER);
        Family member2 = new Family(new Member("Rya","Le",new Date("03/01/1997"),new Date("11/11/2023"),Location.BRIDGEWATER));

        memberList.add(member1);
        memberList.add(member2);
        String[] command = {"C","Pilates","Kim","bridgewater","Ryan","Lee","03/01/1997"};
        System.out.println("Case#1 : Add valid member to valid class.");
        GymManager.addDeleteFitnessClass(command,classList,memberList);
        System.out.println("\nCase#2 : Check if adding deals prevent the member check-in twice.");
        GymManager.addDeleteFitnessClass(command,classList,memberList);

        String[] command2 = {"C","Cardio","Davis","Bridgewater","Ryan","Lee","03/01/1997"};
        System.out.println("\nCase#3 : Test if method checks for time conflict.");
        GymManager.addDeleteFitnessClass(command2,classList,memberList);

        String[] command3 = {"C","Pilates","David","Edison","Ryan","Lee","03/01/1997"};
        System.out.println("\nCase#4 : Add to the invalid fitness class.");
        GymManager.addDeleteFitnessClass(command3,classList,memberList);

        String[] command4 = {"C","Pilates","Kim","Bridgewater","R","L","1/1/1999"};
        System.out.println("\nCase #5: Add the person who is not a member");
        GymManager.addDeleteFitnessClass(command4,classList,memberList);

        String[] command5 = {"D","Pilates","Kim","bridgewater","Ryan","lee","03/01/1997"};
        System.out.println("\nCase #6: Drop the person from the class.");
        GymManager.addDeleteFitnessClass(command5,classList,memberList);
        System.out.println("\nCase #7: Drop the person who is not in the class.");
        GymManager.addDeleteFitnessClass(command5,classList,memberList);

        String[] command6 = {"C","Cardio","Emma","edison","ryan","lee","03/01/1997"};
        System.out.println("\nCase #8: Standard memberships cannot get into another region's class.");
        GymManager.addDeleteFitnessClass(command6,classList,memberList);

        String[] command7 = {"C","Cardio","Emma","edison","rya","Le","03/01/1997"};
        System.out.println("\nCase #9: Family memberships can get into any class regardless place of the class.");
        GymManager.addDeleteFitnessClass(command7,classList,memberList);

        String[] command8 = {"CG","pilates","kim","bridgewater","Ryan","Lee","03/01/1997"};
        System.out.println("\nCase #10: Standard memberships do not have guest pass.");
        GymManager.addDeleteFitnessClass(command8,classList,memberList);

        String[] command9 = {"CG","pilates","kim","bridgewater","Rya","Le","03/01/1997"};
        System.out.println("\nCase #11: Family membership has only 1 guest pass.");
        GymManager.addDeleteFitnessClass(command9,classList,memberList);
        GymManager.addDeleteFitnessClass(command9,classList,memberList);
        String[] command10 = {"DG","pilates","kim","bridgewater","Rya","Le","03/01/1997"};
        System.out.println("\nCase #12: Drop guest class works.");
        GymManager.addDeleteFitnessClass(command10,classList,memberList);


    }


    /**
     * Testbed mains for testing methods.
     * @param args Main function arguments.
     */
    public static void main(String[] args) {
        System.out.println("Testing isValid() method.");
        isValidTest();
        System.out.println("\n");
        System.out.println("Testing membership membershipFee() method.");
        membershipFeeTest();
        System.out.println("\n");
        System.out.print("Test add/delete to fitness class.");
        addDeleteFitnessTest();

    }
}
