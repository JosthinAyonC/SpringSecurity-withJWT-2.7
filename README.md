# Spring Boot JWT Authentication example with Spring Security & Spring Data JPA, SpringV 2.7

Cosas a considerar al manipular la plantilla backend: 

1. Agregar rutas de acceso en WebSecurityConfig.java
2. Agregar en la db en inserto de roles, y en el archivo ERoles.java agregar el rol o la modifiacion.

## Run Spring Boot application
```
mvn spring-boot:run
```

## Run following SQL insert statements
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
## Info de como enviar el body
```
{   "username":"franklinmp69",
    "lastname":"franklin",
    "firstname": "Campoverde",
    "email":"franklin69@hotmail.com",
    "password":"lokoloko21",
    "status":"A",
    "role":["admin", "mod"]
}
```
## Info de firma y duracion de token
```
La informacion que se encuentra en application.properties es la firma y la duracion del token.
bezkoder.app.jwtSecret= bezKoderSecretKey
bezkoder.app.jwtExpirationMs= 86400000
```
