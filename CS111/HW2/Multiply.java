
public class Multiply {

	public static void main(String[] args)
	{
		int num1, num2;
		
		System.out.print("Enter Number: ");
		num1 = IO.readInt();
		
		System.out.print("Enter Number: ");
		num2 = IO.readInt();
		
		int result = num1 * num2 ;
		
		IO.outputIntAnswer(result);
	}
}
