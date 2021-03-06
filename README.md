# flashcards-webapp [![Build Status](https://travis-ci.org/codingchili/flashcards-webapp.svg?branch=master)](https://travis-ci.org/codingchili/flashcards-webapp)

An open source web application to help with managing flashcards. Built as microservices using [chili-core](https://github.com/codingchili/chili-core).

See the demo for v1.0.0 on [YouTube](https://www.youtube.com/watch?v=1LcJ4d6hzA8).
Or try it for yourself at [FlashCardsAlligator](https://flashcardsalligator.com/).

![category search](https://raw.githubusercontent.com/codingchili/flashcards-webapp/5dc830d03f2b5b339edbfbd550964f1002744ddb/category-search.png)

## Build & run

Build the flashcards jar

```console
gradlew jar
``` 

Start the app

```console
java -jar <filename>.jar
``` 

Package distribution
```console
gradlew archiveZip
```

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

## Updating certificates on the demo environment
```console
# force if certbot says certs are not due for renewal yet.
sudo certbot renew --force-renewal
sudo cp /etc/letsencrypt/live/flashcardsalligator.com/privkey.pem ./
sudo cp /etc/letsencrypt/live/flashcardsalligator.com/fullchain.pem ./

# remember to set a password for the intermediate keystore, keytool bugs out otherwise.
openssl pkcs12 -export -out keystore.pkcs12 -in fullchain.pem -inkey privkey.pem

# genereate the java keystore
keytool -importkeystore -srckeystore keystore.pkcs12 -srcstoretype PKCS12 -destkeystore keystore2.jks
```

## Contributing
1. Submit a PR!
2. Get it reviewed and tested
3. Deployed to live servers! (TBD)

[![donate](https://img.shields.io/badge/donate-%CE%9ETH%20/%20%C9%83TC-ff00cc.svg?style=flat&logo=ethereum)](https://commerce.coinbase.com/checkout/673e693e-be6d-4583-9791-611da87861e3)
