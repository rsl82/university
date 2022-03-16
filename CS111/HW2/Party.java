

public class Party {
    
    public static void main(String[] args)
    {
        int people, PizzaEach, SodaEach, PiecePizza, CanSoda, totalpizza, totalsoda;
        double total,PizzaCost,SodaCost;
        
        
        people = IO.readInt();
        PizzaEach = IO.readInt();
        SodaEach = IO.readInt();
        PizzaCost = IO.readDouble();
        PiecePizza = IO.readInt();
        SodaCost = IO.readDouble();
        CanSoda = IO.readInt();
        
        totalpizza = people*PizzaEach / PiecePizza;
        totalsoda = people* SodaEach / CanSoda;
        
        if( (people*PizzaEach)%PiecePizza != 0)
            totalpizza = totalpizza + 1;
        if( (people*SodaEach)%CanSoda != 0)
            totalsoda = totalsoda + 1;
        
        total = totalpizza*PizzaCost + totalsoda * SodaCost;
        
        IO.outputDoubleAnswer(total);
            
        
    }

}