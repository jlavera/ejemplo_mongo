package persistencias

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._

import video.{Pelicula, Episodio, Archivo}

class Mongo extends Persistencia("MongoDB"){
  RegisterJodaTimeConversionHelpers()
  val Mongo_Dir = "localhost"
  val Mongo_Port = 27017
  val Mongo_DB = "subtitulos"
  val Mongo_Collection = "videos"

  val collection = MongoClient(Mongo_Dir, Mongo_Port)(Mongo_DB)(Mongo_Collection)

  def doClear = {
    collection.drop()
  }

  def doPersistir(file: Archivo) = {
    val builder = MongoDBObject.newBuilder

    builder += "nombre" -> file.getNombreFinal
    builder += "lenguaje" -> file.lenguaje

    file match {
      //--Agregar los datos de los episodios
      case epi: Episodio => {
        builder += "temporada" -> epi.temporada
        builder += "nombre_episodio" -> epi.getNombreEpisodio
        builder += "num_episodio" -> epi.episodio
      }
      case pel: Pelicula => {
        builder += "año" -> pel.anio
      }
    }

    builder += "textos" -> file.textos.map { texto =>
      MongoDBObject(
        "orden" -> texto.orden,
        "desde" -> texto.desde,//.toString("yyyy-MM-dd HH:mm:ss.SSS"),
        "hasta" -> texto.hasta,//.toString("yyyy-MM-dd HH:mm:ss.SSS"),
        "texto" -> texto.texto
      )
    }

    collection.insert(builder.result)
  }
}