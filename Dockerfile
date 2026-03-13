# Henter et færdigt java 25 miljø fra dockerHub
# Slim betyder en minimal version af linux med kun de nødvendige, så imaget bliver så lille så muligt
FROM  eclipse-temurin:21-jdk

#Opretter og sætter arbejdsmappen til at hedde "/app" inde i en container
#Alle kommandoer efter, kører i denne mappe
WORKDIR /app

#Kopierer din JAR file fra din computers /target mappe
#Inde i container omdøber den til app.jar
COPY target/KinoXP-0.0.1-SNAPSHOT.jar app.jar

#Appen bruger 8080
# Åbner ikke porten automatisk, sker først i docker container
EXPOSE 8080


#Det er kommandoen man skriver i terminalen, som kører når containeren starter
ENTRYPOINT ["java", "-jar", "app.jar"]