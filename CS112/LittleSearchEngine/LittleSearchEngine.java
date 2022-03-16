package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		if(docFile == null)
			throw new FileNotFoundException("Document file is not found.");
		
		HashMap<String,Occurrence> wordTable = new HashMap<String,Occurrence>();
		
		Scanner sc = new Scanner(new File(docFile));
		while(sc.hasNext())
		{
			String word = sc.next();
			word = this.getKeyword(word);
			
			if(word!=null)
			{
				if(!wordTable.containsKey(word))
				{
					Occurrence newOcc = new Occurrence(docFile,1);
					wordTable.put(word, newOcc);
				}
				else
					wordTable.get(word).frequency+=1;
				
			}
		
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		
		
		//helper
		
		
	
		
		return wordTable;
		
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
		
		
		for(String keyword: kws.keySet())
		{
			
			
			if(!keywordsIndex.containsKey(keyword))
			{
				ArrayList<Occurrence> keywordOcc = new ArrayList<Occurrence>();
				keywordOcc.add(kws.get(keyword));
				keywordsIndex.put(keyword, keywordOcc);
			}
			else
				keywordsIndex.get(keyword).add(kws.get(keyword));
			
		
			
			insertLastOccurrence(keywordsIndex.get(keyword));
			
			
		}
		
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		
		
		for(int i = word.length()-1;i>=0;i--)
		{
			
		if(word.charAt(i)== '.' || word.charAt(i)== ',' || word.charAt(i)== '?' || word.charAt(i)== ':' || word.charAt(i)== ';' || word.charAt(i)== '!')
			word=word.substring(0,i);
		
		else
			break;
		}
		
		word = word.toLowerCase();
		
		if(noiseWords.contains(word))
			return null;
		if(word.isEmpty())
			return null;
		
		for(int i=0;i<word.length();i++)
		{
			if(word.charAt(i)<'a'|| word.charAt(i)>'z')
				return null;
		}
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		
		if(occs.size() == 1)
			return null;
		
		ArrayList<Integer> middlePoints = new ArrayList<Integer>();
		Occurrence target = occs.remove(occs.size()-1);
		
		int low = 0;
		int high = occs.size()-1;
		int mid = 0;
		
		while(low<=high)
		{
			mid = (low+high)/2;
			middlePoints.add(mid);
			
			if(occs.get(mid).frequency > target.frequency)
				low = mid +1;
				
		
			else if(occs.get(mid).frequency < target.frequency)
				high = mid-1;
				
			else
				break;
		}
		
	
		if(high < 0)
			occs.add(0, target);
		else if(low > occs.size() -1)
			occs.add(target);
		else if(occs.get(mid).frequency < target.frequency)
			occs.add(mid,target);
		else
		{	
			int temp ;
			for(temp = mid+1;temp<occs.size();temp++)
			{	
				if(occs.get(temp).frequency != target.frequency)
					break;
			}
			occs.add(temp,target);
			
		}
			
		
		
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		
		//Helper
		/*
		System.out.println(target);
		System.out.println(middlePoints + " midpoints returned");
		System.out.println(occs);
		*/
		
		
		return middlePoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> top5 = new ArrayList<String>();
		
		kw1=kw1.toLowerCase();
		kw2=kw2.toLowerCase();
		
		ArrayList<Occurrence> word1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> word2 = keywordsIndex.get(kw2);

		if(word1 == null && word2 == null)
			return null;
		
		int array1 = 0;
		int array2 = 0;
		if(word1 != null && word2 != null)
		{
			while((top5.size() != 5) && ( (array1<word1.size()) || (array2<word2.size()) ) )
			{
				if((array1<word1.size()) && array2<word2.size())
				{
					if(word1.get(array1).frequency > word2.get(array2).frequency)
					{
						if(!(top5.contains(word1.get(array1).document)))
							top5.add(word1.get(array1).document);
						
						array1++;
					}
					else if(word1.get(array1).frequency < word2.get(array2).frequency)
					{
						if(!(top5.contains(word2.get(array2).document)))
							top5.add(word2.get(array2).document);
						
						array2++;
					}
					else
					{
						if(!(top5.contains(word1.get(array1).document)))
							top5.add(word1.get(array1).document);
						
						array1++;
					
					}
				}
				
				else
				{
					if(array1<word1.size())
					{
						if(!(top5.contains(word1.get(array1).document)))
							top5.add(word1.get(array1).document);
						
						array1++;
					}
					else if(array2<word2.size())
					{
						if(!(top5.contains(word2.get(array2).document)))
							top5.add(word2.get(array2).document);
						
						array2++;
					}
				}
				
			}
		}
		else
		{
			if(word1==null)
			{
				while(top5.size() != 5 && array2<word2.size())
				{
					if(array2<word2.size())
					{
						if(!(top5.contains(word2.get(array2).document)))
							top5.add(word2.get(array2).document);
						
						array2++;
					}
				}
				
			}
			
			else
			{
				while(top5.size()!=5 && array1<word1.size())
				{
					if(array1<word1.size())
					{
						if(!(top5.contains(word1.get(array1).document)))
							top5.add(word1.get(array1).document);
						
						array1++;
					}
				}
				
			}
		}
		
		
		
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return top5;
	
	}
	

}
