package persistencias

import org.joda.time.DateTime
import video.Archivo

abstract class Persistencia(val name: String){
  var inicio: DateTime = _

  def persistir(files: List[Archivo]) = {
    inicio = DateTime.now

    println("[" + name + "] Clearing")
    doClear

    println("[" + name + "] Inicio persistencia")
    files foreach { file =>
      print(" - ")
      doPersistir(file)
      println(file.nombre_completo)
    }

    println("[" + name + "] Fin (" + Math.abs(DateTime.now.getMillis() - inicio.getMillis()) + "ms)")
  }

  def doClear
  def doPersistir(file: Archivo)
}
