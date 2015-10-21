DELIMITER $$
USE Subtitles;

CREATE FUNCTION `insertVideo`(unNombre varchar(100),unLenguaje varchar(30), unTipo varchar(3)) RETURNS int(11)
BEGIN
		DECLARE id_tipo int unsigned DEFAULT 0;
		DECLARE id_lenguaje int unsigned DEFAULT 0;
		DECLARE id_video int unsigned DEFAULT 0;

		SET id_tipo = (SELECT id FROM Tipo t WHERE t.descripcion=unTipo LIMIT 1);

		SET id_lenguaje = (SELECT id FROM Lenguaje l WHERE l.descripcion=unLenguaje LIMIT 1);
		
		IF (id_lenguaje IS NULL) THEN
			INSERT INTO Lenguaje (descripcion) VALUES (unLenguaje);
			SET id_lenguaje = LAST_INSERT_ID();
		END IF;

		INSERT INTO Video (nombre, tipo_id, lenguaje_id) VALUES (unNombre, id_tipo,id_lenguaje);

		SET id_video = LAST_INSERT_ID();
		RETURN id_video;
	END

CREATE PROCEDURE `insertPelicula`(IN id_video INT,IN anio INT)
BEGIN
	INSERT INTO Pelicula (id, anio) VALUES (id_video,anio);
END

CREATE PROCEDURE `insertEpisodio`(
	IN id_video INT, nombre_serie varchar(30), IN temporada_serie INT, IN numero_episodio_serie varchar(45))
BEGIN
	DECLARE id_serie int unsigned DEFAULT 0;
	DECLARE id_temporada int unsigned DEFAULT 0;

	SET id_serie = (SELECT id FROM Serie WHERE nombre = nombre_serie LIMIT 1);
	IF (id_serie IS NULL) THEN
		INSERT INTO Serie (nombre) VALUES (nombre_serie);
		SET id_serie = LAST_INSERT_ID();
	END IF;

	SET id_temporada = (SELECT id FROM Temporada t WHERE t.numero = temporada_serie AND t.serie_id = id_serie LIMIT 1);
	IF (id_temporada IS NULL) THEN
		INSERT INTO Temporada (numero,serie_id) VALUES (temporada_serie, id_serie);
		SET id_temporada = LAST_INSERT_ID();
	END IF;

	INSERT INTO Episodio (id, num_episodio,temporada_id) VALUES (id_video, numero_episodio_serie, id_temporada);
END

CREATE PROCEDURE `insertTexto`(IN id_video INT, texto TEXT, inicio DateTime, fin DateTime, orden INT)
BEGIN
	INSERT INTO Texto (video_id, texto, tiempo_inicio, tiempo_fin, orden) VALUES 
		(id_Video, texto, inicio, fin, orden);
END

CREATE PROCEDURE `clear`()
BEGIN
	SET FOREIGN_KEY_CHECKS=0;
	truncate table Texto;
	truncate table Pelicula;
	truncate table Episodio;
	truncate table Serie;
	truncate table Temporada;
	truncate table Video;
	truncate table Lenguaje;
	SET FOREIGN_KEY_CHECKS=1;
END

DELIMITER ;
