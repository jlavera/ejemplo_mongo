package video

import ejemplo_mongo.Texto
import formatos.{SRT, Formato}
import scala.reflect.io.File

object Archivo {
  val formatos_soportados = List[Formato](new SRT)
  val Pattern = "-S[0-9]+E[0-9]+-".r

  def apply(file: File): Option[Archivo]= {

    //--Crear película o episodio, según la composición del nombre
    val video =
      if (Pattern.findFirstIn(file.name).isEmpty)
        new Pelicula(file)
      else
        new Episodio(file)

    //--Validar que sea un subtítulo de formato válido
    if (Archivo.formatos_soportados.exists{ _.soporta(video.extension) }) {
      Some(video)
    } else {
      None
    }

  }
}

abstract class Archivo(archivo: File){
  val path = archivo.path.trim
  val nombre_completo = archivo.name.trim
  val nombre_sin_extension = nombre_completo.split('.').head.trim
  val extension = nombre_completo.split('.').last.trim

  val nombre_sin_lenguaje = nombre_sin_extension.split('_').head.trim
  val lenguaje = nombre_sin_extension.split('_').last.trim

  var textos = List[Texto]()

  val formato =
    Archivo.formatos_soportados
      .find(_.soporta(extension))
      .getOrElse(throw new Exception("Not supported file type: " + archivo.name))

  def getNombreFinal: String
  def getTipo: String

  def levantarArchivo = formato.levantarArchivo(this)
  def agregarTexto(texto: Texto) = textos = textos :+ texto
}