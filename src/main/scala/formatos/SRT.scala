package formatos

import ejemplo_mongo.Texto
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import video.Archivo

import scala.io.{Codec, Source}
import scala.reflect.io.{Directory, File}

class SRT extends Formato("srt") {

  def levantarArchivo(archivo: Archivo) = {
    //--Ir guardando las líneas a parsear
    var lines: List[String] = List[String]()

    //--Iterar las líneas del archivo
    Source.fromFile(archivo.path, "ISO-8859-1").getLines.foreach { line =>
      val _line = line.trim

      //--Línea vacía => texto completo
      if (_line.isEmpty) {
        if (lines.nonEmpty) {

          archivo.agregarTexto(new Texto(
            lines.takeRight(lines.length - 2).mkString(" "),
            DateTime.parse(lines(1).split("-->").head.trim, DateTimeFormat.forPattern("HH:mm:ss,SSS")),
            DateTime.parse(lines(1).split("-->").last.trim, DateTimeFormat.forPattern("HH:mm:ss,SSS")),
            lines.head.toInt
          ))

          lines = List[String]()
        }
      } else {
        lines = lines :+ _line
      }
    }
  }

}