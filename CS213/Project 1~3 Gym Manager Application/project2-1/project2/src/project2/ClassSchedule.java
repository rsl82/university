package project2;

/**
 * ClassSchedule is the class to contain list of fitness classes.
 * It's essentials are similar to MemberDatabase class.
 * @author Ryan S. Lee, Elliott Ng
 */
public class ClassSchedule {
    private FitnessClass [] classes;
    private int numClasses;

    public static final int CLASS_SIZE=10;


    /**
     * Default constructor that makes new ClassSchedule object.
     * initial size of the array is 10.
     */
    public ClassSchedule() {
        this.classes = new FitnessClass[CLASS_SIZE];
        this.numClasses = 0;
    }

    /**
     * Add the fitness class to the object.
     * Size is increased by 1.
     * @param newClass Fitness class to add.
     */
    public void addClass(FitnessClass newClass) {
        if(this.numClasses % CLASS_SIZE == 0 && this.numClasses != 0) {
            FitnessClass[] classes = new FitnessClass[numClasses+CLASS_SIZE];

            for(int i = 0;i<this.numClasses;i++) {
                classes[i] = this.classes[i];
            }

            this.classes = classes;
        }

        classes[numClasses] = newClass;
        this.numClasses = this.numClasses + 1;
    }


    /**
     * Find the class from the object.
     * @param fitnessClass Fitness class to find.
     * @return return the fitness class of the object if found, null if not found.
     */
    public FitnessClass findClass(FitnessClass fitnessClass) {
        for(int i=0;i<this.numClasses;i++) {
            if(this.classes[i].equals(fitnessClass)) {
                return this.classes[i];
            }
        }

        return null;
    }

    /**
     * Print the current fitness classes in the object.
     */
    public void printClasses() {
        if(this.numClasses == 0) {
            System.out.println("Fitness class schedule is empty.");
            return;
        }

        System.out.println("-Fitness classes-");
        for(int i=0;i<this.numClasses;i++) {
            this.classes[i].print();
        }
        System.out.println("-end of classes-");
        System.out.println("");

    }

    /**
     * Check if there are classes that can cause time conflict.
     * @param fitnessClass Fitness class to compare.
     * @return Array of the fitness classes that can cause time conflict.
     */
    public FitnessClass[] timeConflictChecker(FitnessClass fitnessClass) {
        FitnessClass[] fitnessClasses = new FitnessClass[this.numClasses];
        int count=0;
        for(int i=0;i<this.numClasses;i++) {
            if(this.classes[i].equals(fitnessClass)) {
                continue;
            }
            if(this.classes[i].timeGetter().equals(fitnessClass.timeGetter())) {
                fitnessClasses[count] = new FitnessClass(this.classes[i]);
                count=count+1;
            }
        }
        return fitnessClasses;
    }

}
