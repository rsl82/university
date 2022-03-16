public class SmallestLargest {

    public static void main(String[] args)
    {
        double terminate,number;
        double smallest = Double.POSITIVE_INFINITY;
        double largest = Double.NEGATIVE_INFINITY;
        int check = 0;

        terminate = IO.readDouble();
        number = IO.readDouble();
        check = check + 1;

        while(terminate!=number)
        {


            if(number<smallest){

                smallest = number;
            }
            if(number>largest)
                largest = number;


            number= IO.readDouble();
            check= check + 1;
        }
        if(check==1)
        {
            IO.reportBadInput();
            return;
        }
        else
        {
            IO.outputDoubleAnswer(smallest);
            IO.outputDoubleAnswer(largest);
        }

    }

}
