package models


case class Script(id : Option[Int], code : String, name : String)

case class ScriptList(data: List[Script])