For this assignment, you will write three simple Haskell functions. For testing purposes, save your functions definitions in a file, load the file into ghci, and test its behavior by entering expressions that use the function. (Sample test sessions are shown below. Note that simply passing these cases is not sufficient; your functions must produce the correct answer for all possible inputs.)

 

    heads :: [a] -> Maybe a

heads is a safe version of head that never crashes. For an empty list, it returns Nothing. For a non-empty list starting with x, it returns Just x. (Do not use head in your definition.)
```
    GHCI> heads []
    Nothing
    GHCI> heads [1,2,3]
    Just 1
    GHCI> heads "abcdefg"
    Just 'a'

```

    final :: [a] -> Maybe a

final is a safe version of last that never crashes. For an empty list, it returns Nothing. For a non-empty list whose last value is x, it returns Just x. (Do not use last in your definition.)
```
    GHCI> final []
    Nothing
    GHCI> final [1,2,3]
    Just 3
    GHCI> final "abcdefg"
    Just 'g'
 
```
    data Tree a = Tip | Bin (Tree a) a (Tree a)

    sumTree :: Num a => Tree a -> a

sumTree returns the sum of all elements in a tree. (Note that the empty tree will sum to zero.)

    GHCI> sumTree Tip
    0
    GHCI> sumTree (Bin Tip 5 Tip)
    5
    GHCI> sumTree (Bin (Bin Tip 1 Tip) 2 (Bin Tip 3 Tip))
    6
