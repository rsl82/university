package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	
	/*
	 private static Node addLast(Node front, float coeff, int degree)
	    {
	        Node ptr=front;
	        
	        if(ptr==null)
	            return new Node(coeff,degree,null);  //If it is empty make new first
	        	
	        while(ptr.next!=null)
	        	ptr=ptr.next;         //Find the rear
	     

	        ptr.next=new Node(coeff,degree,null);
	        return front;
	    }
	 */
	
	 private static Node mergeSort(Node front)
	 {

		 if(front==null || front.next==null)    //if you have 0 or 1 node we don't have to sort
			 return front;
		 
		Node mid = findMid(front);
		Node secondHalf = mid.next;           //Break it up
		mid.next = null;
		
		Node frac1 = mergeSort(front);
		Node frac2 = mergeSort(secondHalf);
		
		Node sortedPoly = merge(frac1,frac2);
		
		return sortedPoly;
		
	 }
	 
	 private static Node merge(Node first, Node second)
	 {
		 if(first==null)
			 return second;
		 if(second==null)
			 return first;
		 
		 Node tmp = null;
		 
		 if(first.term.degree<second.term.degree)
		 {
			 tmp = first;
			 tmp.next = merge(first.next,second);
		 }
		 else
		 {
			 tmp = second;
			 tmp.next = merge(first,second.next);
		 }
		 
		 return tmp;
	 }
	 
	 private static Node findMid(Node front)
	 {
		 if(front==null)
			 return front;
		 
		 Node ptr1 = front;
		 Node ptr2 = front;
		 
		 while(ptr2.next!=null && ptr2.next.next != null)
		 
		 {
			 ptr1 = ptr1.next;
			 ptr2 = ptr2.next.next;
		 }
		 
		 return ptr1;
	 }
	 

	 
	public static Node add(Node poly1, Node poly2) {

		Node addPoly = null;
		
		
	
			while(poly1 != null || poly2 != null)
			{
				

				if(poly1 == null)
				{
					addPoly = new Node(poly2.term.coeff,poly2.term.degree,addPoly);
					poly2 = poly2.next;
				}
				
				else if(poly2 == null)
				{
					addPoly = new Node(poly1.term.coeff,poly1.term.degree,addPoly);
					poly1 = poly1.next;
				}
				
				else if(poly1.term.degree > poly2.term.degree) 
				{
					
					addPoly = new Node(poly2.term.coeff,poly2.term.degree,addPoly);
					poly2 = poly2.next;
				}
				
				else if(poly1.term.degree < poly2.term.degree)
				{
					
					addPoly = new Node(poly1.term.coeff,poly1.term.degree,addPoly);
					poly1 = poly1.next;
				}
				
				else
				{
					float addCoeff = poly1.term.coeff+poly2.term.coeff;
					int degree = poly1.term.degree;
					poly1 = poly1.next;
					poly2 = poly2.next;
					
					if(addCoeff==0.0)
						continue;
					
					addPoly = new Node(addCoeff,degree,addPoly);	
					
				}
			}
			
		addPoly = mergeSort(addPoly);
		return addPoly;
		
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	
	
	public static Node multiply(Node poly1, Node poly2) {
		
		Node multPoly = null;
		
		if(poly2==null || poly1==null)
			return null;
	
		for(Node ptr1 = poly1;ptr1!=null;ptr1=ptr1.next) 
		{
			for(Node ptr2 = poly2;ptr2!=null;ptr2=ptr2.next) 
				
				if(ptr1.term.coeff!=0 && ptr2.term.coeff!=0)
					multPoly = new Node(ptr1.term.coeff*ptr2.term.coeff,ptr1.term.degree+ptr2.term.degree,multPoly);
				
		}
		
		for(Node ptr3 = multPoly;ptr3!=null;ptr3=ptr3.next) 
		{
	
			for(Node ptr4=ptr3;ptr4!=null && ptr4.next!=null;ptr4=ptr4.next)
			{
				
				if(ptr3.term.degree == ptr4.next.term.degree) 
				{
					ptr3.term.coeff = ptr3.term.coeff+ptr4.next.term.coeff;
					ptr4.next=ptr4.next.next;
			
				}
					
			}
			
		}
	
	
		multPoly = mergeSort(multPoly);
		return multPoly;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		
		float answer = 0;
		float power = 1;
		
		for(Node ptr=poly;ptr!=null;ptr=ptr.next)
		{
			power = 1;
			
			if(ptr.term.degree>=0)
			{
				for(int a = 0; a<ptr.term.degree;a++)
				{
				
					if(ptr.term.degree>=1)
						power = power * x;
					else if(ptr.term.degree==0)
						answer=answer+ptr.term.coeff;
				}
			
			}
			else
			{
				for(int a = 0;a>ptr.term.degree;a--)
					power = 1 / (power*x);
			}
			
			answer=answer+power*ptr.term.coeff;
		}
		
		return answer;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
