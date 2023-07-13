install.packages("devtools") 
devtools::install_github("devanshagr/CrossValidation",force=TRUE)

library(rpart)
library(rpart.plot)
hired <- read.csv('HireRTrain1-1.csv')

boxplot(TwitterFOLLOWERS~Hired,data=hired)
boxplot(TwitterFOLLOWING~Hired,data=hired)
bp <- boxplot(TikTokFOLLOWERS~Hired,data=hired,plot=FALSE)
boxplot(TikTokTFOLLOWING~Hired,data=hired)
boxplot(new~Hired,data=hired)
mosaicplot(hired$College~hired$Hired,xlab='College',ylab='hired')
hired$new <- 0
hired$new <- hired$TwitterFOLLOWERS-hired$TwitterFOLLOWING+hired$TikTokFOLLOWERS-hired$TikTokTFOLLOWING

tree <- rpart(Hired~Coding+Impression+Major+College+TwitterFOLLOWERS+TwitterFOLLOWING+TikTokFOLLOWERS+TikTokTFOLLOWING+new,data = hired, method= 'class',control = rpart.control(minsplit=30,cp=0.005))
rpart.plot(tree)
pred <- predict(tree, hired, type='class')


mean(hired$Hired==pred)
CrossValidation::cross_validate(hired,tree,20,0.7)

test <- read.csv('test_challenge2.csv')
test$new <- 0
test$new <- test$TwitterFOLLOWERS-test$TwitterFOLLOWING+test$TikTokFOLLOWERS-test$TikTokTFOLLOWING
prediction <- predict(tree,test,type='class')
submission <-read.csv('sample_submission2.csv')
submission$Prediction <- prediction
write.csv(submission,'submissionPart2.csv',row.names= FALSE)
