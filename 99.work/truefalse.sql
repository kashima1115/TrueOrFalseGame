-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2017 年 9 朁E08 日 03:55
-- サーバのバージョン： 5.6.16
-- PHP Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `truefalse`
--

-- --------------------------------------------------------

--
-- テーブルの構造 `battle_result`
--

CREATE TABLE IF NOT EXISTS `battle_result` (
  `battle_id` int(11) NOT NULL,
  `logic_id` int(11) NOT NULL,
  `result` varchar(40) NOT NULL,
  `first_second` varchar(40) NOT NULL,
  `date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`battle_id`,`logic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=sjis;

--
-- テーブルのデータのダンプ `battle_result`
--

INSERT INTO `battle_result` (`battle_id`, `logic_id`, `result`, `first_second`, `date`, `start_time`, `end_time`) VALUES
(1, 1, 'win', 'first', '2017-08-16', '17:19:18', '17:24:50'),
(1, 2, 'lose', 'second', '2017-08-16', '17:19:18', '17:24:50'),
(2, 1, 'win', 'first', '2017-08-17', '15:29:10', '15:25:32'),
(2, 3, 'lose', 'second', '2017-08-17', '15:29:10', '15:25:32'),
(3, 2, 'draw', 'first', '2017-08-17', '18:55:50', '18:59:22'),
(3, 4, 'draw', 'second', '2017-08-17', '18:55:50', '18:59:22'),
(4, 6, 'win', 'first', '2017-08-28', '13:39:00', '13:45:20'),
(4, 7, 'lose', 'second', '2017-08-28', '13:39:33', '13:45:20');

-- --------------------------------------------------------

--
-- テーブルの構造 `location`
--

CREATE TABLE IF NOT EXISTS `location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `battle_id` int(11) NOT NULL,
  `logic_id` int(11) NOT NULL,
  `location_x` int(11) NOT NULL,
  `location_y` int(11) NOT NULL,
  `turn` int(11) NOT NULL,
  `play_start` time NOT NULL,
  `play_end` time NOT NULL,
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=sjis AUTO_INCREMENT=27 ;

--
-- テーブルのデータのダンプ `location`
--

INSERT INTO `location` (`location_id`, `battle_id`, `logic_id`, `location_x`, `location_y`, `turn`, `play_start`, `play_end`) VALUES
(1, 1, 1, 0, 0, 1, '17:19:18', '17:18:50'),
(2, 1, 2, 0, 1, 2, '17:20:18', '17:21:50'),
(3, 1, 1, 1, 1, 3, '17:21:18', '17:22:50'),
(4, 1, 2, 1, 2, 4, '17:22:18', '17:23:50'),
(5, 1, 1, 2, 2, 5, '17:23:18', '17:24:50'),
(6, 2, 1, 2, 2, 1, '11:58:00', '11:58:01'),
(7, 2, 3, 0, 1, 2, '11:58:02', '11:58:03'),
(8, 2, 1, 1, 1, 3, '11:58:04', '11:58:05'),
(9, 2, 3, 1, 0, 4, '11:58:06', '11:58:07'),
(10, 2, 1, 0, 0, 5, '11:58:08', '11:58:09'),
(11, 3, 2, 0, 0, 1, '12:01:00', '12:01:01'),
(12, 3, 4, 1, 1, 2, '12:01:03', '12:01:04'),
(13, 3, 2, 2, 2, 3, '12:01:06', '12:01:07'),
(14, 3, 4, 0, 1, 4, '12:01:09', '12:01:10'),
(15, 3, 2, 2, 1, 5, '12:01:12', '12:01:13'),
(16, 3, 4, 2, 0, 6, '12:01:15', '12:01:16'),
(17, 3, 2, 0, 2, 7, '12:01:18', '12:01:19'),
(18, 3, 4, 1, 2, 8, '12:01:21', '12:01:22'),
(19, 3, 2, 1, 0, 9, '12:01:24', '12:01:25'),
(20, 4, 6, 0, 0, 1, '13:39:00', '13:39:20'),
(21, 4, 7, 2, 2, 2, '13:40:00', '13:40:20'),
(22, 4, 6, 2, 0, 3, '13:41:00', '13:41:20'),
(23, 4, 7, 1, 0, 4, '13:42:00', '13:42:20'),
(24, 4, 6, 0, 2, 5, '13:43:00', '13:43:20'),
(25, 4, 7, 1, 1, 6, '13:44:00', '13:44:20'),
(26, 4, 6, 0, 1, 7, '13:45:00', '13:45:20');

-- --------------------------------------------------------

--
-- テーブルの構造 `logic`
--

CREATE TABLE IF NOT EXISTS `logic` (
  `logic_id` int(11) NOT NULL AUTO_INCREMENT,
  `logic_name` varchar(40) NOT NULL,
  `logic_writer` varchar(40) NOT NULL,
  `logic_ver` varchar(40) NOT NULL,
  PRIMARY KEY (`logic_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=sjis AUTO_INCREMENT=8 ;

--
-- テーブルのデータのダンプ `logic`
--

INSERT INTO `logic` (`logic_id`, `logic_name`, `logic_writer`, `logic_ver`) VALUES
(1, 'Alogic', 'arahari', '0.1'),
(2, 'Blogic', 'takeru', '0.2'),
(3, 'Clogic', 'kanayama', '0.1'),
(4, 'Dlogic', 'hatsugai', '0.3'),
(5, 'AtsushiLogic', 'hatsugai', '0.1'),
(6, 'テストロジック', '荒張', '0.4'),
(7, '更新チェック', '武', '0.6');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
