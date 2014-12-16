package controllers

import akka.actor._
import play.api.Play.current
import play.api.mvc._
import utils.Scripter

case class Sending(targetId: String, message: String)

class Sender(context: ActorContext, sender: ActorRef) {

  def path(iId: String) = context.actorSelection(context.system / iId)

  def tell(id: String, message: Any) = path(id).tell(message, sender)
}

class ScriptActor(out: ActorRef, actorId: String) extends Actor {

  val senderFacade = new Sender(context, this.sender())

  def receive = {

    case msg: String =>

      // use json4s
      val snd = Sending("", "")
      Scripter.runReceive(actorId, snd.message, senderFacade)

  }
}

object ScriptActor {
  def props(out: ActorRef, actorId: String) = Props(classOf[ScriptActor], out, actorId)
}

object ActorCtrl extends Controller {

  def socket(actorId: String) =
    WebSocket.acceptWithActor[String, String] { req => out => ScriptActor.props(out, actorId)}

}