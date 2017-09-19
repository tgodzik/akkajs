package utils.db

import com.mongodb.DBObject
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization._

import scala.util.Try

object MongoLocalDatabase {

  private val client = MongoClient("localhost")

  private val db = client("AkkaJs")

  private implicit val formats = DefaultFormats

  def counter(collectionName: String): Int = {
    val collection = db("counters")
    val query = MongoDBObject("_id" -> collectionName)
    collection.findOne(query).map {
      _ =>
        val update = MongoDBObject("$inc" -> MongoDBObject("seq" -> 1))
        collection.findAndModify(query, update).get.get("seq").asInstanceOf[Int]
    } getOrElse {
      collection.insert(MongoDBObject("_id" -> collectionName, "seq" -> 0))
      0
    }
  }

  def save[T <: AnyRef](model: T)(implicit manifest: Manifest[T]): T = {
    val collectionName = manifest.runtimeClass.getSimpleName.toLowerCase
    val saved = write(model)
    val obj = MongoDBObject(saved)
    val id = counter(collectionName)
    obj.put("id", id)
    db(collectionName).save(obj)
    convertToModel(obj)
  }

  def update[T <: AnyRef](id: Int, model: T)(implicit manifest: Manifest[T]) = {
    val collectionName = manifest.runtimeClass.getSimpleName.toLowerCase
    val saved = write(model)
    val obj = MongoDBObject(saved)
    db(collectionName).update(MongoDBObject("id" -> id), obj)
  }

  def find[T]()(implicit manifest: Manifest[T]): List[T] = {
    val collectionName = manifest.runtimeClass.getSimpleName.toLowerCase
    db(collectionName).find().map(convertToModel(_)(manifest)).toList
  }

  def findOne[T](id: Int)(implicit manifest: Manifest[T]): Option[T] = {
    val collectionName = manifest.runtimeClass.getSimpleName.toLowerCase
    val query = MongoDBObject("id" -> id)
    db(collectionName).findOne(query).map(convertToModel(_)(manifest))
  }

  private def convertToModel[T: Manifest](mongoDbObject: DBObject) = {
    parse(mongoDbObject.toString).extract[T]
  }
}
