package com.example.project3cs213;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 ClassSchedule class to contain class schedule for the fitness class
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class ClassSchedule {
    private FitnessClass [] classes;
    private int numbClasses;
    /**
     Create class schedule instance.
     Assign numbClasses and classes values.
     */
    public ClassSchedule(){
        this.numbClasses = 0;
        this.classes = new FitnessClass [Compare.ARRAYGROWSIZE];
    }
    /**
     * Given time string, output time location instance
     * @param time string that contains morning, afternoon, or evening
     * @return Time instance associated with String time input
     */
    private Time assignTime(String time) {
        if (time.equalsIgnoreCase("morning"))
            return Time.MORNING;
        if (time.equalsIgnoreCase("afternoon"))
            return Time.AFTERNOON;
        if (time.equalsIgnoreCase("evening"))
            return Time.EVENING;
        return null;
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
     Grow capacity by 4 into new array and copy old array elements into the new array.
     Find the member from the database and return the index of the member.
     Find the member by traversing the member list.
     */
    private void grow() {
        FitnessClass []  oldList = classes; //Temp array to hold old array
        int newCapacity = this.classes.length + 4;
        this.classes = new FitnessClass[newCapacity]; // new array with 4 more capacity
        for (int index = 0; index < oldList.length; index++) { //copy old array elements into new array
            classes[index] = oldList[index];
        }
    }
    /**
     * Parse a given file to load class schedules
     * @param fileName string that contains file name
     * @throws FileNotFoundException if file is not found
     */
    public void loadClassesFromFile(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            // process the line
            if (line == "")
                continue;
            String[] fileTokens = line.replaceAll("(^\\s+|\\s+$)", "").split("\\s+");
            String className = fileTokens[0];
            String instructor = fileTokens[1];
            Time startTime = assignTime(fileTokens[2]);
            Location location = assignLocation(fileTokens[3]);
            this.addFitnessClass(new FitnessClass(className, startTime, instructor, location));
        }
        scan.close();
    }
    /**
     Find the member from the database and return the index of the member.
     Find the member by traversing the member list.
     @param fitnessClass member who needs to be found.
     @return index if the member is found, Compare.NOT_FOUND otherwise.
     */
    public int find(FitnessClass fitnessClass) {
        for (int index = 0; index < this.numbClasses; index++) { // Checks if member is in database
            if (classes[index].equals(fitnessClass)) {
                return index; //returns index is member is in database
            }
        }
        return Compare.NOT_FOUND; //returns "NOT_FOUND" if not
    }
    /**
     Add a member into the member list.
     Add member who is not already in the list. Grow the list when necessary.
     @param fitnessClass fitnessClass to add into the member list.
     @return true if the member is successfully added, false if member is already in the list.
     */
    public boolean addFitnessClass(FitnessClass fitnessClass) {
        if (find(fitnessClass) != Compare.NOT_FOUND){
            return false;
        }
        if ((this.numbClasses + 1) > this.classes.length ) { //checks if database has enough room for another member
            grow();  //increases the database if it doesn't.
            this.classes[numbClasses] = fitnessClass;
            numbClasses++;
            return true;
        } else {
            this.classes[numbClasses] = fitnessClass;
            numbClasses++;
            return true;
        }
    }
    /**
     Populate the given fitness class with the right time
     @param fitnessClass fitnessClass to add into the fitnessClass list.
     @return fitnessClass if the member is gotten, null otherwise
     */
    public FitnessClass getFitnessClassWithRightTime(FitnessClass fitnessClass) {
        // find the class index
        int classIndex = find(fitnessClass);
        if (classIndex == Compare.NOT_FOUND)
            return null;
        // get fitnessclass object
        return this.classes[classIndex];
    }
    /**
     Get the list of fitness classes
     @return list of fitness classes
     */
    public FitnessClass [] getClassSchedule() {
        return this.classes;
    }
    /**
     Check whether class schedule is empty
     @return true if the class schedule is empty, false otherwise
     */
    public boolean checkEmpty() {
        if (this.numbClasses == 0)
            return true;
        return false;
    }
}
