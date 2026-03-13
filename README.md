# KinoXP 🎬

KinoXP er et simpelt biografsystem lavet som et projekt på datamatikeruddannelsen. Formålet med projektet er at lave en webapplikation hvor brugere kan se film, oprette en bruger og booke billetter.

Systemet har også en admin-rolle, hvor man kan oprette og slette film samt administrere visninger.

## Funktioner

### Bruger

* Oprette en bruger
* Logge ind og ud
* Se aktuelle film
* Filtrere film efter genre, sprog, format og aldersgrænse
* Læse mere om en film
* Booke billetter

### Admin

* Oprette nye film
* Slette film
* Se alle film
* Administrere visninger

## Teknologier

Projektet er lavet med:

Backend

* Java
* Spring Boot
* Spring Data JPA
* REST API

Frontend

* HTML
* CSS
* JavaScript

Database

* MySQL / H2

Der bruges også **OMDb API** til at hente filmplakater.

## Installation

1. Clone repository:

git clone https://github.com/J-M-I-T-Tech/KinoXP.git

2. Åbn projektet i IntelliJ.

3. Start Spring Boot applikationen.

4. Åbn derefter i browseren:

http://localhost:8080

## Roller

Systemet har tre roller:

CUSTOMER – almindelig bruger
EMPLOYEE – medarbejder
ADMIN – administrator

Kun ADMIN kan oprette og slette film.

## Contributors

<a href="https://github.com/J-M-I-T-Tech/KinoXP/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=J-M-I-T-Tech/KinoXP" />
</a>

## Projekt

Projektet er lavet som en del af datamatikeruddannelsen.
