video <- read.csv('Video_Games_Sales_as_at_22_Dec_2016.csv')
video <- na.omit(video)
video$User_Score = as.numeric(video$User_Score)

table1 <- tapply(video$Critic_Score,video$Genre,mean)
barplot(table1, xlab="Genre", ylab="Critic Score(mean)",cex.names=.6,main="Barplot for critic score for each genre")
table2 <- tapply(video$User_Score,video$Genre,mean)
barplot(table2, xlab="Genre", ylab="User Score(mean)",cex.names=.5,main="Barplot for user score for each genre")
table3 <- tapply(video$Global_Sales,video$Genre,mean)
barplot(table1, xlab="Genre", ylab="Global sales",cex.names=.5,main="Barplot for global sales for each genre")


plot(video$Critic_Score,video$Global_Sales,ylab='Sales(in millions)',xlab='Critic Score',ylim=c(0,10))
abline(lm(Global_Sales~Critic_Score,data=video),col='red')
plot(video$User_Score,video$Global_Sales,ylab='Sales(in millions)',xlab='User Score',ylim=c(0,10))
abline(lm(Global_Sales~User_Score,data=video),col='red')
plot(video$User_Score,video$Critic_Score,ylab='Critic Score',xlab='User Score',ylim=c(0,100))
abline(lm(Critic_Score~User_Score,data=video),col='red')


video <- transform(video, critic_category = cut(video$Critic_Score, breaks = c(0,50,75,100), labels = c('Low','Medium','High')))
ZTest::z_test_from_data(video,'critic_category','Global_Sales','Medium','High')
video <- transform(video, user_category = cut(video$User_Score, breaks = c(0,5,7.5,10), labels = c('Low','Medium','High')))
ZTest::z_test_from_data(video,'user_category','Global_Sales','Medium','High')

