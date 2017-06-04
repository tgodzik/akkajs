package models

import akka.actor.ActorRef
import akka.stream.scaladsl.Flow
import play.api.http.websocket._
import play.api.libs.streams.AkkaStreams
import play.api.mvc.WebSocket.MessageFlowTransformer

case class MessageWithSender(contents : String, sender : ActorRef)

object MessageWithSender{
  implicit val messageFlowTransformer = new MessageFlowTransformer[MessageWithSender, MessageWithSender] {
    def transform(flow: Flow[MessageWithSender, MessageWithSender, _]) = {
      AkkaStreams.bypassWith[Message, MessageWithSender, Message](Flow[Message] collect {
        case TextMessage(text) => Left(MessageWithSender(text, ActorRef.noSender))
        case BinaryMessage(_) =>
          Right(CloseMessage(Some(CloseCodes.Unacceptable),
            "This WebSocket only supports text frames"))
      })(flow.map( msg => TextMessage(msg.contents)))
    }
  }
}