Edited: Corrected the third example for ($$).

Edited: Corrected x^2 - 2*x.

Edited: Corrected Num instance to use scale instead of scalePoly.

We will design a type representing polynomials. A polynomial is a function of some variable x written in the form . The values  are the coefficients of the polynomial.

We can represent a polynomial as a list of coefficients. For convenience, we will start with  followed by  and so forth. Two polynomials are equal if all their coefficients are equal. To simply things, we will require our lists to be finite and end with a non-zero coefficient.

Copy this data declaration to a file:

    data Poly a = P [a] deriving (Show, Eq)

Thus, P [2,1] represents the polynomial , P [-1,0,1] represents , P [0,0,0,2] represents , and so forth.

1. The degree of a polynomial is the largest exponent occurring in any of its terms. Write a function that returns the degree of a polynomial.

    degree :: Poly a -> Int

For example:
```
> degree (P [])
0
> degree (P [8])
0
> degree (P [2,1])
1
> degree (P [-1,0,2])
2
```
2. Write a function that multiplies a polynomial by a constant (or scalar).

    scale :: (Num a, Eq a) => a -> Poly a -> Poly a

For example:
```
> scale 2 (P [-1,0,1])
P [-2,0,2]
> scale 0 (P [0,0,2])
P []
```
3. Write an operator that computes a polynomial at some point. (That is, it substitutes a value for x.)

    ($$) :: (Num a, Eq a) => Poly a -> a -> a

For example:
```
> P [-1,0,1] $$ 3
8
> P [12] $$ 0
12
> P [0,0,0,1] $$ (-2)
-8
```
Your implementations of scale and $$ should have this property: scale n p $$ x == n * (p $$ x).

4. Write a function that adds two polynomials. Ensure that the polynomial produced matches the requirements.

    addPoly :: (Num a, Eq a) => Poly a -> Poly a -> Poly a

For example:
```
> addPoly (P [1]) (P [-1,1])
P [0,1]
> addPoly (P [17]) (P [0,0,0,1])
P [17,0,0,1]
>addPoly (P [1,-1]) (P [0,1])
P [1]
```
You may find it helpful to write addPoly using two helper functions: one to combine the lists of coefficients, and one to trim trailing zeroes.

Your implementation of addPoly should have this property: 

    addPoly p q $$ x == (p $$ x) + (q $$ x)

5. Write a function that multiplies two polynomials.

    multPoly :: (Num a, Eq a) => Poly a -> Poly a -> Poly a

For example:
```
> multPoly (P [-1,0,1]) (P [0,1])
P [0,-1,0,1]
> multPoly (P [-1,0,1]) (P [2])
P [-2,0,2]
> multPoly (P [1,1]) (P [-1,1])
P [-1,0,1]
> multPoly (P [18,0,3]) (P [])
P []
```
Before writing multPoly, you may find it helpful to write a function that increases the degree of a polynomial by multiplying it by x. E.g., mapping  to . Seeing how this function, scale, and addPoly are written should give you a sense of how to construct multPoly. As with addPoly, multPoly is easier to write as a combination of simpler functions.

Your implementation of multPoly should have this property: 
    multPoly p q $$ x == (p $$ x) * (q $$ x)

Appendix I
Once you have these functions written, copy the following code into your file:
```
instance (Num a, Eq a) => Num (Poly a) where
    (+) = addPoly
    negate = scale (-1)
    (*) = multPoly
    fromInteger 0 = P []
    fromInteger n = P [fromInteger n]
    abs = error "No abs for Poly"
    signum = error "No signum for Poly"

x :: (Num a) => Poly a
x = P [0,1]
```
Now try writing some simple expressions and see what you get. For example,

```
> x + 1
P [1,1]
> x^2 - 2*x
P [0,-2,1]
```
Could you explain to someone why this works?

Appendix II
Add the following definition to your file:
```
y :: (Num a) => Poly (Poly a)
y = P [P [0,1]]
```
Try entering expressions such as x^2 + y^2 and (x + 1) * (y - 1) into GHCi. Can you make sense of the results?
