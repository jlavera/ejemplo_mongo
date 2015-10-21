import formatos.{Formato, SRT}
import org.joda.time.DateTimeZone
import persistencias.{Persistencia, MySql, Mongo}
import video.Archivo

import scala.reflect.io.Directory

object Main {
  DateTimeZone.setDefault(DateTimeZone.forOffsetHours(0))
  val Dir_Name = "subs"

  val persistencias = List[Persistencia](
    new Mongo,
    new MySql
   )

  def main(args: Array[String]) {

    //--Entrar en la carpeta con los subtitulos
    val archivos = Directory(Directory.Current.get.path + "/" + Dir_Name + "/").files

    //--Ciclar los archivos para transformarlos en objetos de dominio con su información propia
    val videos =
      archivos map { file => //--Transformar en objetos propios
        Archivo(file)
      } filter  { file => //--Filtrar los subtitulos cuyo formato no soportemos
        file.soportado
      } toList

    //--Levantar todos los archivos es una etapa separada, debido a su costo
    videos foreach { video => video levantarArchivo }

    persistencias foreach { persistencia => persistencia persistir videos }

  }
}
