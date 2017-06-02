package utils.actors

import akka.actor._
import models.{Script, Sender}
import org.json4s.DefaultFormats
import utils.actors.ScriptActor.{AddObserver, Message, RemoveObserver}
import utils.scripts.ScriptEngine
import org.json4s.jackson.JsonMethods._

class ScriptActor(out: ActorRef, script: Script) extends Actor {

  implicit val system = context.system
  val engine = ScriptEngine.javascript()
  engine.compile(script)
  var observers : Set[ActorRef] = Set.empty

  private implicit val formats = DefaultFormats

  def receive = {

    case AddObserver(observer : ActorRef)  =>
      observers = observers + observer

    case RemoveObserver(observer : ActorRef) =>
      observers = observers - observer

    case msg: String =>
      observers.foreach(_ ! msg)
      println(out)
      println(sender())
//      val snd = parse(msg).extract[Message]
      engine.runReceive(script.id, msg, new Sender(out))

  }
}

object ScriptActor {

  case class AddObserver(sender : ActorRef)

  case class RemoveObserver(sender : ActorRef)

  case class SetScript()

  case class Message(targetId : String, message : String)

  def props(script: Script)(out: ActorRef) = Props(classOf[ScriptActor], out, script)
}

