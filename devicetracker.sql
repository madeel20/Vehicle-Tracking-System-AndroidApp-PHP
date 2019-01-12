-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 12, 2017 at 09:20 AM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 7.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `devicetracker`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `username` text NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`username`, `password`) VALUES
('admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `devices`
--

CREATE TABLE `devices` (
  `deviceid` int(11) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `longitude` text NOT NULL,
  `langitude` text NOT NULL,
  `devicename` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `devices`
--

INSERT INTO `devices` (`deviceid`, `username`, `password`, `longitude`, `langitude`, `devicename`) VALUES
(3, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(4, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(6, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(7, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(10, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(14, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(16, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(17, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(18, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(19, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(20, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(21, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(22, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(23, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(24, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(25, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(26, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(27, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(28, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(29, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(30, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(31, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(32, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(33, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(34, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(35, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(36, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(37, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(38, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(39, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(40, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(41, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(42, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(43, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(44, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(45, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(46, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(47, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(48, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(49, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(50, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(51, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(52, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(53, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(54, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(55, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(56, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(57, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(58, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(59, 'ADEEL', 'adeel', '29340293', '23434', 'asdf;kl'),
(61, 'zahid', 'zahid', '67.0622', '24.9977', 'Galaxy S5');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `devices`
--
ALTER TABLE `devices`
  ADD PRIMARY KEY (`deviceid`) KEY_BLOCK_SIZE=4 USING HASH;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `devices`
--
ALTER TABLE `devices`
  MODIFY `deviceid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
