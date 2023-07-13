#setup
moody <- read.csv("moodyJanuary31b.csv")
head(moody)
table(moody$Grade)
table(moody$Texting)
table(moody$Asking)
table(moody$Dozing)

barplot(table(moody$Grade), main='Frequency of Grades', xlab='Grade', ylab='Frequency')
boxplot(moody$Score~moody$Grade, main='Distribution of Scores by Grade', xlab='Grade', ylab='Score')

boxplot(moody$GPA~moody$Grade, main='Distribution of GPA by Grade',xlab='Grade',ylab='GPA')


#texting analyze
always_texting <- moody[moody$Texting == 'Always',]
barplot(table(always_texting$Grade),main="Frequency of Grades of Students who Always Text",xlab='Grade',ylab='Frequency')
often_texting <- moody[moody$Texting == 'Often',]
barplot(table(often_texting$Grade),main="Frequency of Grades of Students who Often Text",xlab='Grade',ylab='Frequency')
sometimes_texting <- moody[moody$Texting == 'Sometimes',]
barplot(table(sometimes_texting$Grade),main="Frequency of Grades of Students who Text Sometimes",xlab='Grade',ylab='Frequency')
never_texting <- moody[moody$Texting == 'Never',]
barplot(table(never_texting$Grade),main="Frequency of Grades of Students who Never Text",xlab='Grade',ylab='Frequency')

texting <- tapply(moody$Score,moody$Texting,mean)
barplot(texting, main='Median Score for Texting', xlab='Texting', ylab='Median Score', col=colors)


# Asking analyze
never_asking <- moody[moody$Asking == 'Never',]
barplot(table(never_asking$Grade),main="Frequency of Grades of Students who Never Ask",xlab='Grade',ylab='Frequency')
often_asking <- moody[moody$Asking == 'Often',]
barplot(table(often_asking$Grade),main="Frequency of Grades of Students who Often Ask",xlab='Grade',ylab='Frequency')
sometimes_asking <- moody[moody$Asking == 'Sometimes',]
barplot(table(sometimes_asking$Grade),main="Frequency of Grades of Students who Ask Sometimes",xlab='Grade',ylab='Frequency')

asking <- tapply(moody$Score,moody$Asking,mean)
barplot(asking, main='Median Score for asking', xlab='Texting', ylab='Median Score', col=colors)

# Dozing analyze
always_dozing <- moody[moody$Dozing == 'Always',]
barplot(table(always_dozing$Grade),main="Frequency of Grades of Students who Always Doze",xlab='Grade',ylab='Frequency')
sometimes_dozing <- moody[moody$Dozing == 'Sometimes',]
barplot(table(sometimes_dozing$Grade),main="Frequency of Grades of Students who Doze Sometimes",xlab='Grade',ylab='Frequency')
never_dozing <- moody[moody$Dozing == 'Never',]
barplot(table(never_dozing$Grade),main="Frequency of Grades of Students who Never Doze",xlab='Grade',ylab='Frequency')
table(never_dozing$Grade)

dozing <-tapply(moody$Score,moody$Dozing,mean)
barplot(asking, main='Median Score for dozing', xlab='Texting', ylab='Median Score', col=colors)

