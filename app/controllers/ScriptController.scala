package controllers

import models.Script
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization._
import org.json4s.jackson.JsonMethods
import play.api.mvc.{Action, Controller}
import utils.db.MongoLocalDatabase

class ScriptController extends Controller {

  implicit val formats = DefaultFormats

  def scripts = Action { implicit req =>
    val scripts = MongoLocalDatabase.find[Script]()
    Ok(write(scripts))
  }

  def script(scriptId: String) = Action { implicit req =>
    val script = MongoLocalDatabase.findOne[Script](scriptId)
    script.map(scr => Ok(write(scr))).getOrElse(NotFound)
  }

  def saveScript = Action(parse.tolerantText) {implicit request =>
    val script = JsonMethods.parse(request.body).extract[Script]
    MongoLocalDatabase.findOne[Script](script.id).map{
      found =>
        MongoLocalDatabase.update(script.id, script)
    }.getOrElse {
      MongoLocalDatabase.save(script)
    }
    Ok(write(script))
  }

  def angular(any : String) = Action {
    Ok(views.html.Index("Scripts"))
  }

  def index = angular("")
}
