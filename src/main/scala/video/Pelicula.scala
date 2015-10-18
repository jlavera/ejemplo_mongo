package video

import scala.reflect.io.File

class Pelicula(file: File) extends Archivo(file) {
  def getNombreFinal = nombre_sin_lenguaje.split('-')(1).trim
  def getTipo = "P"

  val anio = nombre_sin_lenguaje.split('-')(0).trim.toInt
}