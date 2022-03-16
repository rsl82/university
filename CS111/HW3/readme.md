# SmallestLargest

Write a program called SmallestLargest.java which outputs the biggest and smallest numbers in a list of numbers entered by the user.
Ask the user for a terminating value which should be entered again when they are done inputting the list of numbers.
First output the smallest number and then the biggest number.
There must be at least 1 number in the list.
YOU MUST USE THE IO MODULE FOR INPUT/OUTPUT.
Report bad input via IO.reportBadInput() and exit on error.




# TwoLargest

Write your code in the file TwoLargest.java.

We wish to write a program that takes a set of numbers and determines which are the two largest.

Ask the user for the following information, in this order:

A terminating value (real number). The user will enter this value again later, to indicate that he or she is finished providing input.
A sequence of real numbers. Keep asking for numbers until the terminating value is entered.
Compute and output the largest and second-largest real number, in that order. It is possible for the largest and second-largest numbers to be the same (if the sequence contains duplicate numbers).



# Lucky Sevens

Write your code in the file LuckySevens.java. Use the IO module for all inputs and outputs.

Sevens are considered lucky numbers. Your task is to count the number of sevens that appear within a range of numbers. Your solution should make use of looping constructs.

Ask the user for the following information, in this order:

The lower end of the range
The upper end of the range
Determine the number of sevens that appear in the sequence from lower end to upper end (inclusive).
Hint: Some numbers have more than 1 seven, and not every 7 appears in the ones place.
Hint2: Nested loops are helpful

On error return without outputting any answer to the IO.


# Lucky Nines

Write your code in the file LuckyNines.java. Use the IO module for all inputs and outputs.

Your task is to write a method called

public static int countLuckyNines(int lowerEnd, int upperEnd) 

which counts and returns the number of nines that appear within a range of numbers. Your solution should make use of looping constructs.
In main method, ask the user for the following information, in this order:

The lower end of the range
The upper end of the range
Then call countLuckyNines(lowerEnd, upperEnd) with the user input values; countLuckyNines(lowerEnd, upperEnd) returns the number of nines that appear in the sequence from lower end to upper end (inclusive).
Hint: Some numbers have more than 1 nine, and not every 9 appears in the ones place.
Hint2: Nested loops are helpful

On error (i.e. upper end is less than lower end) countLuckyNines returns -1.
