package utils.actors

import akka.actor._
import models.{MessageWithSender, Script, Sender}
import org.json4s.DefaultFormats
import utils.actors.ScriptActor.{AddObserver, RemoveObserver}
import utils.scripts.ScriptEngine

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

    case MessageWithSender(msg, ActorRef.noSender) =>
      engine.runReceive(script.id, msg, new Sender(out))

    case MessageWithSender(msg, otherSender) =>
      engine.runReceive(script.id, msg, new Sender(otherSender))
  }
}

object ScriptActor {

  case class AddObserver(sender : ActorRef)

  case class RemoveObserver(sender : ActorRef)

  case class SetScript()

  def props(script: Script)(out: ActorRef) = Props(classOf[ScriptActor], out, script)
}

