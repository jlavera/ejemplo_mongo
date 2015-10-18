 drop database Subtitles;
-- phpMyAdmin SQL Dump
-- version 4.2.12deb2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 30, 2015 at 08:14 PM
-- Server version: 5.6.25-0ubuntu0.15.04.1
-- PHP Version: 5.6.4-4ubuntu6.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `Subtitles`
--
CREATE DATABASE IF NOT EXISTS `Subtitles` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `Subtitles`;

-- --------------------------------------------------------

--
-- Table structure for table `Episodio`
--

DROP TABLE IF EXISTS `Episodio`;
CREATE TABLE IF NOT EXISTS `Episodio` (
  `id` int(10) NOT NULL,
  `temporada_id` int(10) NOT NULL,
  `num_episodio` varchar(45) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `Episodio`:
--   `id`
--       `Video` -> `id`
--   `temporada_id`
--       `Temporada` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `Lenguaje`
--

DROP TABLE IF EXISTS `Lenguaje`;
CREATE TABLE IF NOT EXISTS `Lenguaje` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Pelicula`
--

DROP TABLE IF EXISTS `Pelicula`;
CREATE TABLE IF NOT EXISTS `Pelicula` (
`id` int(11) NOT NULL,
  `anio` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS FOR TABLE `Pelicula`:
--   `id`
--       `Video` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `Serie`
--

DROP TABLE IF EXISTS `Serie`;
CREATE TABLE IF NOT EXISTS `Serie` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Temporada`
--

DROP TABLE IF EXISTS `Temporada`;
CREATE TABLE IF NOT EXISTS `Temporada` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serie_id` int(10) NOT NULL,
  `numero` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `Temporada`:
--   `serie_id`
--       `Serie` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `Texto`
--

DROP TABLE IF EXISTS `Texto`;
CREATE TABLE IF NOT EXISTS `Texto` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `video_id` int(10) NOT NULL,
  `tiempo_inicio` datetime NOT NULL,
  `tiempo_fin` datetime NOT NULL,
  `texto` text NOT NULL,
  `orden` int(10),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `Texto`:
--   `lenguaje_id`
--       `Lenguaje` -> `id`
--   `video_id`
--       `Video` -> `id`
--

-- --------------------------------------------------------

--
-- Table structure for table `Tipo`
--

DROP TABLE IF EXISTS `Tipo`;
CREATE TABLE IF NOT EXISTS `Tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `descripcion` char(1) NOT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Video`
--

DROP TABLE IF EXISTS `Video`;
CREATE TABLE IF NOT EXISTS `Video` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(25) NOT NULL,
  `tipo_id` int(10) NOT NULL,
  `lenguaje_id` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS FOR TABLE `Video`:
--   `tipo_id`
--       `Tipo` -> `id`
--   `lenguaje_id`
--       `Lenguaje` -> `id`
--

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Episodio`
--
ALTER TABLE `Episodio` ADD KEY `fk_Episodio_Temporada1_idx` (`temporada_id`);

--
-- Indexes for table `Temporada`
--
ALTER TABLE `Temporada` ADD KEY `fk_Temporada_Serie1_idx` (`serie_id`);

--
-- Indexes for table `Texto`
--
ALTER TABLE `Texto` ADD KEY `fk_Texto_Video_idx` (`video_id`);

--
-- Indexes for table `Video`
--
ALTER TABLE `Video` ADD KEY `fk_Video_Tipo1_idx` (`tipo_id`), ADD KEY `fk_Video_Lenguaje1_idx` (`lenguaje_id`);


--
-- Constraints for table `Episodio`
--
ALTER TABLE `Episodio`
ADD CONSTRAINT `fk_Episodio_Temporada1` FOREIGN KEY (`temporada_id`) REFERENCES `Temporada` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Episodio_Video1` FOREIGN KEY (`id`) REFERENCES `Video` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Pelicula`
--
ALTER TABLE `Pelicula`
ADD CONSTRAINT `fk_Pelicula_Video1` FOREIGN KEY (`id`) REFERENCES `Video` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Temporada`
--
ALTER TABLE `Temporada`
ADD CONSTRAINT `fk_Temporada_Serie1` FOREIGN KEY (`serie_id`) REFERENCES `Serie` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Texto`
--
ALTER TABLE `Texto`
ADD CONSTRAINT `fk_Texto_Video` FOREIGN KEY (`video_id`) REFERENCES `Video` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Video`
--
ALTER TABLE `Video`
ADD CONSTRAINT `fk_Video_Lenguaje1` FOREIGN KEY (`lenguaje_id`) REFERENCES `Lenguaje` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Video_Tipo1` FOREIGN KEY (`tipo_id`) REFERENCES `Tipo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;




-- Valores iniciales

insert into Tipo (descripcion) values ('P'), ('E');
