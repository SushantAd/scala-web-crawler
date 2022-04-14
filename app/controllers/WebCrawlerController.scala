package controllers

import model.UrlList
import org.slf4j.LoggerFactory
import play.api.mvc._
import service.PageService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


class WebCrawlerController @Inject()(cc: ControllerComponents, pageService: PageService)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  private val logger = LoggerFactory.getLogger(classOf[WebCrawlerController])


  def fetchCrawledData = Action.async { request => {
    request.body.asJson match {
      case Some(body) =>
        body.asOpt[UrlList] match {
          case Some(urlList) =>
            pageService.getPage(urlList.urls).map(Ok(_))
          case _ => Future(BadRequest("Request Body not as expected!"))
        }
      case _ => Future(BadRequest("Request Body not sent!"))
    }
  }
    .recover {
      case ex: Exception =>
        logger.error(ex.getMessage)
        InternalServerError("Something went wrong, Please contact your admin")
    }
  }
}
