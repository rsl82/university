
public class Compress {

	
	public static String compress(String original)
	{
		int count = 1;
		int initial;
		String result = "";
		for(int i=0;i<original.length()-1;i++)
		{
			if(original.charAt(i) == original.charAt(i+1))
				{
					count++;
					initial = i;
				}
			
			else
			{
				if(count != 1)
					result = result + count + original.substring(i, i+1);
				else
					result = result + original.substring(i,i+1);
				
				count = 1;
				
			}
			
	
			
		}
		
		if(count != 1)
			result = result + count + original.substring(original.length()-1);
		else
			result = result + original.substring(original.length()-1);
		
		return result;
	}
}
