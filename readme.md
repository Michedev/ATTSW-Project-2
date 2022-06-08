# Task-Manager-v2
![Build](https://img.shields.io/github/workflow/status/Michedev/ATTSW-Project-2/Maven%20test%20and%20package)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Michedev_ATTSW-Project-2&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Michedev_ATTSW-Project-2)
# Introduction

## Application features

The application is a task manager i.e. an application that  allows the user to keep track of its own tasks. Specifically, the tasks handled by this application consists in a title and three steps to reach its conclusion; for instance title "Shopping" and subtasks {"Bread", "Milk", "Water"}.
Furthermore, the application tasks are bounded to an user, in fact the application features a registration and login pages. In conclusion, a summary of the application capabilities:

- Create new user
- Login user
- Add new task
- Update a task
- Delete logged user
- Delete a task

## Techin

## Application screenshots


![Screenshot from 2022-06-08 10-33-02](https://user-images.githubusercontent.com/12683228/172571530-a9dd460a-2384-4984-aa1e-207d22a5a0af.png)
![Screenshot from 2022-06-08 10-33-16](https://user-images.githubusercontent.com/12683228/172571535-f67854af-882f-4696-bf49-031b5b9a04c8.png)
![Screenshot from 2022-06-08 10-33-52](https://user-images.githubusercontent.com/12683228/172571537-45d0f687-c0a7-4d02-8625-a1377c53d4a6.png)
![Screenshot from 2022-06-08 10-33-58](https://user-images.githubusercontent.com/12683228/172571538-db2523bb-7afb-47d4-90d5-c0978af2e6e7.png)
![Screenshot from 2022-06-08 10-34-23](https://user-images.githubusercontent.com/12683228/172571543-393630c5-c693-426e-bfb0-1d6ebed05850.png)

## How to run

There are three ways to run this software:

- package the fat jar, then run it with an InMemory Database
- package the fat jar, then run it with a PostgreSQL Database
- Run both the application and the Database in two separate docker containers

### Run with an InMemory Database

1) Package the fat jar by running in the project folder

```bash
mvn package [-DskipTests]
```

2) Run the jar with the following command

```bash
java -jar target/task-manager-v2-0.0.1-SNAPSHOT-jar-with-dependencies.jar --dbtype=INMEMORY
```

### Run with a PostgreSQL Database


1) Package the fat jar by running in the project folder

```bash
mvn package [-DskipTests]
```

2) Run the jar with the following command

```bash
java -jar target/task-manager-v2-0.0.1-SNAPSHOT-jar-with-dependencies.jar --dbtype=POSTGRESSQL --address=ADDRESS --port=PORT
```

### Run in two separate Docker containers


#### Requirements

It is required docker and docker-compose with version at least 2.1 (to support docker healthcheck)

#### Steps

1) Allow to display the GUI through the docker container with the following command
```bash
xhost +local:root
```
2) Then to build the jar file prompt

```bash
mvn package [-DskipTests]
```

3) To start the application prompt

```bash
[sudo] docker compose up
```

4) Once the application is stopped, to revert the permission to execute GUI applications through docker container prompt

```bash
xhost -local:root
```
