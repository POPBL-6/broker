# Middleware broker application

[![Build Status](https://travis-ci.org/POPBL-6/broker.svg?branch=master)](https://travis-ci.org/POPBL-6/broker)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4ddb41b8f7a24935b5ecc79ecef66d09)](https://www.codacy.com/app/POPBL6/broker?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=POPBL-6/broker&amp;utm_campaign=Badge_Grade)

[JavaDocs](http://popbl-6.github.io/broker/)

Application that serves as our central server for managing all the messages coming from our middleware implementations.

## What this is
This application is neccesary for the clients using <a href=https://github.com/POPBL-6/middleware>Middleware</a>
to communicate with other peers.

## How to use it
See the <a href=https://github.com/POPBL-6/broker/wiki/2-.-How-to-use-the-broker>wiki</a>.

## How to run it
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
 
