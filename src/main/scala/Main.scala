import formatos.{Formato, SRT}
import persistencias.{Persistencia, MySql, Mongo}
import video.Archivo

import scala.reflect.io.Directory

object Main {
  val Dir_Name = "subs"

  val persistencias = List[Persistencia](
    new Mongo,
    new MySql
   )

  def main(args: Array[String]) {
    println(Dir_Name)
    //--Entrar en la carpeta con los subtitulos
    val archivos = Directory(Directory.Current.get.path + "/" + Dir_Name + "/").files

    //--Ciclar los archivos para transformarlos en objetos de dominio con su información propia
    val videos =
      archivos flatMap { file =>
        //--Transformar en objetos propios
        Archivo(file)
      } toList

    //--Levantar todos los archivos es una etapa separada, debido a su costo
    videos foreach { video => video levantarArchivo }

    //--La declaratividad de la siguiente línea habla por si misma
    persistencias foreach { persistencia => persistencia persistir videos }
  }
}
