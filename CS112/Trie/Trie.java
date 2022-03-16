package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	private static ArrayList<TrieNode> compArray = new ArrayList<TrieNode>();
	
	
	private static int common(String x, String y)
	{
		int commonWords = 0;
		
		if(x.charAt(0)!=y.charAt(0))
			return commonWords;
		
		int shorterCount = x.length() < y.length() ? x.length() : y.length();
		
		for(int i=0;i<shorterCount;i++)
		{
			if(x.charAt(i)==y.charAt(i))
				commonWords++;
			else
				return commonWords;
		}
		
		return commonWords;	
	}
	
	
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
	
		TrieNode root = new TrieNode(null,null,null);
		
		Indexes newIndex = null;
		TrieNode newWord = null;
		TrieNode ptr = null;
		TrieNode prev = null;
		boolean ifChild = false;
		Indexes oldIndex = null;
		TrieNode oldWord = null;
		boolean ifPrefix = false;
	

		
		if(root.firstChild == null)
		{
			Indexes index = new Indexes(0,(short)0,(short)(allWords[0].length()-1));
			root.firstChild = new TrieNode(index,null,null);
		}
		
		
		for(int i=1;i<allWords.length;i++)
		{
			
				ptr = root.firstChild;
				prev = root;
				
				String addWord = allWords[i];
				
				 while(ptr != null)
				 {
					 
					 String compareWord = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);

					 
					 int commonLength = common(addWord,compareWord);
					 
					 if(commonLength==0)
					 {
							newIndex = new Indexes(i,ptr.substr.startIndex,(short)(allWords[i].length()-1));
							newWord = new TrieNode(newIndex,null,null);
							
							
						prev=ptr;
						ptr=ptr.sibling;
						ifChild =false;
						
					
					 }
					 
					 else
					 {
						 addWord = addWord.substring(commonLength, addWord.length());
						 if(commonLength-1<(ptr.substr.endIndex-ptr.substr.startIndex))
						 {
							newIndex = new Indexes(i,(short)(ptr.substr.startIndex+commonLength),(short)(allWords[i].length()-1));
							newWord = new TrieNode(newIndex,null,null);
							oldIndex = new Indexes(ptr.substr.wordIndex,(short)(ptr.substr.startIndex+commonLength),ptr.substr.endIndex);
							oldWord = new TrieNode(oldIndex,null,null);
							 
							
							if(ptr.substr.endIndex != allWords[ptr.substr.wordIndex].length()-1)
							{
								oldWord.firstChild=ptr.firstChild;
								ptr.firstChild=oldWord;
							}
							
							ptr.substr = new Indexes(ptr.substr.wordIndex,(short)ptr.substr.startIndex,(short)(ptr.substr.startIndex+commonLength-1));
							
							
							
							prev=ptr;
							ptr=ptr.firstChild;
							ifChild=true;
							ifPrefix=false;

						 }
						 else
						 {
							newIndex = new Indexes(i,(short)(ptr.substr.startIndex+commonLength),(short)(allWords[i].length()-1));
							newWord = new TrieNode(newIndex,null,null);
							
							 prev=ptr;
							 ptr=ptr.firstChild;
							 ifChild=true;
							 ifPrefix=true;

							
						 }
			
					 }		 
				 }
		if(ifChild)
		{
			if(ifPrefix)
				prev.firstChild = newWord;
			else
			{
				prev.firstChild = oldWord;
				oldWord.sibling = newWord;	
			}
		}
		else
		{
			prev.sibling = newWord;
		}
				 
	
	}
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords, String prefix) {
		
		compArray.clear();
		
		if(prefix.isEmpty())
			return null;
		
		traverse(root.firstChild,allWords,prefix);
		
		if(compArray.isEmpty())
			return null;
		
		return compArray;
}
	
	private static void traverse(TrieNode ptr,String[] allWords, String prefix)
	{

		
		//TrieNode ptr = root.firstChild;
		
		while(ptr !=null)
		{
			/*
			System.out.print("(");
			System.out.print(ptr.substr.wordIndex);
			System.out.print(ptr.substr.startIndex);
			System.out.print(ptr.substr.endIndex);
			System.out.println(")");
			*/
			
			String ptrWord = allWords[ptr.substr.wordIndex];
			String something = allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex+1);
				
			
			int commonPrefix  = common(ptrWord,prefix);
			int commonSomething = common(prefix,something);

			
			if((commonPrefix == prefix.length()) || (commonSomething == something.length()))
				
			{
				
				if(ptr.firstChild == null)
				{
					
					
					if(allWords[ptr.substr.wordIndex].length() >= prefix.length())
						compArray.add(ptr);
				

				}
				else
				{
					traverse(ptr.firstChild,allWords,prefix);
			
				
				}
				
				if(prefix.equals(allWords[ptr.substr.wordIndex]))
					break;
				if(ptr.sibling==null)
					break;
				
				String commonNext = allWords[ptr.sibling.substr.wordIndex].substring(0, ptr.sibling.substr.endIndex+1);
				if(!(allWords[ptr.sibling.substr.wordIndex].startsWith(prefix) || prefix.startsWith(commonNext)))
					break;
				
				ptr = ptr.sibling;
			}
			
	
			else
				ptr = ptr.sibling;

			
			

		}
			
	}
		

	
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
