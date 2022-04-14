package actor

import akka.actor._
import model.{CustomError, HtmlPage}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import scala.util.{Failure, Success, Try}

/*
 CrawlerActor is used for all Actor calls to crawl data from a webpage.
 */

class CrawlerActor extends Actor with ActorLogging {

  import CrawlerActor._

  private val browser = JsoupBrowser()

  /*
  Synchronous function to crawl a web page and either return the page content or if
  error message in case of exceptions.

  Params    urls - List of web page urls to be crawled.
  Returns   JsObject - Future JsObject result of crawled data and errors if any.
   */
  private def crawlWebPage(url: String) = {
    Try {
      browser.get(url)
    } match {
      case Success(doc) => HtmlPage(doc.location, doc.title, doc.body.text).toJson
      case Failure(exception) =>
        log.error(s"Failed to get Page with exception: $exception")
        CustomError(url, exception.toString).toJson
    }
  }

  def receive = {
    case CrawlWebPage(url: String) =>
      sender() ! crawlWebPage(url)
  }
}

object CrawlerActor {
  def props: Props = Props[CrawlerActor]

  def actorName = "Web-Crawler"

  case class CrawlWebPage(url: String)
}
