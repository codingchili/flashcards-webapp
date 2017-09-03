# flashcards-webapp [![Build Status](https://travis-ci.org/codingchili/flashcards-webapp.svg?branch=master)](https://travis-ci.org/codingchili/flashcards-webapp)

An open source web application to help with managing flashcards. Built as microservices using [chili-core](https://github.com/codingchili/chili-core).

## Build & run

Build the jar

``` gradle jar ``` 

Start the app

``` java -jar <filename>.jar ``` 

## Features
Planned features
- group flashcards into categories
- share categories with friends
- make a category public
- keep track on users scores per category
- allow media content on cards
- select answers to cards from a predefined list
- hotkeys:
  - Answer, Skip, Delete, Flip, Edit

## Libraries & Frameworks
This project makes use of some awesome libraries, including
- [vertx](https://github.com/eclipse/vert.x)
- [hazelcast](https://github.com/hazelcast/hazelcast)
- [elasticsearch](https://github.com/elastic/elasticsearch)
- [cqengine](https://github.com/npgall/cqengine)
- [mongodb](https://github.com/mongodb/mongo)
- [polymer](https://github.com/Polymer/polymer)

It is also built using the [chili-core](https://github.com/codingchili/chili-core) framework!

## Contributing
1. Submit a PR
2. Get it reviewed and tested
3. Deployed to live servers! (TBD)
