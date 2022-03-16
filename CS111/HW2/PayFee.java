
public class PayFee {

	  public static void main(String[] args)
	    {
	        double payment, fee1, fee2;
	        double fee= 0.0;
	        
	    
	        payment = IO.readDouble();
	        
	        if(payment<=0)
	            IO.reportBadInput();
	        
	        else if(payment<=500)
	        {
	            fee = 10;
	            IO.outputDoubleAnswer(fee);
	        }
	        
	        else if(payment<5000)
	        {
	            fee1 = payment * 0.01;
	            fee2 = 20.00;
	            if(fee1>fee2)
	                fee = fee1;
	            else
	                fee = fee2;
	            IO.outputDoubleAnswer(fee);
	        }

	        else if(payment<10000)
	        {
	            fee1 = payment * 0.02;
	            fee2 = 120.00;
	            if(fee1>fee2)
	                fee = fee1;
	            else
	                fee = fee2;
	            IO.outputDoubleAnswer(fee);
	        }
	    
	        else
	        {
	            if(payment<=15000)
	                fee = 10000*0.02 + (payment-10000) * 0.03;
	            else
	                fee = 10000*0.02 + 5000 * 0.03 + (payment-15000) * 0.04;
	            IO.outputDoubleAnswer(fee);
	        }
	    
	        
	    }

	    
}
