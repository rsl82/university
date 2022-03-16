
public class WordCount {
	
	
	
	public static int countWords (String original, int maxLength)
	{
		String x="";
		int count = 0;
		
		original = original.replaceAll("[^a-zA-Z\\s]", "");   
		while(original.indexOf("  ")!=-1) {
			original = original.replaceAll("  ", " ");
			}
		original = original.replaceAll(" ", "!");
	
		original = original + "! ";
		while(original.indexOf("!!")!=-1) {
			original = original.replaceAll("!!", "!");
			}
		while(original.compareTo(" ") != 0)
		{
			x = original.substring(0, original.indexOf("!"));
		
			
			if(x.length() <= maxLength)
				count++;
			
			original = original.substring(original.indexOf("!")+1);
		
		}
		
			return count;
	}
}
