package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import models.Script
import org.json4s.DefaultFormats
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Controller, WebSocket}
import utils.actors.ScriptActor
import utils.db.MongoLocalDatabase

import scala.concurrent.Future

class ActorController @Inject()(implicit actorSystem: ActorSystem, materializer: Materializer) extends Controller {

  implicit val formats = DefaultFormats

  def createActor(id: String) = WebSocket.acceptOrResult[String, String] {
    _ =>
      Future.successful {
        MongoLocalDatabase.findOne[Script](id).map { script =>
          ActorFlow.actorRef(ScriptActor.props(script))
        }.toRight(Forbidden)
      }
  }

}
