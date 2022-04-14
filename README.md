# Scala Web Scawler using Play

The project is build on Play framework to utilize the efficiency in creating API's and having smooth plug and play
access to futures and concurrent toolkit Akka Actors.

This is not the only usage of Play but for our current use case:
microservice, concurrency, scalable, extensible and readable, play was the safest bet.

## Appendix

### Running

You need to download and install sbt for this application to run.

Once you have sbt installed, the following at the command prompt will start up Play in development mode:

```bash
sbt run
```

Play will start up on the HTTP port at <http://localhost:9000/>. You don't need to deploy or reload anything -- changing
any source code while the server is running will automatically recompile and hot-reload the application on the next HTTP
request.

### Usage

Current Possible Routes

```routes
GET     / 
POST    /api/crawl
```

GET     / ----------- <http://localhost:9000/> is a mock API with a response "Welcome to Scala Web Crawler"

POST    /api/crawl ----------- <http://localhost:9000/api/crawl> has the below request/response format:
Request

```Request
{
"urls": ["https://google.com", "https://github.com", "http://www.t.com"]
}
```

Response

```Response
{
"result": [
{
"url"   : "webpage_url",
"title" : "webpage_title,
"body"  : "webpage_body"
}
]
"error":[
{
"url"       : "webpage_url",
"message"   : "error_message"
}
]
```

## External Library Used

### Scala Scraper [![Build Status](https://travis-ci.org/ruippeixotog/scala-scraper.svg?branch=master)](https://travis-ci.org/ruippeixotog/scala-scraper) [![Coverage Status](https://coveralls.io/repos/github/ruippeixotog/scala-scraper/badge.svg?branch=master)](https://coveralls.io/github/ruippeixotog/scala-scraper?branch=master) [![Maven Central](https://img.shields.io/maven-central/v/net.ruippeixotog/scala-scraper_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/net.ruippeixotog/scala-scraper_2.12) [![Join the chat at https://gitter.im/ruippeixotog/scala-scraper](https://badges.gitter.im/ruippeixotog/scala-scraper.svg)](https://gitter.im/ruippeixotog/scala-scraper)

A library providing a DSL for loading and extracting content from HTML pages.

Take a look at [Examples.scala](core/src/test/scala/net/ruippeixotog/scalascraper/Examples.scala) and at
the [unit specs](core/src/test/scala/net/ruippeixotog/scalascraper) for usage examples.

```scala
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.1"
```

### Application Flow

Request => Routes => Controller => Service => Sends async message to Akka Actor

Akka Actor replies back with message (5 second timeout) => Service => Controller => Response

### How it works

We make parallel calls to Akka Actor using message passing, with each request having a timeout of 5 seconds. When an
Akka Actor receives the message, it enqueues the message inside its mailbox, the messages are then processed by the
message handler concurrently.

With increase in load, the number of actors spawned can be increased, also better optimising can be adding using routing
algorithms and supervision strategies whenever the system needs to be scaled.

The web crawling is handled using scalascraper, which utilizes a browser implementation based on jsoup, for parsing html
pages. The browser object is then used to make a web client and fetch the entire web page using the browser
implementation, parses the body and create a document, which can then be utilized to access parts of the web page.

> **_NOTE: I have made a few additional changes_**

> The result response will have 3 fields with url, title and body, to have a better understanding of the content,
> the model can also be extended to add/remove html elements needed to be crawled.
>
> The error response will have 2 fields with url and message, to have a better understanding of the error 
> for each url.
> 
> Though Akka actors are concurrent in nature, I have made calls to Akka actors parallelly, this can be
> easily changed to sequential if required.
>
> Play comes with in-build Caching libraries e.g cacheApi, eHcache, etc (in-memory caching), I have added cacheApi
> for now which can be injected and used if required.
>
> Also Play allows easy means to plug and play distributed caching like redis, etc as well.
>
> For logging Play uses SLF4J for logging, backed by Logback as its default logging engine. (Please refer
> to conf -> logback.xml)
> 
> Scala native packager is added, in case we want to create a project jar file for deployment purpose.


### Improvements to be added

1. Integration tests.
2. Dockerization.
3. Configuration for different environments.
4. Akka Actor configurations.