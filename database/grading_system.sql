-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 20, 2019 at 06:35 PM
-- Server version: 5.7.25-0ubuntu0.16.04.2
-- PHP Version: 7.0.33-0ubuntu0.16.04.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `grading_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `COURSE_ID` varchar(10) NOT NULL,
  `COURSE_NAME` varchar(25) NOT NULL,
  `QUOTA` int(11) NOT NULL,
  `INSTRUCTOR_ID` int(11) NOT NULL,
  `ATTENDANTS` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`COURSE_ID`, `COURSE_NAME`, `QUOTA`, `INSTRUCTOR_ID`, `ATTENDANTS`) VALUES
('BIM101', 'Programming I', 15, 10, 4),
('BIM102', 'Programming II', 15, 10, 4),
('ENG101', 'English A1', 20, 15, 1),
('ENG102', 'English A2', 20, 15, 1),
('MAT805', 'Calculus I', 30, 10, 1),
('MAT806', 'Calculus II', 40, 10, 1);

-- --------------------------------------------------------

--
-- Table structure for table `grades`
--

CREATE TABLE `grades` (
  `student_id` int(11) DEFAULT NULL,
  `course_id` varchar(7) DEFAULT NULL,
  `point` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `grades`
--

INSERT INTO `grades` (`student_id`, `course_id`, `point`) VALUES
(255, 'BIM101', 40),
(255, 'BIM102', 70),
(310, 'BIM101', 100),
(310, 'BIM102', 45),
(412, 'BIM101', 55),
(412, 'BIM102', 90),
(581, 'ENG101', 85),
(581, 'BIM101', 75),
(581, 'MAT805', 75),
(581, 'ENG102', 40),
(581, 'MAT806', -1),
(581, 'BIM102', 55);

-- --------------------------------------------------------

--
-- Table structure for table `instructors`
--

CREATE TABLE `instructors` (
  `ID` int(11) NOT NULL,
  `USER_NAME` varchar(25) NOT NULL,
  `PASSWORD` varchar(25) DEFAULT NULL,
  `FIRST_NAME` varchar(25) NOT NULL,
  `LAST_NAME` varchar(25) DEFAULT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `TELEPHONE` varchar(25) NOT NULL,
  `ADMIN` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instructors`
--

INSERT INTO `instructors` (`ID`, `USER_NAME`, `PASSWORD`, `FIRST_NAME`, `LAST_NAME`, `EMAIL`, `TELEPHONE`, `ADMIN`) VALUES
(0, 'admin', '12345', 'Admin', 'Admin', '--', '--', 1),
(10, 'instructor1', '12345', 'Instructor', '1', '--', '--', 0),
(15, 'ezgialacam', '12345', 'Ezgi', 'Alaçam', '--', '--', 0);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `ID` int(11) NOT NULL,
  `USER_NAME` varchar(25) NOT NULL,
  `PASSWORD` varchar(25) DEFAULT NULL,
  `FIRST_NAME` varchar(25) NOT NULL,
  `LAST_NAME` varchar(25) DEFAULT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `TELEPHONE` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`ID`, `USER_NAME`, `PASSWORD`, `FIRST_NAME`, `LAST_NAME`, `EMAIL`, `TELEPHONE`) VALUES
(252, 'muratavni', '12345', 'Murat', 'Avni', 'muratavni@gmail.com', '--'),
(255, 'student1', '12345', 'Student', '1', '--', '--'),
(310, 'mithat', '12345', 'Mithat', 'Menderes', 'mithatmenderes', '--'),
(412, 'ayseinan', '12345', 'Ayse Inan', 'Alican', 'ayseinanalican@tubitak.gov.tr', '--'),
(580, 'serdar', '12345', 'Serdar', 'Efendi', 'serdarefendi@gmail.com', '05001112222'),
(581, 'umutcanalacam', '12345', 'Umut Can', 'Alaçam', 'uca', '--'),
(615, 'rebelknight', '12345', 'Bahar', 'Gürsoy', '--', '--');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`COURSE_ID`),
  ADD UNIQUE KEY `courses_COURSE_ID_uindex` (`COURSE_ID`);

--
-- Indexes for table `instructors`
--
ALTER TABLE `instructors`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `instructors_USER_NAME_uindex` (`USER_NAME`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `students_USER_NAME_uindex` (`USER_NAME`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
