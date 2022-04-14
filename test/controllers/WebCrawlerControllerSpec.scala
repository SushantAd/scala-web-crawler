package controllers

import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, Ok}
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, route, writeableOf_AnyContentAsJson}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class WebCrawlerControllerSpec extends AnyWordSpecLike
  with Matchers {

  implicit val ex: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  implicit val timeout: Timeout = Timeout(5.seconds)
  val app: Application = new GuiceApplicationBuilder().build()


  "WebCrawlerController" must {
    "fetch crawled data for a correct request" in {
      val request = Json.obj("urls" -> Json.arr("https://www.test.com"))

      val response = route(app, FakeRequest(POST, "/api/crawl").withJsonBody(request)).get

      response.map(res => res mustBe (Ok))
    }

    "throw bad request crawled data for a empty request" in {
      val request = Json.obj()

      val response = route(app, FakeRequest(POST, "/api/crawl").withJsonBody(request)).get

      response.map(res => res mustBe (BadRequest))
    }

    "throw bad request crawled data for a incorrect request" in {
      val request = Json.obj("wrong_data" -> Json.arr("https://www.test.com"))

      val response = route(app, FakeRequest(POST, "/api/crawl").withJsonBody(request)).get

      response.map(res => res mustBe (BadRequest))
    }
  }
}
