movies <- read.csv('Movies2022F-4.csv')

barplot(tapply(movies$imdb_score, movies$country,mean),ylab="mean imdb score",
        main="mean imdb score for each country",border="black",las=2)
tapply(movies$imdb_score,movies$content,mean)
tapply(movies$imdb_score,movies$Gross,mean)
tapply(movies$imdb_score,movies$Budget,mean)
tapply(movies$imdb_score,movies$genre,mean)

#part A
#Hypothesis test 1
A <- movies[movies$Budget == 'Low' & movies$country =='Kyrgyzstan',]
B <- movies[movies$Budget == 'Medium' & movies$country == 'Romania',]
Cat1 <- rep("GroupA",nrow(A))
Cat2 <- rep("GroupB",nrow(B))
Cat <- c(Cat1,Cat2)
Val <- c(A[,c("imdb_score")],B[,c("imdb_score")])

d <- data.frame(Cat,Val)
boxplot(Val~Cat,data=d)
PermutationTestSecond::Permutation(d,"Cat","Val",1000,"GroupA","GroupB")

#Hypothesis test 2
A <- movies[movies$content=="PG-13" & movies$genre=='Family',]
B <- movies[movies$content=='R' & movies$genre=="History",]
Cat1 <- rep("GroupA",nrow(A))
Cat2 <- rep("GroupB",nrow(B))
Cat <- c(Cat1,Cat2)
Val <- c(A[,c("imdb_score")],B[,c("imdb_score")])

d <- data.frame(Cat,Val)
boxplot(Val~Cat,data=d)
PermutationTestSecond::Permutation(d,"Cat","Val",1000,"GroupA","GroupB")

#Hypothesis test 3
A <- movies[movies$content == "G" & movies$genre=='Sci-Fi',]
B <- movies[movies$content == 'R' & movies$genre == 'Family',]
Cat1 <- rep("GroupA",nrow(A))
Cat2 <- rep("GroupB",nrow(B))
Cat <- c(Cat1,Cat2)
Val <- c(A[,c("imdb_score")],B[,c("imdb_score")])

d <- data.frame(Cat,Val)
boxplot(Val~Cat,data=d)
PermutationTestSecond::Permutation(d,"Cat","Val",1000,"GroupA","GroupB")

#Part B
#Hypothesis test 4
A <- movies[movies$Budget == "Low" & movies$genre=='Comedy',]
B <- movies[movies$Budget == 'High' & movies$genre == 'Drama',]
Cat1 <- rep("GroupA",nrow(A))
Cat2 <- rep("GroupB",nrow(B))
Cat <- c(Cat1,Cat2)
Val <- c(A[,c("imdb_score")],B[,c("imdb_score")])

d <- data.frame(Cat,Val)
PermutationTestSecond::Permutation(d,"Cat","Val",1000,"GroupA","GroupB")

#Hypothesis test 5
A <- movies[movies$content == "PG-13" & movies$genre=='Drama',]
B <- movies[movies$content == 'PG' & movies$genre == 'Sci-Fi',]
Cat1 <- rep("GroupA",nrow(A))
Cat2 <- rep("GroupB",nrow(B))
Cat <- c(Cat1,Cat2)
Val <- c(A[,c("imdb_score")],B[,c("imdb_score")])

d <- data.frame(Cat,Val)
PermutationTestSecond::Permutation(d,"Cat","Val",1000,"GroupA","GroupB")


