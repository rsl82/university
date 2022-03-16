% CS 314 Fall 2021, HW9
% Due: December 13, 11:59 PM (ET)
%
% Submissions received up to 48 hours past the deadline will be accepted with reduced
% scores.
%
% For this assignment, submit a file named hw6.pl or hw6.pro containing the definitions
% given described below. You may use this file directly, or create your own.
%
% Each relation is worth 3 points. You will receive all 3 points if your definition works
% in the specified "mode". The mode is indicated by placing +, ++, and - before a variable
% name and indicating whether the relation is deterministic.
%
% The prefixes mean:
%    +X     X must be structurally instantiated. If X is an integer, it must be a
%           specific integer. If X is a list, it may contain variables, but it must have
%           a specific length. (That is, the structure of the tree must be known.)
%
%    ++X    X must be fully instantiated (containing no variables)
%
%    -X     X is uninstantiated
%
% "deterministic" means the relation holds in exactly one way
%
% Thus, "zip(+List1, +List2, -List3) is deterministic" means that zip/3 will produce
% a single solution when List1 and List2 are structurally instantiated and the List3 is
% an uninstantiated variable.
%
% You will receive 1 bonus point if your code produces answers for the "most general
% query". That is, if the relation produces infinitely many answers when all parameters
% are uninstantiated.
%
% The maximum possible score for this assignment is 20/12.
%
% You will need to define additional relations to support your definitions.

% import CLP(FD) definitions -- strongly encouraged
:- use_module(library(clpfd)).

% zip(+List1, +List2, -List3) is deterministic
%
% zip/3 is a relation between three lists of the same length, where each element of
% List3 is a pair X-Y, where X and Y are the corresponding elements of List1 and List2.
%
% e.g., zip([a,b,c],[d,e,f],[a-d, b-e, c-f]).
%
% Note: while zip/3 is only true when all three lists are the same length, you should not
% need to check this explicitly: it should be a consequence of your definition.
%
% zip/3 can be defined in two rules, using no other relations.
%
% If you have defined zip/3 well, it will produce infinitely many answers if all three
% arguments are uninstantiated.

zip([],[],[]).
zip([X|Xs],[Y|Ys],[X-Y|Zs]) :-zip(Xs,Ys,Zs).

% sorted(++List) is deterministic
%
% sorted/1 holds if its argument is a list of integers in nondecreasing order.
%
% sorted([]).
% sorted([1,2,5,18]).
% sorted([2,18,18,18,2000]).
%
% Note: Because you are using CLP(FD), sorted/1 should produce a single answer even if the
% list contains uninstantiated variables, and should produce infinitely many lists if
% the list is partially uninstantiated.

sorted([]).
sorted([_]).
sorted([X,Y|Z]):-X#=<Y,sorted([Y|Z]).




% symmetric(+Tree) is deterministic
%
% symmetric/1 holds if its argument is a symmetric binary tree.
%
% symmetric(tip)
% symmetric(bin(bin(tip,1,bin(tip,2,tip)),3,bin(bin(tip,2,tip),1,tip)))
%
% symmetric/1 must work properly if the tree contains variables.
%
% ?- symmetric(bin(bin(tip,A,bin(tip,B,tip)),C,bin(bin(tip,D,tip),E,tip))).
% A = E,
% B = D.
%
% Note: symmetric/1 should produce infinitely many symmetric trees if its argument is
% uninstantiated, but does not need to produce every such tree.

symmetric(tip).
symmetric(bin(L,_,R)):-symmetric2(L,R).

symmetric2(tip,tip).
symmetric2(bin(La,A,Ra),bin(Lb,B,Rb)):-A=B,symmetric2(La,Rb),symmetric2(Ra,Lb).


% insert_tree(+X, ++InitialBST, -FinalBST) is deterministic
%
% insert_tree/3 is a relation between an integer and two binary search trees containing
% integers. It holds when FinalBST is the result of inserting X into InitialBST. (If X
% is present in InitialBST, then FinalBST = InitialBST.)
%
% insert_tree/3 may assume that InitialBST is a valid binary search tree.
%
% ?- insert_tree(3, tip, T).
% T = bin(tip,3,tip) ;
% false.
%
% ?- insert_tree(3, bin(tip,3,tip), T).
% T = bin(tip, 3, tip) ;
% false.
%
% ?- insert_tree(3, bin(tip,4,tip), T).
% T = bin(bin(tip, 3, tip), 4, tip) ;
% false.
%
% ?- insert_tree(3, bin(bin(tip, 2, tip), 4, tip), T).
% T = bin(bin(tip, 2, bin(tip, 3, tip)), 4, tip) ;
% false.
%
% If you have defined insert_tree/3 well, it will work in reverse. E.g.,
%
% ?- insert_tree(3, T, bin(tip,3,tip)).
% T = tip ;
% T = bin(tip, 3, tip) ;
% false.

insert_tree(A,tip,bin(tip,A,tip)).
insert_tree(A,bin(L,A,R),bin(L,A,R)).
insert_tree(A,bin(L,B,R),bin(L2,B,R)) :- A<B, insert_tree(A,L,L2).
insert_tree(A,bin(L,B,R),bin(L,B,R2)) :- A>B, insert_tree(A,R,R2).



% %%%%%%%%%%%%
% Extra Credit
% %%%%%%%%%%%%

% route(+Source, +Destination, -Route)
%
% route/3 is a relation that holds between two locations and a route between them. A route
% is a list of triples of the form Src-Type-Dst, where connect(Src, Dst, Type) holds,
% where the destination of each step is the source of the next, and no location is visited
% more than once.
%
% ?- route(manhattan, bronx, R).
% R = [manhattan-bridge-bronx] ;
% R = [manhattan-bridge-brooklyn, brooklyn-road-queens, queens-bridge-bronx] ;
% R = [manhattan-ferry-staten_island, staten_island-bridge-brooklyn, brooklyn-road-queens, queens-bridge-bronx] ;
% R = [manhattan-bridge-queens, queens-bridge-bronx] ;
% false.

% route(A,B,[A-C-B]):- connect(A,B,C).
% route(A,B,R):- connect(A,C,D), route(C,B,Rs), R=[A-D-C|Rs], nonmember(A-D-C,R).
route(A,B,R):-route(A,B,[],R).
route(A,B,_,[A-C-B]):- connect(A,B,C).
route(A,B,V,R):- connect(A,D,C),D\=B,nonmember(D,[A|V]),route(D,B,[D,A|V],Rs),R=[A-C-D|Rs].

% connect/3 is a relation between two locations and a connection type. It is symmetric,
% in that connect(S,D,T) holds if and only if connect(D,S,T) holds.

connect(Src, Dst, Type) :- connect_(Src, Dst, Type); connect_(Dst, Src, Type).

% connect_/3 is the underlying relation used by connect/3. Unlike connect/3, it is not
% symmetric.
%
% Feel free to add additional locations and connections!

connect_(manhattan, bronx, bridge).
connect_(manhattan, brooklyn, bridge).
connect_(manhattan, staten_island, ferry).
connect_(manhattan, queens, bridge).
connect_(staten_island, brooklyn, bridge).
connect_(brooklyn, queens, road).
connect_(queens, bronx, bridge).

% nonmember/2 is a relation that holds between an element and a list that does not contain
% the element.

nonmember(_, []).
nonmember(X, [Y|Ys]) :- dif(X,Y), nonmember(X,Ys).
