# Task manager
### Hexlet tests and linter status:
[![Actions Status](https://github.com/vaspav98/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/vaspav98/java-project-99/actions)
[![Java CI](https://github.com/vaspav98/java-project-99/actions/workflows/my-check.yml/badge.svg)](https://github.com/vaspav98/java-project-99/actions/workflows/my-check.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/b8312d4965235b894f6d/maintainability)](https://codeclimate.com/github/vaspav98/java-project-99/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/b8312d4965235b894f6d/test_coverage)](https://codeclimate.com/github/vaspav98/java-project-99/test_coverage)

## Description
This application is a task management system. It allows you to set tasks, assign performers and change their statuses.
To work with the system, registration and authentication are required.

## Used technologies
* Spring Boot
* Spring Security
* Spring Data JPA
* H2 & PostgreSQL
* JUnit, AssertJ & MockWebServer
* MapStruct
* Lombok
* Docker (for deploy)
* PaaS Render
* Sentry
* Swagger
* And others

## Requirements
* JDK 20
* Gradle 8.4

## Usage
```bash
make install-dist
make start-dist
# Open http://localhost:8080
#Username: hexlet@example.com
#Password: qwerty
```

### Link on working app:
[![Render.com](https://render.com/images/render-banner.png)](https://java-project-99.onrender.com/welcome)