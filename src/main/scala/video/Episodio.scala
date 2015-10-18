package video

import scala.io.{Codec, Source}
import scala.reflect.io.{Directory, File}
import scala.util.matching.Regex

class Episodio(file: File) extends Archivo(file) {
  val nombreSerie = nombre_completo.split('-')(0).trim
  val temporada = new Regex("S([0-9]+)E").findFirstMatchIn(nombre_sin_extension).get.group(1).toInt
  val episodio = new Regex("E([0-9]+)-").findFirstMatchIn(nombre_sin_extension).get.group(1).toInt

  def getTipo = "E"
  def getNombreFinal = nombre_sin_lenguaje.split('-').head.trim
  def getNombreEpisodio = nombre_sin_lenguaje.split('-').last.trim
}