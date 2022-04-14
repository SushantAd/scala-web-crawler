package actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike


class CrawlerActorSpec() extends TestKit(ActorSystem("CrawlerActorTest"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A web crawler actor should actor" must {
    "send back messages unchanged" in {
      val webCrawlerActor = system.actorOf(TestActors.echoActorProps)
      webCrawlerActor ! "hello world"
      expectMsg("hello world")
    }

  }

}
