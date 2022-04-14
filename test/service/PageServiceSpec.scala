package service

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpecLike
import scalaz.NonEmptyList

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext


class PageServiceSpec extends TestKit(ActorSystem("CrawlerActorTest"))
  with AnyWordSpecLike
  with Matchers {

  implicit val ex: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  val pageService = new PageService(system)


  "PageServiceSpec" must {
    "fetch crawled data for a correct url " in {
      val urls = List("https://www.test.com")
      pageService.getPage(urls).map(result => result shouldBe NonEmptyList)
    }
  }

}
