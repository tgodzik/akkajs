package models

import akka.actor.{Actor, ActorRef, ActorSystem}

class Sender(sender: ActorRef)
            (implicit system: ActorSystem, self: ActorRef = Actor.noSender) {

  def path(iId: String) = system / iId

  def tell(id: String, message: String): Unit = {
    system.actorSelection(path(id)) ! MessageWithSender(message, self)
  }

  def tell(message: String): Unit = {
    sender ! MessageWithSender(message, self)
  }
}
