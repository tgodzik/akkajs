package controllers

import models.{Script, ScriptList}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization._
import org.json4s.jackson.JsonMethods
import play.api.mvc.{Action, Controller}
import utils.db.MongoLocalDatabase

class ScriptController extends Controller {

  implicit val formats = DefaultFormats

  def scripts = Action { implicit req =>
    val scripts = MongoLocalDatabase.find[Script]()
    Ok(write(ScriptList(scripts)))
  }

  def script(scriptId: Int) = Action { implicit req =>
    val script = MongoLocalDatabase.findOne[Script](scriptId)
    script.map(scr => Ok(write(scr))).getOrElse(NotFound)
  }

  def saveScript = Action(parse.tolerantText) { implicit request =>
    val script = JsonMethods.parse(request.body).extract[Script]
    val updatedScript = MongoLocalDatabase.save(script)
    Ok(write(updatedScript))
  }

  def update(id: Int) = Action(parse.tolerantText) { implicit request =>
    val script = JsonMethods.parse(request.body).extract[Script]
    MongoLocalDatabase.findOne[Script](id).map {
      found =>
        MongoLocalDatabase.update(id, script)
        Ok(write(script))
    }.getOrElse(NotFound)
  }

  def angular(any: String) = Action {
    Ok(views.html.Index("Scripts"))
  }

  def index = angular("")
}
