public class LuckyNines {

    public static void main(String[] args)
    {
        int lowerEnd = IO.readInt();
        int upperEnd = IO.readInt();
        int nines = 0;

        nines = countLuckyNines(lowerEnd,upperEnd);
        IO.outputIntAnswer(nines);


    }

    public static int countLuckyNines(int lowerEnd, int upperEnd)
    {
        int nines = 0;

        if(lowerEnd>upperEnd)
            return -1;

        for(int x = lowerEnd; x<=upperEnd; x++)
        {
            int number = x;
            while(number!=0)
            {
                if(number%10 == -9 || number%10 == 9)
                    nines++;

                number/=10;
            }
        }

        return nines;
    }
}
