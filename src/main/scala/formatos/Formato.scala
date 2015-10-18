package formatos

import video.Archivo

abstract class Formato(val extension: String){
  def levantarArchivo(archivo: Archivo)
  def soporta(_ext: String): Boolean = extension == _ext
}
