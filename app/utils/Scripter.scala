package utils

import javax.script.{Invocable, ScriptEngineManager}

import controllers.Sender

import scala.io.Source


object Scripter {

  val engineManager = new ScriptEngineManager(null)
  val engine = engineManager.getEngineByName("Nashorn")
  val item = Source.fromURL(getClass.getResource("/com/isaacloud/engine/item.js")).reader()

  def compile(id: String, script: String): Unit = {

    engine.eval(
      s"""
          |  function receive$id(input, actor){
          |     $script
          | };
        """.stripMargin)

  }

  def runReceive(id: String, message: String, actor: Sender): Unit = {
    engine.asInstanceOf[Invocable].invokeFunction(s"receive$id", message, actor)
  }
}
