# Middleware broker application
[![Build Status](https://travis-ci.org/POPBL-6/broker.svg?branch=master)](https://travis-ci.org/POPBL-6/broker)
[![Coverity Status](https://scan.coverity.com/projects/8889/badge.svg)](https://scan.coverity.com/projects/popbl-6-broker)
Application that serves as our central server for managing all the messages coming from our middleware implementations.

## How to run it?
### If you have Gradle installed
Just run the following command in the repository folder
```sh
$ gradle run
```
The broker will start listening in port 5434 by default

### If you don't have gradle installed
We have included a gradle wrapper also, just execute the next command to run it
```sh
$ ./gradlew run
```
 
