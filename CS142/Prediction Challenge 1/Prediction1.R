hired <- read.csv('HireTrainApr10.csv',stringsAsFactors = T)
v <- sample(1:nrow(hired))
hiredScrambled <- hired[v,]

train <- hiredScrambled[1:1900,]
valid <- hiredScrambled[(nrow(hiredScrambled)-99):nrow(hiredScrambled),]

#Get to know your data
colnames(train)
summary(train)
unique(train$Coding)
unique(train$Impression)
unique(train$Major)
unique(train$College)
table(train$Coding,train$Hired)
table(train$Impression,train$Hired)
table(train$Major,train$Hired)
table(train$College,train$Hired)

#Odds of being hired when you are from BestCollege
Prior<-nrow(hired[hired$College=='BestCollege',])/nrow(hired)
Prior
PriorOdds<-round(Prior/(1-Prior),2)
PriorOdds
TruePositive<-round(nrow(hired[hired$Hired=='Yes' & hired$College=='BestCollege',])/nrow(hired[hired$Hired=='Yes',]),2)
TruePositive
FalsePositive<-round(nrow(hired[hired$Hired!='Yes' & hired$College=='BestCollege',])/nrow(hired[hired$Hired!='Yes',]),2)
FalsePositive
LikelihoodRatio<-round(TruePositive/FalsePositive,2)
LikelihoodRatio
PosteriorOdds <-LikelihoodRatio * PriorOdds
PosteriorOdds
Posterior <-PosteriorOdds/(1+PosteriorOdds)
Posterior


#chisq test
chisq.test(train$Coding,train$Hired)
chisq.test(train$Impression,train$Hired)
chisq.test(train$Major,train$Hired)
chisq.test(train$College,train$Hired)


#Mosaic plots to view
#coding
excellentCoding <- train[train$Coding== 'Excellent',]
mosaicplot(excellentCoding$Impression~excellentCoding$Hired,xlab='Impression',ylab='hired',main='Excellent Impression Distribution')
mosaicplot(excellentCoding$Major~excellentCoding$Hired,xlab='Major',ylab='hired',main='Excellent Major Distribution')
mosaicplot(excellentCoding$College~excellentCoding$Hired,xlab='College',ylab='hired',main='Excellent College distribution')

okCoding <- train[train$Coding== 'OK',]
mosaicplot(okCoding$Impression~okCoding$Hired,xlab='Impression',ylab='hired',main='OK Impression Distribution')
mosaicplot(okCoding$Major~okCoding$Hired,xlab='Major',ylab='hired',main='OK Major Distribution')
mosaicplot(okCoding$College~okCoding$Hired,xlab='College',ylab='hired',main='OK College Distribution')

weakCoding <- train[train$Coding=='Weak',]
mosaicplot(weakCoding$Impression~weakCoding$Hired,xlab='Impression',ylab='hired',main='Weak Impression Distribution')
mosaicplot(weakCoding$Major~weakCoding$Hired,xlab='Major',ylab='hired', main='Weak Major Distribution')
mosaicplot(weakCoding$College~weakCoding$Hired,xlab='College',ylab='hired', main='Weak College Distribution')

temp <- excellentCoding[ excellentCoding$Hired=='No',]
summary(temp)
isbyu <- excellentCoding[excellentCoding$College=='BYU' & excellentCoding$Major=='CS',]
table(isbyu$Hired)
hypothesis <- train[train$College=='BestCollege' & train$Impression=='Nerdy' & train$Major=='IT',]
table(hypothesis$Hired)


temp7 <- train[train$Coding=='OK' & train$College=='BestCollege',]
mosaicplot(temp7$Impression~temp7$Hired,xlab='Impression',ylab='hired')

temp <- weakCoding[weakCoding$College=='Redbrick'& weakCoding$Hired=='Yes',]
summary(temp)
temp <- weakCoding[weakCoding$Impression=='Nerdy' & weakCoding$Hired=='Yes',]
summary(temp)

temp <- train[train$College=='Redbrick' & train$Impression=='Nerdy' & train$Coding=='Weak',]
table(temp$Hired)

decision <- rep('Yes',nrow(valid))
decision[valid$Coding=='Weak'] <- 'No'
decision[valid$Coding=='Weak' & valid$Impression=='Nerdy' & valid$College=='Redbrick'] <- 'Yes'
decision[valid$Major == 'IT' & valid$College=='BestCollege' & valid$Impression=='Nerdy'] <- 'No'
decision[valid$Coding=='OK' & valid$College=='BestCollege' & valid$Impression=='Nerdy'] <- 'No'
decision[valid$Coding=='OK' & valid$College=='BestCollege' & valid$Impression=='Shy'] <- 'No'
error <- mean(valid$Hired != decision)
error

test <- read.csv('test_challenge1.csv')
submission <- read.csv('sample_submission_challenge1.csv')

decision <- rep('Yes',nrow(test))
decision[test$Coding=='Weak'] <- 'No'
decision[test$Coding=='Weak' & test$Impression=='Nerdy' & test$College=='Redbrick'] <- 'Yes'
decision[test$Major == 'IT' & test$College=='BestCollege' & test$Impression=='Nerdy'] <- 'No'
decision[test$Coding=='OK' & test$College=='BestCollege' & test$Impression=='Nerdy'] <- 'No'
decision[test$Coding=='OK' & test$College=='BestCollege' & test$Impression=='Shy'] <- 'No'
table(decision)
submission$Prediction <- decision

write.csv(submission,'submission2.csv',row.names= FALSE)
