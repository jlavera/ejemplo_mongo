package ejemplo_mongo

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

case class Texto(texto: String, desde: DateTime, hasta: DateTime, orden: Int)