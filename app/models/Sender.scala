package models

import akka.actor.{Actor, ActorRef, ActorSystem}

class Sender(sender: ActorRef)(implicit system: ActorSystem, self: ActorRef = Actor.noSender) {

  def path(iId: String) = system / iId

  def tell(id: String, message: Any): Unit = {
    system.actorSelection(path(id)) ! message
  }

  def tell(message: Any): Unit = {
    println("Sending " + message)
    sender ! message
  }
}
