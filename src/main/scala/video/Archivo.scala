package video

import ejemplo_mongo.Texto
import formatos.{SRT, Formato}
import scala.reflect.io.File

object Archivo {
  val formatos_soportados = List[Formato](
    new SRT
  )
  val Pattern = "-S[0-9]+E[0-9]+-".r

  def apply(file: File): Archivo= {
    //--Crear película o episodio, según la composición del nombre
    if (Pattern.findFirstIn(file.name).isEmpty)
      new Pelicula(file)
    else
      new Episodio(file)
  }
}

abstract class Archivo(archivo: File) {
  val path = archivo.path.trim
  val nombre_completo = archivo.name.trim
  val nombre_sin_extension = nombre_completo.split('.').head.trim
  val extension = nombre_completo.split('.').last.trim

  val nombre_sin_lenguaje = nombre_sin_extension.split('_').head.trim
  val lenguaje = nombre_sin_extension.split('_').last.trim

  var textos = List[Texto]()

  def getNombreFinal: String
  def getTipo: String

  def soportado = Archivo.formatos_soportados.exists { _.soporta(extension) }

  def levantarArchivo = Archivo
    .formatos_soportados
    .find( f => f.soporta(extension))
    .get
    .levantarArchivo(this)
  def agregarTexto(texto: Texto) = textos = textos :+ texto
}