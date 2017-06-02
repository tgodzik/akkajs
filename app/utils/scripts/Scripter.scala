package utils.scripts

import javax.script.{Invocable, ScriptEngineManager}

import models.{Script, Sender}

import scala.util.Try

trait ScriptEngine {
  protected val engineManager = new ScriptEngineManager(null)
  protected val engine = engineManager.getEngineByName("Nashorn")

  def compile(script: Script): Boolean

  def runReceive(id: String, message: String, actor: Sender): Unit = {
    engine.asInstanceOf[Invocable].invokeFunction(s"receive$id", message, actor)
  }

}

object ScriptEngine {

  def javascript(): Javascript = new Javascript

}


class Javascript extends ScriptEngine {

  def compile(script: Script): Boolean = {
    Try {
      engine.eval(
        s"""
           |  function receive${script.id}(message, sender){
           |     ${script.code}
           | };
        """.stripMargin)
    }.isSuccess
  }
}