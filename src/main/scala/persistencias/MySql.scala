package persistencias

import java.sql.{Date, Timestamp, CallableStatement}

import org.joda.time.DateTime
import video.{Pelicula, Episodio, Archivo}
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery

class MySql extends Persistencia("MySQL") {

  val Mysql_Dir = "localhost"
  val Mysql_Port = 3306
  val Mysql_User = "root"
  val Mysql_Pass = "123"
  val Mysql_DB = "Subtitles"

  val db = Database.forURL(
    url = "jdbc:mysql://" + Mysql_Dir + ":" + Mysql_Port + "/" + Mysql_DB,
    user = Mysql_User,
    password = Mysql_Pass,
    driver = "com.mysql.jdbc.Driver"
  )

  def doClear = {
    db.withSession { implicit session =>
      val procSQL = "call clear();"
      val cs = session.conn.prepareCall(procSQL)
      cs.execute
    }
  }

  def doPersistir(file: Archivo) = {
    db.withSession { implicit session => {

      var video_id = 0

      //--Insertar archivo
      file match {
        case epi: Episodio => {
          video_id = insertVideo(epi.getNombreEpisodio, epi.lenguaje, epi.getTipo)
          insertEpisodio(video_id, epi.nombreSerie, epi.temporada, epi.episodio)
        }
        case pel: Pelicula => {
          video_id = insertVideo(pel.getNombreFinal, pel.lenguaje, pel.getTipo)
          insertPelicula(video_id, pel.anio)
        }
      }

      //--Insertar textos del archivo
      file.textos.foreach{texto=>
        insertTexto(video_id, texto.texto,texto.desde, texto.hasta, texto.orden)
      }

    }}
  }

  //CREATE FUNCTION insertVideo(unNombre varchar(30),unLenguaje varchar(30), unTipo varchar(3)) returns int
  def insertVideo(nombre: String, lenguaje: String, tipo: String)(implicit session: Session): Int = {
    StaticQuery.queryNA[Int]("select insertVideo('" + nombre + "', '" + lenguaje + "', '" + tipo + "') LIMIT 1").first
  }

  //CREATE PROCEDURE insertPelicula(IN id_video INT,IN anio INT)
  def insertPelicula(idVideo: Int, anio: Int)(implicit session: Session) = {
    val procSQL = "call insertPelicula(?, ?);"
    val cs = session.conn.prepareCall(procSQL)
    cs.setInt(1, idVideo)
    cs.setInt(2, anio)
    cs.execute
  }

  //CREATE PROCEDURE insertEpisodio(IN id_video INT, nombre_serie varchar(30),IN temporada_serie INT,IN numero_episodio_serie varchar(45))
  def insertEpisodio(idVideo: Int, serie: String, temporada: Int, episodio_numero: Int)(implicit session: Session) = {
    val procSQL = "call insertEpisodio(?, ?, ?, ?);"
    val cs = session.conn.prepareCall(procSQL)
    cs.setInt(1, idVideo)
    cs.setString(2, serie)
    cs.setInt(3, temporada)
    cs.setInt(4, episodio_numero)
    cs.execute
  }

  //CREATE PROCEDURE `insertTexto`(IN id_video INT, texto TEXT, inicio DateTime, fin DateTime, orden INT)
  def insertTexto(idVideo: Int, texto: String, inicio: DateTime, fin: DateTime, orden: Int)(implicit session: Session) = {
    val procSQL = "call insertTexto(?, ?, ?, ?, ?);"
    val cs = session.conn.prepareCall(procSQL)
    cs.setInt(1, idVideo)
    cs.setString(2, texto)
    cs.setDate(3, new Date(inicio.getMillis))
    cs.setDate(4, new Date(fin.getMillis))
    cs.setInt(5, orden)
    cs.execute
  }
}