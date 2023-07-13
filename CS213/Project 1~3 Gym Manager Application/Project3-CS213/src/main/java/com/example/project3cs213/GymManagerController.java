package com.example.project3cs213;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileNotFoundException;
/**
 * GymManagerController class to perform actions triggered by GymManagerView.
 * Connect with Models to perform actions.
 * @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class GymManagerController {
    private MemberDatabase memberDatabase;
    private ClassSchedule classSchedule;
    private StringBuilder stringBuilder;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField fname, lname, studentFname, studentLname;
    @FXML
    private DatePicker dob, studentDob;
    @FXML
    private ToggleGroup type, studentType, gymLocation, studentGymLocation, className, instructor;
    @FXML
    private RadioButton somerville, bridgewater, edison, piscataway, franklin,
    instructorJennifer, instructorKim, instructorDenise, instructorDavis, instructorEmma,
    classSomerville, classBridgewater, classEdison, classPiscataway, classFranklin,
    pilates, spinning, cardio;
    /**
     * Initialize a memberDatabase, classSchedule objects, and connect some of the RadioButtons to ToggleGroups.
     */
    @FXML
    public void initialize() {
        this.memberDatabase = new MemberDatabase();
        this.classSchedule = new ClassSchedule();
        this.groupInstructors();
        this.groupClassLocations();
        this.groupClassNames();
        this.groupGymLocations();
        this.dob.getEditor().setDisable(true);
        this.studentDob.getEditor().setDisable(true);
    }
    /**
     * Group location RadioButtons into gymLocation ToggleGroup
     */
    private void groupGymLocations() {
        this.somerville.setToggleGroup(gymLocation);
        this.bridgewater.setToggleGroup(gymLocation);
        this.edison.setToggleGroup(gymLocation);
        this.piscataway.setToggleGroup(gymLocation);
        this.franklin.setToggleGroup(gymLocation);
    }
    /**
     * Group class name RadioButtons into className ToggleGroup
     */
    private void groupClassNames() {
        this.pilates.setToggleGroup(className);
        this.spinning.setToggleGroup(className);
        this.cardio.setToggleGroup(className);
    }
    /**
     * Group class location RadioButtons into studentGymLocation ToggleGroup for fitness classes
     */
    private void groupClassLocations() {
        this.classSomerville.setToggleGroup(studentGymLocation);
        this.classBridgewater.setToggleGroup(studentGymLocation);
        this.classEdison.setToggleGroup(studentGymLocation);
        this.classPiscataway.setToggleGroup(studentGymLocation);
        this.classFranklin.setToggleGroup(studentGymLocation);
    }
    /**
     * Group instructors RadioButtons into instructor ToggleGroup
     */
    private void groupInstructors() {
        this.instructorJennifer.setToggleGroup(instructor);
        this.instructorKim.setToggleGroup(instructor);
        this.instructorDenise.setToggleGroup(instructor);
        this.instructorDavis.setToggleGroup(instructor);
        this.instructorEmma.setToggleGroup(instructor);
    }
    /**
     * Print the participants of a fitness class
     * @param fitnessClass class to be printed out
     * @param stringBuilder to store participants output for TextArea
     * @return StringBuilder that contains string output for TextArea
     */
    private StringBuilder printParticipants(FitnessClass fitnessClass, StringBuilder stringBuilder) {
        stringBuilder.append("- Participants -\n");
        for (Member member : fitnessClass.getStudentList()) {
            if (member == null)
                continue;
            stringBuilder.append("    " + member.toString() + "\n");
        }
        return stringBuilder;
    }
    /**
     * Print the guests of a fitness class
     * @param fitnessClass class to be printed out
     * @param stringBuilder to store guests output for TextArea
     * @return StringBuilder that contains string output for TextArea
     */
    private StringBuilder printGuests(FitnessClass fitnessClass, StringBuilder stringBuilder) {
        stringBuilder.append("- Guests -\n");
        for (Member member : fitnessClass.getGuestList()) {
            if (member == null)
                continue;
            stringBuilder.append("   " + member.toString() + "\n");
        }
        return stringBuilder;
    }
    /**
     * Print the students and guests of a fitness class
     * Calls printParticipants and printGuests methods to obtain participants and guests information
     * @param fitnessClass class to be printed out
     * @param stringBuilder to store students and guests output for TextArea
     * @return StringBuilder that contains string output for TextArea
     */
    private StringBuilder printClassStudentAndGuestsList(FitnessClass fitnessClass, StringBuilder stringBuilder) {
        if (!fitnessClass.checkEmptyStudentList())
            stringBuilder = this.printParticipants(fitnessClass, stringBuilder);
        if (!fitnessClass.checkGuestStudentList())
            stringBuilder = this.printGuests(fitnessClass, stringBuilder);
        return stringBuilder;
    }
    /**
     * Check if memberDatabase is empty
     * @return true if checkEmptyMemberDatabase is empty, false if it is not empty
     */
    private boolean checkEmptyMemberDatabase() {
        if (this.memberDatabase.checkEmptyDatebase()) {
            messageArea.appendText("Member database is empty!\n");
            return true;
        }
        return false;
    }
    /**
     * Print the fitness class schedule together with students' information.
     * @param stringBuilder to store class information for TextArea
     * @return StringBuilder that contains string output for TextArea
     */
    private StringBuilder printFitnessClasses(StringBuilder stringBuilder) {
        FitnessClass[] fitnessClasses = this.classSchedule.getClassSchedule();
        for (FitnessClass fitnessClass : fitnessClasses) {
            if (fitnessClass == null)
                continue;
            stringBuilder.append(fitnessClass.toString() + "\n");
            stringBuilder = printClassStudentAndGuestsList(fitnessClass, stringBuilder);
        }
        return stringBuilder;
    }
    /**
     * Check whether the input location is a valid gym location.
     * Check if the location exists in Location enum class.
     *
     * @param inputString the string of the name of the gym location
     * @return true if the date is today or future date, false otherwise.
     */
    private boolean checkValidGymLocation(String inputString) {
        for (Location gymLocations : Location.values()) {
            if (inputString.equalsIgnoreCase(gymLocations.name()))
                return true;
        }
        this.messageArea.appendText(inputString + " - invalid location!\n");
        this.stringBuilder = null;
        return false;
    }
    /**
     * Check valid calendar date for member's date of birth or expiration date.
     * Extracts the dates from member and use isValid method from Date class to check
     * whether date is valid.
     *
     * @param member the member with date of birth or expiration date to check valid
     *               calendar date.
     * @return true if the date is a valid calendar date, false otherwise.
     */
    private boolean checkValidCalendarDate(Member member) {
        // Any date that is not a valid calendar date
        if (!member.getDateOfBirth().isValid()) {
            this.messageArea.appendText("DOB " + member.getDateOfBirth().getMonth() + "/" + member.getDateOfBirth().getDay()
                    + "/" + member.getDateOfBirth().getYear() + ": invalid calendar date!\n");
            return false;
        }
        if (member.getExpirationDate().toString().equals("0/0/0")) {
            return true;
        }
        if (!member.getExpirationDate().isValid()) {
            this.stringBuilder.append("Expiration date " + member.getExpirationDate().getMonth() + "/"
                    + member.getExpirationDate().getDay() + "/" + member.getExpirationDate().getYear()
                    + ": invalid calendar date!\n");
            this.messageArea.appendText(stringBuilder.toString());
            this.stringBuilder = null;
            return false;
        }
        return true;
    }
    /**
     * Get the expiration date for a specific membership type.
     * Three months for standard or family membership, one year for premium membership.
     * @param memberType the type of member input.
     * @return Date the expiration date for the respective membership type.
     */
    private Date addExpirationDate(int memberType) {
        Date expirationDate = new Date();
        if (memberType == MemberInformation.STANDARD || memberType == MemberInformation.FAMILY) {
            if (expirationDate.getMonth() + Time.THREEMONTHS > Time.DECEMBER)
                expirationDate.setYear(expirationDate.getYear() + Time.ONEYEAR);
            expirationDate.setMonth((expirationDate.getMonth() + Time.THREEMONTHS) % Time.DECEMBER);
        }
        if (memberType == MemberInformation.PREMIUM)
            expirationDate.setYear(expirationDate.getYear() + 1);
        return expirationDate;
    }
    /**
     * Check the date is today or future date.
     * Compare the results against today's date.
     *
     * @param date the date to check it is today or future date.
     * @return true if the date is today or future date, false otherwise.
     */
    private boolean checkTodayOrFutureDate(Date date) {
        Date today = new Date();
        int compareResult = today.compareTo(date);
        if (compareResult == Compare.EQUAL || compareResult == Compare.LESSTHAN) {
            return true;
        }
        return false;
    }
    /**
     * Obtain information from the type ToggleGroup
     * @return string for the text of the selected type ToggleGroup
     */
    private String getType() {
        return ((RadioButton) type.getSelectedToggle()).getText();
    }
    /**
     * Obtain information from the studentType ToggleGroup
     * @return string for the text of the selected studentType ToggleGroup
     */
    private String getStudentType() {
        return ((RadioButton) studentType.getSelectedToggle()).getText();
    }
    /**
     * Obtain information from the gymLocation ToggleGroup
     * @return string for the text of the selected gymLocation ToggleGroup
     */
    private String getGymLocation() {
        return ((RadioButton) gymLocation.getSelectedToggle()).getText();
    }
    /**
     * Obtain information from the studentGymLocation ToggleGroup
     * @return string for the text of the selected studentGymLocation ToggleGroup
     */
    private String getStudentGymLocation() {
        return ((RadioButton) studentGymLocation.getSelectedToggle()).getText();
    }
    /**
     * Obtain information from the className ToggleGroup
     * @return string for the text of the selected className ToggleGroup
     */
    private String getClassName() {
        return ((RadioButton) className.getSelectedToggle()).getText();
    }
    /**
     * Obtain information from the instructor ToggleGroup
     * @return string for the text of the selected instructor ToggleGroup
     */
    private String getInstructor() {
        return ((RadioButton) instructor.getSelectedToggle()).getText();
    }
    /**
     * Reformat dates obtained from the DatePicker
     * @param inputDate date obtained from DatePicker with format yyyy-mm-dd
     * @return string reformatted string with format mm/dd/yyyy
     */
    private String dateFormatter(String inputDate) {
        String [] results = inputDate.split("-");
        return results[1] + "/" + results[2] + "/" + results[0];
    }
    /**
     Check whether a member's age is more than 18 years old or not.
     @param member member to check valid age.
     @return true if the member is within the valid age, false otherwise.
     */
    private boolean checkValidMemberAge(Member member) {
        Date today = new Date(); //Hardcoded but im going to change to use Calendar Class
        Date memberDob = member.getDateOfBirth();
        if (memberDob.getYear() + Compare.VALIDAGE > today.getYear())
            return false;
        if (memberDob.getYear() + Compare.VALIDAGE == today.getYear()) {
            if (memberDob.getMonth() > today.getMonth()) {
                return false;
            }
            if (memberDob.getMonth() == today.getMonth()) {
                if (memberDob.getDay() > today.getDay())
                    return false;
            }
        }
        return true;
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
     * Append out the membership location restriction to message area.
     * @param newStudent student that have location restriction
     * @param guestPrint string to print for guest
     * @param inputCheckInLocation location to check into the membership
     */
    private void printStandardMembershipLocationRestriction(Member newStudent, String guestPrint,
                                                            Location inputCheckInLocation) {
        // Mary Lindsey checking in BRIDGEWATER, 08807, SOMERSET - standard membership location restriction.
        Location checkInLocation = inputCheckInLocation;
        String standardMembershipPrint = "standard membership";
        if (newStudent instanceof Family || newStudent instanceof Premium) {
            standardMembershipPrint = "guest";
        }
        messageArea.appendText(newStudent.getFirstName() + " " + newStudent.getLastName() + guestPrint
                + " checking in " +
                checkInLocation.toString() + " - " + standardMembershipPrint + " location restriction.\n");
    }
    /**
     * Check valid membership location.
     * @param newStudent that contains membership location
     * @return true if membership location is valid, false otherwise
     */
    private boolean checkValidMembershipLocation(Member newStudent) {
        if (getStudentType().equals("Guest") && (!(newStudent instanceof Family)) && (!(newStudent instanceof Premium))) {
            this.messageArea.appendText("Standard membership - guest check-in is not allowed.\n");
            return false;
        }
        // 1. standard member only membership location
        // 2. family or premium member any location
        if ((!(newStudent instanceof Premium || newStudent instanceof Family)) || (!getStudentType().equals("Member"))) {
            if (!newStudent.getGymLocation().equals(assignLocation(this.getStudentGymLocation()))) {
                String guestPrint = " Guest";
                if (getStudentType().equals("Member")) {
                    guestPrint = "";
                }
                this.printStandardMembershipLocationRestriction(newStudent, guestPrint, assignLocation(this.getStudentGymLocation()));
                return false;
            }
        }
        return true;
    }
    /**
     * Check membership information for member that is adding into memberDatabase.
     * Check for valid calendar date, today is future date or not, and valid membership age.
     * @param newMember that contains membership information
     * @return true if membership information is valid, false otherwise
     */
    private boolean checkMember(Member newMember) {
        Date memberDob = newMember.getDateOfBirth();
        if (!this.checkValidCalendarDate(newMember))
            return false;
        if (this.checkTodayOrFutureDate(newMember.getDateOfBirth())) {
            this.stringBuilder.append("DOB " + memberDob.getMonth() + "/" + memberDob.getDay() + "/" + memberDob.getYear()
                    + ": cannot be today or a future date!\n");
            this.messageArea.appendText(stringBuilder.toString());
            this.stringBuilder = null;
            return false;
        }
        if (!(this.checkValidMemberAge(newMember))) {
            this.stringBuilder.append("DOB " + memberDob.getMonth() + "/" + memberDob.getDay() + "/" + memberDob.getYear()
                    + ": must be 18 or older to join!\n");
            this.messageArea.appendText(stringBuilder.toString());
            this.stringBuilder = null;
            return false;
        }
        return true;
    }
    /**
     * Append error message if the fitness class does not exist in the class schedule
     * @param fitnessClass returned from classSchedule.getFitnessClassWithRightTime method
     * @return true if valid fitness class is found, false otherwise
     */
    private boolean checkFitnessClass(FitnessClass fitnessClass) {
        if (fitnessClass == null) {
            messageArea.appendText(this.getClassName() + " by " + this.getInstructor() + " does not exist at " + this.getStudentGymLocation() + "\n");
            return false;
        }
        return true;
    }

    /**
     * Check whether time conflict of a given member exists or not
     * @param inputFitnessClass fitness class for checking in
     * @param inputMember member to check into the class
     * @return true if time conflicts, false otherwise
     */
    private boolean checkTimeConflict(FitnessClass inputFitnessClass, Member inputMember) {
        for (FitnessClass fitnessClass : this.classSchedule.getClassSchedule()) {
            if (fitnessClass == null)
                continue;
            if (fitnessClass.equals(inputFitnessClass))
                continue;
            for (Member member : fitnessClass.getStudentList()) {
                if (member == null)
                    continue;
                if (member.equals(inputMember)) {
                    if (fitnessClass.getStartTime().equals(inputFitnessClass.getStartTime())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Append time conflict error message on Text Area
     * @param fitnessClass that contains the information to be appended
     */
    private void printTimeConflict(FitnessClass fitnessClass) {
        messageArea.appendText("Time conflict - " + fitnessClass.getClassName().toUpperCase() + " - "
                + fitnessClass.getInstructor().toUpperCase() + ", " + fitnessClass.getStartTime().toString()
                + ", " + fitnessClass.getLocation() + "\n");
    }
    /**
     * Print the check in results of a fitness class
     * @param checkIntType type of member to be printed out
     * @param newStudent student to be printed
     * @param fitnessClass class that needs to be printed out
     */
    private void printCheckInResults(int checkIntType, Member newStudent, FitnessClass fitnessClass) {
        String guest = " (guest)";
        if (checkIntType == MemberInformation.MEMBERCHECKIN)
            guest = "";
        messageArea.appendText(newStudent.getFirstName() + " " + newStudent.getLastName() + guest + " checked in " + fitnessClass + "\n");
        this.stringBuilder = new StringBuilder();
        this.stringBuilder = printClassStudentAndGuestsList(fitnessClass, stringBuilder);
        messageArea.appendText(stringBuilder.toString());
        this.stringBuilder = null;
    }
    /**
     * Update guest information for all members in every fitness class.
     * @param member the member whose guest information has been changed.
     */
    private void updateGuestInformation(Member member) {
        // for every fitness class in classSchedule
        for (FitnessClass fitnessClass : this.classSchedule.getClassSchedule()) {
            if (fitnessClass == null)
                continue;
            // find the family/premium membership and update guest information
            if (fitnessClass.getLocation().equals(member.getGymLocation()))
                fitnessClass.updateGuestInformation(member);
        }
    }
    /**
     * Check into specific fitness class
     * @param newStudent new student to check into the class
     * @param fitnessClass that student is checking into
     */
    private void checkIntoSpecificFitnessClass(Member newStudent, FitnessClass fitnessClass) {
        if (getStudentType().equals("Member")) {
            if (fitnessClass.checkInClass(newStudent)) {
                printCheckInResults(MemberInformation.MEMBERCHECKIN, newStudent, fitnessClass);
                return;
            }
        }
        // check available guest passes and update when available
        if (getStudentType().equals("Guest")) {
            // decrease guest pass count in member in memberdatabase
            if (!this.memberDatabase.checkAndDecreaseAvailableGuestPass(newStudent)) {
                messageArea.appendText(newStudent.getFirstName() + " "
                        + newStudent.getLastName() + " ran out of guest pass.\n");
                return;
            }
            // update guest pass count to current guests in all fitness classes
            this.updateGuestInformation(newStudent);
            // check in the guest to the fitness class
            fitnessClass.checkInGuestClass(newStudent);
            printCheckInResults(MemberInformation.GUESTCHECKIN, newStudent, fitnessClass);
            return;
        }
        messageArea.appendText(newStudent.getFirstName() + " " + newStudent.getLastName() + " already checked in.\n");
    }
    /**
     * Check the whether input name is null and reformat name so that only the first name is uppercase.
     * @param checkType string that identifies membership or fitness class related checks.
     * @return true if the name is not null and reformatted, false otherwise.
     */
    private boolean checkName(String checkType) {
        if (checkType.equals("membership")) {
            if (this.fname.getText().equals("") || this.lname.getText().equals("")) {
                messageArea.appendText("Invalid Name - must enter first name and last name\n");
                return false;
            }
            fname.setText(fname.getText(0, 1).toUpperCase() + fname.getText().substring(1).toLowerCase());
            lname.setText(lname.getText(0, 1).toUpperCase() + lname.getText().substring(1).toLowerCase());
            return true;
        }
        if (this.studentFname.getText().equals("") || this.studentLname.getText().equals("")) {
            messageArea.appendText("Invalid Name - must enter first name and last name\n");
            return false;
        }
        studentFname.setText(studentFname.getText(0, 1).toUpperCase() + studentFname.getText().substring(1).toLowerCase());
        studentLname.setText(studentLname.getText(0, 1).toUpperCase() + studentLname.getText().substring(1).toLowerCase());
        return true;
    }
    /**
     * Check whether class name exists or not.
     * Append error message to Text Area if class does not exist.
     * @param className string class name to be checked
     * @return true if class exists, false otherwise
     */
    private boolean checkClassName(String className) {
        for (String element : MemberInformation.CLASSES) {
            if (element.equalsIgnoreCase(className))
                return true;
        }
        messageArea.appendText(className + " - class does not exist.\n");
        return false;
    }
    /**
     * Check whether instructor exists or not
     * Append error message to Text Area if instructor does not exist.
     * @param instructor string instructor to be checked
     * @return true if instructor exists, false otherwise
     */
    private boolean checkInstructor(String instructor) {
        for (String element : MemberInformation.INSTRUCTORS) {
            if (element.equalsIgnoreCase(instructor))
                return true;
        }
        messageArea.appendText(instructor + " - instructor does not exist.\n");
        return false;
    }
    /**
     * Check whether a member exists in the MemberDatabase.
     * Append error message to Text Area if the member does not exist.
     * @param member the member who wants to check into a specific class.
     * @return true if member is already in the database, false otherwise.
     */
    private boolean checkExistanceInDatabase(Member member) {
        if (!this.memberDatabase.isInDatabase(member)) {
            messageArea.appendText(member.getFirstName() + " " + member.getLastName() + " "
                    + member.getDateOfBirth().toString() + " is not in the database.\n");
            return false;
        }
        return true;
    }
    /**
     * Check student information if instructors, class name, and calendar dates are valid or not
     * @param newStudent to check the information
     * @return true if member is success, false otherwise
     */
    private boolean checkStudent(Member newStudent) {
        if ((!checkClassName(this.getClassName())) || (!checkInstructor(this.getInstructor())) || (!checkValidGymLocation(this.getStudentGymLocation())))
            return false;
        // check valid date of birth
        if ((!this.checkValidCalendarDate(newStudent)) || (!this.checkExistanceInDatabase(newStudent)))// check dob is a valid date
            return false;
        return true;
    }
    /**
     * Append the drop results to TextArea if the dropping from the class is successful.
     * @param checkIntType indicates member or guest.
     * @param newStudent the member or guest who wants to drop from the fitness class.
     */
    private void printDropResults(int checkIntType, Member newStudent) {
        String guest = " Guest";
        if (checkIntType == MemberInformation.MEMBERDROP)
            guest = "";
        messageArea.appendText(newStudent.getFirstName() + " " + newStudent.getLastName() + guest + " done with the class.\n");
    }
    /**
     * Drop a member from a specific fitness class.
     * Called by dropFitnessClass method to perform the action of dropping the
     * member from the class
     * @param member the member that wants to drop the class
     * @param fitnessClass the class that the member wants to drop from
     * @return true if member is successfully dropped from the class, false otherwise.
     */
    private boolean dropSpecificFitnessClass(Member member, FitnessClass fitnessClass) {
        // D pilates Davis piscataway Jonnathan Wei 9/21/1992
        // DG pilates davis Edison Jerry Brown 6/30/1979
        // drop the class
        if (getStudentType().equals("Member")) {
            if (fitnessClass.dropMemberClass(member)) {
                this.printDropResults(MemberInformation.MEMBERDROP, member);
                return true;
            }
        }
        // check available guest passes and update when available
        if (getStudentType().equals("Guest") && this.memberDatabase.checkAndIncreaseAvailableGuestPass(member)) {
            this.updateGuestInformation(member);
            if (fitnessClass.dropGuestClass(member)) {
                this.printDropResults(MemberInformation.GUESTDROP, member);
                return true;
            }
        }
        return false;
    }
    /**
     * Append all member information to TextArea.
     * Corresponds to "Print" in "MemberDatabase" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    // P command
    @FXML
    public void printMemberList(ActionEvent event){
        try {
            if (checkEmptyMemberDatabase())
                return;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n-list of members-\n");
            stringBuilder = this.memberDatabase.print(stringBuilder);
            stringBuilder.append("-end of list-\n\n");
            this.messageArea.appendText(stringBuilder.toString());
        } catch (NullPointerException e) {
            this.messageArea.appendText("-end of list-\n\n");
        }

    }
    /**
     * Append all member information sorted by county to TextArea.
     * Corresponds to "Print by County/Zipcode" in "MemberDatabase" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    // PC command
    @FXML
    public void printByCounty(ActionEvent event){
        if (checkEmptyMemberDatabase())
            return;
        this.messageArea.appendText(this.memberDatabase.printByCounty());
    }
    /**
     * Append all member information sorted by name to TextArea.
     * Corresponds to "Print by Last/First Name" in "MemberDatabase" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    // PN command
    @FXML
    public void printByName(ActionEvent event){
        if (checkEmptyMemberDatabase())
            return;
        this.messageArea.appendText(this.memberDatabase.printByName());
    }
    /**
     * Append all member information sorted by expiration date to TextArea.
     * Corresponds to "Print by Expiration Date" in "MemberDatabase" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    // PD command
    @FXML
    public void printByExpiry(ActionEvent event){
        if (checkEmptyMemberDatabase())
            return;
        this.messageArea.appendText(this.memberDatabase.printByExpirationDate());
    }
    /**
     * Append all membership fee information of all members to TextArea.
     * Corresponds to "Next bill" in "Membership Fee" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    // PF command
    @FXML
    public void printByMembershipFee(ActionEvent event){
        if (checkEmptyMemberDatabase())
            return;
        this.messageArea.appendText(this.memberDatabase.printWithFee());
    }
    /**
     * Load members from "memberList.txt" file.
     * Append all load information to TextArea.
     * Corresponds to "Load member list from file" in "Member Database" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     * @throws FileNotFoundException if file is not found
     */
    // LM command
    @FXML
    public void loadMember(ActionEvent event) throws FileNotFoundException {
        try {
            this.memberDatabase.loadMemberFromFile("memberList.txt");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n-list of members loaded-\n");
            stringBuilder = this.memberDatabase.print(stringBuilder);
            stringBuilder.append("-end of class list-\n\n");
            this.messageArea.appendText(stringBuilder.toString());
        } catch (FileNotFoundException e){
            this.messageArea.appendText("File not found\n");
        }
    }
    /**
     * Load classes from "classSchedule.txt" file.
     * Append all load class information to TextArea.
     * Corresponds to "Load class schedule from file" in "Class Schedule" option at "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    // LS command
    @FXML
    public void loadClasses(ActionEvent event) {
        try {
            this.classSchedule.loadClassesFromFile("classSchedule.txt");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n-Fitness classes loaded-\n");
            stringBuilder = this.printFitnessClasses(stringBuilder);
            stringBuilder.append("-end of class list.\n\n");
            this.messageArea.appendText(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            this.messageArea.appendText("File not found\n");
        }
    }
    // A command
    /**
     * Add a valid member into the MemberDatabase.
     * Add a member who has valid date of birth, age, member expiration date, and
     * gym location.
     * Corresponds to "Add" button in the "Membership" TabPane
     * @param event ActionEvent to be triggered
     */
    @FXML
    public void addMember(ActionEvent event) {
        try {
            if (!this.checkName("membership"))
                return;
            String memberType = this.getType(); // example output: Standard
            String tmpDate = dateFormatter(dob.getValue().toString());
            String location = this.getGymLocation();
            this.stringBuilder = new StringBuilder();
            if ((this.gymLocation.getSelectedToggle() != null) && !checkValidGymLocation(this.getGymLocation()))
                return;
            Member newMember = null;
            if (memberType.equals("Standard"))
                newMember = new Member(fname.getText(), lname.getText(), new Date(tmpDate),
                        this.addExpirationDate(MemberInformation.STANDARD), Location.valueOf(location.toUpperCase()));
            if (memberType.equals("Family"))
                newMember = new Family(fname.getText(), lname.getText(), new Date(tmpDate),
                        this.addExpirationDate(MemberInformation.FAMILY), Location.valueOf(location.toUpperCase()),
                        MemberInformation.FAMILYGUESSPASSNUMBER);
            if (memberType.equals("Premium"))
                newMember = new Premium(fname.getText(), lname.getText(), new Date(tmpDate),
                        this.addExpirationDate(MemberInformation.PREMIUM), Location.valueOf(location.toUpperCase()),
                        MemberInformation.PREMIUMGUESSPASSNUMBER);
            if (!checkMember(newMember))
                return;
            if (this.memberDatabase.add(newMember)) {
                this.stringBuilder.append(newMember.getFirstName() + " " + newMember.getLastName() + " added.\n");
            } else {
                this.stringBuilder.append(newMember.getFirstName() + " " + newMember.getLastName() + " is already in database.\n");
            }
            this.messageArea.appendText(stringBuilder.toString());
            this.stringBuilder = null;
        }
        catch (NullPointerException exception) {
            messageArea.appendText("Invalid date - no date of birth data has been entered\n");
        }
    }
    /**
     * Cancel a member from the MemberDatabase.
     * Remove a member who exists in the MemberDatabase.
     * Corresponds to "Remove" button in the "Membership" TabPane
     * @param event ActionEvent to be triggered
     */
    @FXML
    public void cancelMember(ActionEvent event) {
        try {
            if (!this.checkName("membership"))
                return;
            String tmpDate = dateFormatter(dob.getValue().toString());
            this.stringBuilder = new StringBuilder();
            if (this.memberDatabase.remove(
                    new Member(fname.getText(), lname.getText(), new Date(tmpDate), new Date("00/00/0000"), Location.EDISON))) {
                // Date is set to a random date and locatin is set to EDISION by default
                this.stringBuilder.append(fname.getText() + " " + lname.getText() + " removed.\n");
                messageArea.appendText(stringBuilder.toString());
                stringBuilder = null;
                return;
            }
            this.stringBuilder.append(fname.getText() + " " + lname.getText() + " is not in the database.\n");
            messageArea.appendText(stringBuilder.toString());
            stringBuilder = null;
        }
        catch (NullPointerException exception) {
            messageArea.appendText("Invalid date - no date of birth data has been entered\n");
        }
    }

    /**
     * Append the fitness class schedule information onto TextArea
     * A fitness class shall include the fitness class name, instructor’s name,
     * the time of the class, and the list of members who have already checked in
     * today.
     * Corresponds to "Show all classes" in the "Class Schedule" option in the "Information Hub" TabPane
     * @param event ActionEvent to be triggered
     */
    @FXML
    public void showAllClasses(ActionEvent event) {
        if (this.classSchedule.checkEmpty()) {
            this.messageArea.appendText("Fitness class schedule is empty.\n");
            return;
        }
        this.stringBuilder = new StringBuilder();
        this.stringBuilder.append("\n\n-Fitness classes-\n");
        this.stringBuilder = this.printFitnessClasses(stringBuilder);
        this.stringBuilder.append("\n-end of class list.\n\n");
        this.messageArea.appendText(stringBuilder.toString());
        this.stringBuilder = null;
    }
    /**
     * Check into a fitness class.
     * A member or guest check into a fitness class.
     * Checks whether the member exists in the class, the date of birth is invalid,
     * the fitness class exist or not, whether there is a time conflict with other
     * fitness classes,or whether a member has already check into the class.
     * Corresponds to the "Check In" button at "Fitness Class" TabPane.
     * @param event ActionEvent to be triggered
     */
    @FXML
    public void checkInFitnessClass(ActionEvent event) {
        // C pilates Emma Edison Bill Scanlan 5/1/1999
        // CG cardio Davis bridgewater Jonnathan Wei 9/21/1992
        try {
            if (!this.checkName("fitnessClass"))
                return;
            Member newStudent = new Member(studentFname.getText(), studentLname.getText(), new Date(dateFormatter(studentDob.getValue().toString())), new Date("00/00/0000"),
                    Location.EDISON);
            if (!checkStudent(newStudent))
                return;
            // check expiration date
            if (this.memberDatabase.checkExpirationDate(newStudent)) {
                this.messageArea.appendText(studentFname.getText() + " " + studentLname.getText() + " " + newStudent.getDateOfBirth().toString()
                        + " membership expired.\n");
                return;
            }
            // populate the right expiry date and location with right membership type
            newStudent = this.memberDatabase.getMemberInformation(newStudent);
            // check valid membership location
            if (!checkValidMembershipLocation(newStudent))
                return;
            // create and check available fitness class
            FitnessClass fitnessClass = classSchedule.getFitnessClassWithRightTime(
                    new FitnessClass(this.getClassName(), null, this.getInstructor(), assignLocation(this.getStudentGymLocation())));
            if (!checkFitnessClass(fitnessClass))
                return;
            // check class time conflicts
            if (this.checkTimeConflict(fitnessClass, newStudent)) {
                this.printTimeConflict(fitnessClass);
                return;
            }
            // check into the class
            checkIntoSpecificFitnessClass(newStudent, fitnessClass);
        } catch (NullPointerException exception) {
            messageArea.appendText("Invalid date - no date of birth data has been entered\n");
        }
    }
    /**
     * Drop a member or guest from a specific fitness class.
     * Drop the fitness classes after the member checked into a class.
     * Corresponds to the "Check Out" button at the "Fitness Class" TabPane.
     * @param event
     */
    // D command, to drop the fitness classes after the member checked in to a class
    @FXML
    public void dropFitnessClass(ActionEvent event) {
        try {
            if (!this.checkName("fitnessClass"))
                return;
            // D pilates Davis piscataway Jonnathan Wei 9/21/1992
            // DG pilates davis Edison Jerry Brown 6/30/1979
            Member student = new Member(this.studentFname.getText(), this.studentLname.getText(), new Date(dateFormatter(this.studentDob.getValue().toString())), new Date("00/00/0000"),
                    Location.EDISON);
            if (!checkStudent(student))
                return;
            // create and check available fitness class to be dropped
            FitnessClass fitnessClass = classSchedule.getFitnessClassWithRightTime(
                    new FitnessClass(this.getClassName(), null, this.getInstructor(), assignLocation(this.getStudentGymLocation())));
            if (!checkFitnessClass(fitnessClass))
                return;
            // populate the right expiry date and location with right membership type
            student = this.memberDatabase.getMemberInformation(student);
            if (dropSpecificFitnessClass(student, fitnessClass))
                return;
            messageArea.appendText(student.getFirstName() + " " + student.getLastName() + " did not check in.\n");
        } catch (NullPointerException exception) {
            messageArea.appendText("Invalid date - no date of birth data has been entered\n");
        }
    }
}
