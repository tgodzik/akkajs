package utils.scripts

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import models.Sender
import org.scalatest._


class TestJavascript extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with FlatSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Script" should "respond with pong" in {

    val scripter = ScriptEngine.javascript()

    val receiveScript =
      """
        | if (message == "ping"){
        |   sender.tell("pong")
        | }
      """.stripMargin
    val actorId = "1"
    scripter.compile(actorId, receiveScript)

    val testProbe = new TestProbe(system)
    val sender = new Sender(testProbe.ref)
    scripter.runReceive(actorId, "ping", sender)

    testProbe.expectMsg("pong")
  }
}
