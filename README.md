# Captcha

Construida con Angular 13 y Spring 2.5.6, utilizando programacion funcional y Webflux/Reactor, he persistido los settings en un Mongo embedido en el servicio back y utilizado la libreria  mongodb-reactive para hacer las peticiones a la BD.

![alt text](https://github.com/GabrielLiz/captcha/blob/main/front-example.png?raw=true)

Swagger http://localhost:8080/swagger-ui.html definicion de la API
![alt text](https://github.com/GabrielLiz/captcha/blob/main/api-example.png?raw=true)
## Ejecucion

- Backend `mvn clean install` ->  /target `java -jar backend-biocaptcha-0.0.1-SNAPSHOT.jar`
- Frontend `ng serve`

## Tecnologias
 * Java 11
 * Typescript
 * Spring
    - WebFlux/reactor (Rxjava para spring)
    - MongoDB
    - OpenApi
    - Swagger
    - Lombok
 * Angular 13
   - Material
   - bootstrap
   - Node
   
