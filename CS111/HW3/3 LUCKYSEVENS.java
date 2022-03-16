public class LuckySevens {

    public static void main(String[] args)
    {
        int lowerEnd = IO.readInt();
        int upperEnd = IO.readInt();

        while(lowerEnd>upperEnd)
        {
            IO.reportBadInput();
            return;
        }

        int sevens = 0;

        for(int x = lowerEnd; x<=upperEnd; x++)
        {
            int number = x;
            while(number!=0)
            {
                if(number%10 == -7 || number%10 == 7)
                    sevens++;

                number/=10;
            }
        }

        IO.outputIntAnswer(sevens);
    }
}
