package service

import actor.CrawlerActor
import actor.CrawlerActor.CrawlWebPage
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import model.Constants
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

/*
  Page Service contains all services related to calling, crawling, formatting a web page.
 */

class PageService @Inject()(actorSystem: ActorSystem)(implicit exec: ExecutionContext) {

  private val crawlerActor = actorSystem.actorOf(CrawlerActor.props, CrawlerActor.actorName)
  implicit val timeout: Timeout = Timeout(5.seconds)

  /*
  Asynchronous function used to call akka actor to send message `CrawlWebPage`
  which crawl a web page and either return the page content or if error message in case of exceptions.

  The function uses Future.traverse which calls the Akka actor in parallel.

  Params    urls - List of web page urls to be crawled.
  Returns   Future[JsObject] - Future JsObject result of crawled data and errors if any.
   */

  def getPage(urls: List[String]): Future[JsObject] = {
    Future.traverse(urls)(url => (crawlerActor ? CrawlWebPage(url)).mapTo[JsValue]).map(transform)
  }


  /*
  Synchronous function used to transform unformatted jsValue, format it based on result and error
  and return a single JsObject containing both.

  Params    responses - List of web page crawled data in unformatted Json format
  Returns   JsObject -  JsObject result of crawled data formatted with keys result and error.
 */

  private def transform(responses: List[JsValue]): JsObject = {
    val (result, error) = responses.foldLeft((JsArray(), JsArray())) { case ((res: JsArray, err: JsArray), current) =>
      val result = current.\(Constants.result).asOpt[JsValue] match {
        case Some(value) => res ++ Json.arr(value)
        case _ => res
      }

      val error = current.\(Constants.error).asOpt[JsValue] match {
        case Some(value) => err ++ Json.arr(value)
        case _ => err
      }
      (result, error)
    }
    Json.obj(Constants.result -> result, Constants.error -> error)
  }

}
