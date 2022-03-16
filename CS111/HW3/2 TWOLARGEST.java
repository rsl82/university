public class TwoLargest {

    public static void main(String[] args)
    {
        double terminate = IO.readDouble();

        double largest = -999999;
        double secondlarge = -999999;

        double value = -99999999;
        int count = 0;

        while(terminate!=value)
        {
            value = IO.readDouble();
            if(value==terminate && count>=2)
                break;
            else if(value== terminate && count<2)
            {
                while(value == terminate)
                {
                    IO.reportBadInput();
                    value = IO.readDouble();
                }
            }

            if(value>largest) {
                secondlarge = largest;
                largest = value;
            }
            else if(value > secondlarge)
                secondlarge = value;

            count++;

        }

        IO.outputDoubleAnswer(largest);
        IO.outputDoubleAnswer(secondlarge);
    }
}
