import Data.List

data Poly a = P [a] deriving (Show, Eq)

degree :: Poly a -> Int
degree (P []) = 0
degree (P (x:[])) = 0
degree (P (x:xs)) = 1 + degree(P (xs))


scale :: (Num a, Eq a) => a -> Poly a -> Poly a
scale 0 _ = P []
scale a (P xs) = P ys
    where
        ys = map (a*) xs



($$) :: (Num a, Eq a) => Poly a -> a -> a
P [] $$ _ = 0
P (x:xs) $$ a = x + a * P xs $$ a




helper :: Num a => [a] -> [a] -> [a]
helper (xs) [] = xs
helper [] ys = ys
helper (x:xs) (y:ys) = x+y : helper xs ys

nozero :: (Eq a, Num a) => [a] -> [a]
nozero [] = []
nozero xs = reverse (dropWhile (==0) (reverse xs))

addPoly :: (Num a, Eq a) => Poly a -> Poly a -> Poly a
addPoly (P xs) (P ys) = P (nozero(helper xs ys))    




multPoly :: (Num a, Eq a) => Poly a -> Poly a -> Poly a
multPoly _ (P []) = P []
multPoly (P []) _ = P []
multPoly (P xs) (P (y:ys)) = addPoly (P (map (y*) xs)) (P (0:z))   
    where P z = multPoly (P xs) (P ys)


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

y :: (Num a) => Poly (Poly a)
y = P [P [0,1]]