package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	
	/*
	private void bfs(int start, boolean[] visited, Queue<Integer> queue)
	{
		visited[start] = true;
		System.out.println("visiting " + adjLists[start].name);
		queue.enqueue(start);

		while (!queue.isEmpty()) {
			int v = queue.dequeue();
			for (Neighbor nbr=adjLists[v].adjList; nbr != null; nbr=nbr.next) {
				int vnum = nbr.vertexNum;
				if (!visited[vnum]) {
					System.out.println("\n" + adjLists[v].name + "--" + adjLists[vnum].name);
					visited[vnum] = true;
					queue.enqueue(vnum);
				}
			
			}
			
		}
	}
	*/
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		if(g==null)
	         return null;
	     
		if(p1==null || p2 == null)
	         return null;
	   
		if(!g.map.containsKey(p1) || !g.map.containsKey(p2))
	         return null;
	      
	      int n1 = g.map.get(p1);
	      int n2 = g.map.get(p2);

	      Queue<Integer> queue = new Queue<>();
	      boolean[] visited = new boolean[g.members.length];
	      int[] pred = new int[g.members.length];
	      
	    
	      for(int i = 0;i<g.members.length;i++)
	    	  pred[i]=-1;
	     
	      visited[n1]=true;
	      
	      queue.enqueue(n1);
	      
	      while(!queue.isEmpty())
	      {  
	    	  int num = queue.dequeue();
	    	  Person tempPerson = g.members[num];
	    	  
	    	  for(Friend tempP2 = tempPerson.first;tempP2!=null;tempP2=tempP2.next)
	    	  {
	    		  
	    		  if(!visited[tempP2.fnum])
	    		  {
	    			  visited[tempP2.fnum] = true;
	    			  pred[tempP2.fnum] = num;
	    			  
	    			  queue.enqueue(tempP2.fnum);
	    			  
	    			  if(tempP2.fnum == n2)
	    				  break;
	    		  }
	    	  }
	      }
	      
	      if(!visited[n2])
	    	  return null;
	      
	      ArrayList<Integer> intPath = new ArrayList<>();
	      int track = n2;
	      
	      while(track!=-1)
	      {
	    	  intPath.add(track);
	    	  track = pred[track];
	      }
	      
	      ArrayList<String> answer = new ArrayList<>();
	   
	      
	      for(int i = intPath.size() -1 ; i>=0 ; i--)
	      {
	    	  answer.add(g.members[intPath.get(i)].name);
	      }
	      
	     
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return answer;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		if(g==null || school.isEmpty())
			return null;
		
	
		ArrayList<ArrayList<String>> answer = new ArrayList<>();
		
		
		boolean[] visited = new boolean[g.members.length];
		
		for(int i = 0; i<g.members.length;i++)
		{
			
			if(!visited[i])
			{
				visited[i] = true;
				ArrayList<String> clique = new ArrayList<>();
				
				if(g.members[i].school != null && g.members[i].school.equals(school))
				{

					//System.out.println("Clique is added");
					clique.add(g.members[i].name);
					
					Queue<Integer> queue = new Queue<>();
					queue.enqueue(i);
					
					while(!queue.isEmpty())
					{
						 int num = queue.dequeue();
				    	  Person tempPerson = g.members[num];
				    	  
				    	  for(Friend tempP2 = tempPerson.first;tempP2!=null;tempP2=tempP2.next)
				    	  {
				    		  
				    		  if(!visited[tempP2.fnum])
				    		  {
				    			  visited[tempP2.fnum] = true;
				    			  
				    			  if(g.members[tempP2.fnum].school ==null || !g.members[tempP2.fnum].school.equals(school))
				    				  continue;
				    			  
				    			  queue.enqueue(tempP2.fnum);
				    			  clique.add(g.members[tempP2.fnum].name);
				   		  	    }
				    	  }
					}
					
					answer.add(clique);
				}
	
			
			}
			
		}
		
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return answer;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		if(g==null)
			return null;
		
		boolean[] visited = new boolean[g.members.length];
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		int startingPoint = 0;
		int count = 1;
		ArrayList<String> answer = new ArrayList<>();
		
		for(int i=0;i<g.members.length;i++)
		{
			dfsnum[i] = -1;
			back[i]= -1;
		}
		
		for(int i=0;i<g.members.length;i++)
		{
			
			dfs(answer,i,visited,g,dfsnum,back,i,count);
		}
		
		
		for(int i=0;i<g.members.length;i++)
		{
			if(g.members[i].first==null)
				continue;
			
	       
			if ((g.members[i].first.next == null && !answer.contains(g.members[g.members[i].first.fnum].name))) {
	                answer.add(g.members[g.members[i].first.fnum].name);
	            }
	        }
		
		for(int i=0;i<answer.size();i++)
		{
			if(g.members[g.map.get(answer.get(i))].first == null || g.members[g.map.get(answer.get(i))].first.next == null)
			{
				answer.remove(i);
				i--;
			}
				
				
		}
		
		return answer;
	}

	
	
	private static void dfs(ArrayList<String> answer,int v, boolean[] visited,Graph g,int[] dfsnum, int[] back, int startingPoint,int count)
	{
		visited[v] = true;
		if(dfsnum[v]==-1)
		{
			dfsnum[v]=count;
			back[v]=count;
			count++;
		}
		
		for (Friend e=g.members[v].first; e != null; e=e.next) {
			if (!visited[e.fnum]) {
				dfs(answer,e.fnum,visited,g,dfsnum,back,startingPoint,count);
				if(dfsnum[v]>back[e.fnum])
					back[v]=Math.min(back[v], back[e.fnum]);
				
			
			}
			else
			{
				back[v] = Math.min(back[v], dfsnum[e.fnum]);
			}
			
			if(dfsnum[v]<=back[e.fnum] && !(answer.contains(g.members[v].name)) && startingPoint!=v)
			{
					answer.add(g.members[v].name);
			}
		}
		

	}
	
	
}

