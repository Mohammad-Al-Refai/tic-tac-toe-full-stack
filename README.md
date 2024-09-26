# Tic Tac Toe ✖️⭕

[demo](https://github.com/user-attachments/assets/82a60580-8c07-47bd-a76a-fdfd5e4ef3c5)

## Backend

### Tech Stack

* Language:
  * [Kotlin](https://kotlinlang.org/)
* Build Tools:
  * [Gradle](https://gradle.org)
* Database:
  * [PostgreSQL](https://www.postgresql.org)
  * [Postgres R2DBC Driver](https://mvnrepository.com/artifact/org.postgresql/r2dbc-postgresql)
* Framework:
  * [Spring 6.x/Spring Boot 3.x](https://spring.io)
  * [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)


### Initialize database

* Create database with name `tic-tac-toe` then make sure it works on port 5432
- Set the PostgreSQL user name in `spring.r2dbc.username` and the password in `spring.r2dbc.password` in application.properties in the resource folder

### Run project

```bash
./gradlew bootRun
```

### Build

#### Docker

```bash
docker build -t tic-tac-toe . & docker-compose up
```

#### Gradle

```bash
./gradlew build
```

---



## Frontend

### Tech Stack

* Language:
  * TypeScript
* Build Tools:
  * Vite
* Framework:
  * React

### Install dependencies

```bash
yarn install
```

### Run project

Make sure to change the URL in `frontend\src\game\gameViewModel.ts`

```
const WS = "ws://{your_device_ip}:8080/ws";
```

Then Run:

```bash
yarn dev --host
```

### Build

```bash
yarn build
```
