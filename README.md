# Purpose
This application is a sample example project build by Tobias Poll for Intergamma.

# Requirements
## Basis opdracht
- Intergamma heeft meerder bouwmarkten, maak een Kotlin of Java applicatie voor het
bijhouden van de voorraad van een uniek artikel per winkel.
- De applicatie moet een CRUD API bevatten voor het opslaan, opvragen, wijzigen en
verwijderen van de voorraad van een artikel.
- Het moet mogelijk zijn de voorraad van een artikel voor een periode van maximaal 5
minuten te reserveren, daarna vervalt de reservering automatisch.
- Gebruik het Spring Boot-, Gradle- en een opslagmedium naar keuze. Alternatieven voor
frameworks zijn toegestaan indien je geen ervaring met de genoemde frameworks hebt.
## Extra functionaliteiten (optioneel)
- Test coverage
- Grafische interface
- API documentatie
- Autorisatie op CRUD operaties: opslaan, wijzigen en verwijderen
- Een andere interessante toevoeging...?
## Verwachte resultaat
- Op tijd aanleveren van de applicatie middels een zip bestand
- Een werkende webapplicatie
- Een commando waarmee de applicatie kan worden gebouwd en getest
- Een commando waarmee de applicatie kan worden gestart
- Een korte beschrijving in een README.md

# Model
This application has the following model:
- Shop: A simple representation of a physical store. 
- Product: A simple description of a product.
- Item: A specific product in a specific store


# Building the application
The application can be tested and build through gradle using:
`./gradlew jar` on Linux/Mac
or
`gradlew.bat jar` on Windows

# Running the application
The application can be started using
`./gradlew bootRun`
or
`gradlew.bat bootRun`

# Swagger UI
To use the REST API through a browser after the application has started, go to:
[SwaggerUI](http://localhost:8080/swagger-ui/index.html#/)

# Extras
To approach a fully developed application, the following is added:
- An example of a cucumber feature test in [rest_items.feature](src/test/resources/features/rest_items.feature)
- Exposed prometheus metrics through the [Spring Actuator](http://localhost:8080/actuator/prometheus)
- Logging through kotlin-logging
- Example DockerFile, pipeline yaml and kubernetes deployment