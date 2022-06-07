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

## How to run

### Requirements

To run this software, it is required a system installation of maven along with docker and docker-compose with
version at least 2.1 (to support docker healthcheck)

### Steps

1) Allow to display the GUI through docker container. To accomplish, prompt in any shell the following command
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