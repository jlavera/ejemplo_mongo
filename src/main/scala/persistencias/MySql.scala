package persistencias

import java.sql.{Date, Timestamp, CallableStatement}

import ejemplo_mongo.Texto
import org.joda.time.{DateTimeZone, DateTime}
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

      //--Insertar archivo
      var video_id = file match {
        case epi: Episodio => {
          insertVideo(epi)
        }
        case pel: Pelicula => {
          insertVideo(pel)
        }
      }

      //--Insertar textos del archivo
      file.textos.foreach{ texto =>
        insertTexto(video_id, texto)
      }

    }}
  }

  def insertVideo(episodio: Episodio)(implicit session:Session): Int = {
    var video_id = insertVideo(episodio.getNombreEpisodio, episodio.lenguaje, episodio.getTipo)
    insertEpisodio(video_id, episodio)
    video_id
  }
  def insertVideo(pelicula: Pelicula)(implicit session:Session): Int = {
    var video_id = insertVideo(pelicula.getNombreFinal, pelicula.lenguaje, pelicula.getTipo)
    insertPelicula(video_id, pelicula)
    video_id
  }

  //CREATE FUNCTION insertVideo(unNombre varchar(30),unLenguaje varchar(30), unTipo varchar(3)) returns int
  def insertVideo(nombre: String, lenguaje: String, tipo: String)(implicit session: Session): Int = {
    StaticQuery.queryNA[Int]("select insertVideo('" + nombre + "', '" + lenguaje + "', '" + tipo + "') LIMIT 1").first
  }

  //CREATE PROCEDURE insertPelicula(IN id_video INT,IN anio INT)
  def insertPelicula(idVideo: Int, pelicula: Pelicula)(implicit session: Session) = {
    val procSQL = "call insertPelicula(?, ?);"
    val cs = session.conn.prepareCall(procSQL)
    cs.setInt(1, idVideo)
    cs.setInt(2, pelicula.anio)
    cs.execute
  }

  //CREATE PROCEDURE insertEpisodio(IN id_video INT, nombre_serie varchar(30),IN temporada_serie INT,IN numero_episodio_serie varchar(45))
  def insertEpisodio(idVideo: Int, episodio: Episodio)(implicit session: Session) = {
    val procSQL = "call insertEpisodio(?, ?, ?, ?);"
    val cs = session.conn.prepareCall(procSQL)
    cs.setInt(1, idVideo)
    cs.setString(2, episodio.nombreSerie)
    cs.setInt(3, episodio.temporada)
    cs.setInt(4, episodio.episodio)
    cs.execute
  }

  //CREATE PROCEDURE `insertTexto`(IN id_video INT, texto TEXT, inicio DateTime, fin DateTime, orden INT)
  def insertTexto(idVideo: Int, texto: Texto)(implicit session: Session) = {
    val procSQL = "call insertTexto(?, ?, ?, ?, ?);"
    val cs = session.conn.prepareCall(procSQL)
    cs.setInt(1, idVideo)
    cs.setString(2, texto.texto)
    cs.setTimestamp(3, new Timestamp(texto.desde.getMillis+60*60*1000)) //because fuck you, that's why
    cs.setTimestamp(4, new Timestamp(texto.hasta.getMillis+60*60*1000))
    cs.setInt(5, texto.orden)
    cs.execute
  }
}