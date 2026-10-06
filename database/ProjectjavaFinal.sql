-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 12, 2025 at 11:38 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projectjava`
--

-- --------------------------------------------------------

--
-- Table structure for table `autoecoleinfo`
--

CREATE TABLE `autoecoleinfo` (
  `name` varchar(50) NOT NULL,
  `description` varchar(50) NOT NULL,
  `phoneNumber` int(11) NOT NULL,
  `website` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  `logo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `autoecoleinfo`
--

INSERT INTO `autoecoleinfo` (`name`, `description`, `phoneNumber`, `website`, `email`, `address`, `logo`) VALUES
('Auto11Ecole', 'drive Safely', 48795483, 'ww.autoecole.com', 'autoecole@gmail.com', 'RasJbal,Bizerte', 'ProjectJava\\src\\main\\resources\\logos\\logo.png');

-- --------------------------------------------------------

--
-- Table structure for table `candidat`
--

CREATE TABLE `candidat` (
  `CIN` int(11) NOT NULL,
  `nom` varchar(20) NOT NULL,
  `prenom` varchar(20) NOT NULL,
  `dateNaissance` date DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `telephone` int(11) NOT NULL,
  `adresse` varchar(20) NOT NULL,
  `typePermis` enum('voiture','moto','camion') NOT NULL,
  `status` varchar(20) NOT NULL,
  `repos` varchar(255) NOT NULL,
  `nbr_seance_code_atteint` int(11) NOT NULL,
  `nbr_seance_conduit_atteint` int(11) NOT NULL,
  `id_vehicule` int(11) DEFAULT NULL,
  `montant_a_payer` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `candidat`
--

INSERT INTO `candidat` (`CIN`, `nom`, `prenom`, `dateNaissance`, `email`, `telephone`, `adresse`, `typePermis`, `status`, `repos`, `nbr_seance_code_atteint`, `nbr_seance_conduit_atteint`, `id_vehicule`, `montant_a_payer`) VALUES
(1, 'Sami', 'Zahrani', '1990-05-10', 'sami.zahrani@example.com', 218123456, 'Tunis, Tunisie', 'voiture', 'Actif', '', 1, 3, NULL, NULL),
(2, 'Mohamed', 'Cherif', '1988-12-15', 'mohamed.cherif@example.com', 218234567, 'Sfax, Tunisie', 'moto', 'Actif', '', 3, 2, NULL, NULL),
(3, 'Sara', 'Qatami', '1993-07-22', 'sara.qatami@example.com', 218345678, 'Bizerte, Tunisie', 'camion', 'Actif', '', 4, 2, NULL, NULL),
(4, 'Ali', 'Chabbi', '1992-01-12', 'ali.chabbi@example.com', 218456789, 'Tunis, Tunisie', 'voiture', 'Actif', '', 2, 1, NULL, NULL),
(5, 'Meriem', 'Mansouri', '1994-04-05', 'meriem.mansouri@example.com', 218567890, 'Monastir, Tunisie', 'moto', 'Actif', '', 4, 2, NULL, 0),
(7, 'Imane', 'Farhati', '1992-10-01', 'imane.farhati@example.com', 218789012, 'Gabès, Tunisie', 'voiture', 'Actif', '', 3, 2, NULL, NULL),
(8, 'Hussein', 'Ben Hussein', '1990-06-17', 'hussein.benhussein@example.com', 218890123, 'Sousse, Tunisie', 'moto', 'Actif', '', 4, 2, NULL, NULL),
(9, 'Rachida', 'Zghidi', '1989-05-11', 'rachida.zghidi@example.com', 218901234, 'Tunis, Tunisie', 'camion', 'Actif', '', 4, 4, NULL, NULL),
(10, 'Slim', 'Ben Abdallah', '1991-11-07', 'slim.benabdallah@example.com', 218012345, 'Kef, Tunisie', 'voiture', 'Actif', '', 4, 3, NULL, NULL),
(11, 'Marwen', 'Aouni', '1993-02-15', 'marwen.aouni@example.com', 218123456, 'Sidi Bouzid, Tunisie', 'moto', 'Actif', '', 4, 4, NULL, NULL),
(12, 'Chams', 'Makhloof', '1994-12-05', 'chams.makhloof@example.com', 218234567, 'Monastir, Tunisie', 'camion', 'Actif', '', 4, 4, NULL, NULL),
(13, 'Youssef', 'Kassimi', '1988-03-20', 'youssef.kassimi@example.com', 218345678, 'Tunis, Tunisie', 'voiture', 'Actif', '', 5, 3, NULL, NULL),
(14, 'Fawzi', 'Jendoubi', '1995-09-10', 'fawzi.jendoubi@example.com', 218456789, 'Bizerte, Tunisie', 'moto', 'Actif', '', 3, 2, NULL, NULL),
(15, 'Lotfi', 'Hajji', '1991-07-12', '0', 218567890, 'Nabeul, Tunisie', 'camion', 'Actif', '', 3, 2, NULL, NULL),
(16, 'Taher', 'Fetnasi', '1990-11-18', '0', 218678901, 'Kairouan, Tunisie', 'voiture', 'Actif', '', 3, 2, NULL, NULL),
(52170, 'jawahdou', 'ja', '2025-03-08', 'uhvkj@hvlj.vç', 3535436, 'kgvkhg', 'moto', 'Actif', 'C:\\Users\\adelj\\Pictures\\Documents\\REFERENCES.docx', 0, 0, NULL, NULL),
(321654, 'yufk', 'lhb', '2025-03-20', 'jhgvjhv', 6532168, 'liuglj', 'voiture', 'Actif', 'C:\\Users\\adelj\\Pictures\\Documents\\07.pdf', 0, 0, NULL, NULL),
(14725836, 'Adel', 'khcb', '2025-04-04', 'Adel456@gmail.com', 14725836, 'Tunis', 'camion', 'Actif', '', 0, 0, NULL, 0),
(99999999, 'Ala', 'Ammari', '1990-03-01', 'alala@gmail.com', 21212121, '****', 'voiture', 'Actif', 'C:\\Users\\alaam\\Downloads\\APznzaa5diUZxfqDTz_VhaBIrQnya94AVvbMmoVyuBr3Ucbhvzfl0hISxlOEb99oeH7uu5p98dziG-HWXX0Uj0uZ7f8uAk1CQVQCyNLgnIvGLDQHYtlKSqMw1s75NNdehrw50gzJ1W-3UhaDPylgzvEKeTCsquz-arSdjtoJe2Ol6vlRES13kL7CSfUoZ5DUjlxGro9PWeSAaLbey1ev3L.pdf', 3, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `documents`
--

CREATE TABLE `documents` (
  `id` int(11) NOT NULL,
  `candidat_id` int(11) DEFAULT NULL,
  `type_doc` varchar(255) NOT NULL,
  `chemin_fichier` varchar(500) NOT NULL,
  `date_ajout` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `examen_code`
--

CREATE TABLE `examen_code` (
  `id` int(11) NOT NULL,
  `type` enum('camion','voiture','moto') NOT NULL,
  `date` timestamp NULL DEFAULT NULL,
  `duree` time NOT NULL DEFAULT '01:00:00',
  `ingenieur_id` int(11) NOT NULL,
  `candidat_id` int(11) NOT NULL,
  `price` float NOT NULL DEFAULT 100,
  `status` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `examen_code`
--

INSERT INTO `examen_code` (`id`, `type`, `date`, `duree`, `ingenieur_id`, `candidat_id`, `price`, `status`) VALUES
(69, 'voiture', '2025-04-09 09:00:00', '01:30:00', 1, 1, 250, 1),
(70, 'moto', '2025-04-10 13:30:00', '01:00:00', 2, 2, 200, 0),
(71, 'voiture', '2025-04-12 07:00:00', '02:00:00', 4, 16, 20, 1),
(72, 'voiture', '2025-04-14 09:00:00', '01:00:00', 4, 99999999, 20, 0),
(73, 'moto', '2025-04-12 09:00:00', '01:00:00', 1, 8, 20, 1),
(75, 'voiture', '2025-04-21 07:00:00', '01:00:00', 4, 99999999, 20, 0);

--
-- Triggers `examen_code`
--
DELIMITER $$
CREATE TRIGGER `after_examen_code_delete` AFTER DELETE ON `examen_code` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_examen_code_insert` AFTER INSERT ON `examen_code` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_examen_code_update` AFTER UPDATE ON `examen_code` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `examen_conduite`
--

CREATE TABLE `examen_conduite` (
  `id` int(11) NOT NULL,
  `type` enum('voiture','camion','moto') NOT NULL,
  `date_time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `duree` time NOT NULL DEFAULT '01:00:00',
  `moniteur_id` int(11) NOT NULL,
  `candidat_id` int(11) NOT NULL,
  `matricule_vehicule` int(11) DEFAULT NULL,
  `price` float NOT NULL DEFAULT 100,
  `locationX` float DEFAULT NULL,
  `locationY` float DEFAULT NULL,
  `locationName` varchar(255) DEFAULT NULL,
  `status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `examen_conduite`
--

INSERT INTO `examen_conduite` (`id`, `type`, `date_time`, `duree`, `moniteur_id`, `candidat_id`, `matricule_vehicule`, `price`, `locationX`, `locationY`, `locationName`, `status`) VALUES
(69, 'voiture', '2025-04-11 08:00:00', '02:00:00', 3, 3, 1200154, 300, 35.7749, -5.8131, 'Circuit Tanger', 1),
(72, 'moto', '2025-04-12 08:00:00', '01:30:00', 2, 5, 1237874, 20, 36.8423, 10.1884, 'شارع محمد علي عقيد, El Menzah 1, حي السلام, معتمدية حي الخضراء, Tunis, 1004, Tunisia', 1),
(73, 'voiture', '2025-04-19 07:00:00', '01:00:00', 5, 1, 1557777, 20, 36.8281, 10.1975, 'Hay El Khadra, فرحات حشاد, معتمدية حي الخضراء, Tunis, 1003, Tunisia', 0);

--
-- Triggers `examen_conduite`
--
DELIMITER $$
CREATE TRIGGER `after_examen_conduite_delete` AFTER DELETE ON `examen_conduite` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_examen_conduite_insert` AFTER INSERT ON `examen_conduite` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_examen_conduite_update` AFTER UPDATE ON `examen_conduite` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `facture`
--

CREATE TABLE `facture` (
  `id_facture` int(11) NOT NULL,
  `numero_facture` varchar(50) NOT NULL,
  `date_facture` date NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `montant` int(11) NOT NULL,
  `categorie` varchar(50) NOT NULL,
  `statut` varchar(20) DEFAULT 'Non payée',
  `date_paiement` date DEFAULT NULL,
  `chemin_scan` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `facture`
--

INSERT INTO `facture` (`id_facture`, `numero_facture`, `date_facture`, `description`, `montant`, `categorie`, `statut`, `date_paiement`, `chemin_scan`) VALUES
(1, '1', '2025-04-03', '', 125, 'Internet', 'Payée', '2025-04-04', 'ProjectJava\\src\\main\\java\\org\\example\\repository\\factures0_220bfdfab6ca1826045b587d03dc9624_MIT8_04S13_Lec02.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `ingenieurs`
--

CREATE TABLE `ingenieurs` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `dateNaissance` date DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telephone` int(50) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `vehiculeType` enum('moto','voiture','camion') DEFAULT NULL,
  `salaire` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ingenieurs`
--

INSERT INTO `ingenieurs` (`id`, `nom`, `prenom`, `dateNaissance`, `email`, `telephone`, `adresse`, `vehiculeType`, `salaire`) VALUES
(1, 'Ben Ali', 'Khaled', '1985-03-20', 'khaled.benali@example.com', 218123456, 'Tunis, Tunisie', 'moto', 5000),
(2, 'Atboubai', 'Samir', '1990-07-05', 'samir.atboubai@example.com', 218987654, 'Sousse, Tunisie', 'voiture', 4800),
(3, 'El Haj', 'Ali', '1982-12-12', 'ali.elhaj@example.com', 218345678, 'Sfax, Tunisie', 'voiture', 5200),
(4, 'Zahrani', 'Meriem', '1995-06-30', 'meriem.zahrani@example.com', 218765432, 'Monastir, Tunisie', 'voiture', 5500),
(5, 'Chazli', 'Adel', '1987-11-25', 'adel.chazli@gmail.com', 21865432, 'Kairouan, Tunisie', 'camion', 6005);

-- --------------------------------------------------------

--
-- Table structure for table `maintenances`
--

CREATE TABLE `maintenances` (
  `id` int(11) NOT NULL,
  `matricule` int(11) DEFAULT NULL,
  `date_maintenance` date DEFAULT NULL,
  `type_maintenance` varchar(50) NOT NULL,
  `price` float DEFAULT NULL,
  `repos` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `maintenances`
--

INSERT INTO `maintenances` (`id`, `matricule`, `date_maintenance`, `type_maintenance`, `price`, `repos`) VALUES
(2, 1851480, '2023-12-10', 'Brake Check', 200, ''),
(3, 1801234, NULL, 'Engine Repair', NULL, ''),
(4, 1706780, '2024-02-05', 'Tire Replacement', 150.75, ''),
(6, 1608888, NULL, 'General Inspection', NULL, ''),
(7, 1557777, '2023-11-25', 'Battery Change', 120, ''),
(11, 1239987, '2024-04-12', 'Coolant Flush', 80, ''),
(22, 1901234, '2025-02-20', 'flat tires', 2000, ''),
(27, 1350023, NULL, 'Oil change', NULL, ''),
(28, 1952345, '2025-02-28', 'akhd', 400, ''),
(32, 1608888, '2025-02-23', 'flat tires', 2500, ''),
(195, 1239987, '2020-03-15', 'Brake Check', 200, ''),
(196, 1239987, '2021-07-10', 'Oil Change', 150, ''),
(197, 1239987, '2022-06-05', 'Tire Replacement', 180, ''),
(198, 1239987, '2023-01-22', 'Battery Change', 250, ''),
(199, 1239987, '2023-09-10', 'Engine Repair', 500, ''),
(200, 1239987, '2024-02-14', 'Coolant Flush', 120, ''),
(201, 1239987, '2024-05-20', 'Suspension Fix', 300, ''),
(202, 1239987, '2024-10-02', 'Brake Fluid Change', 100, ''),
(203, 1239987, '2025-01-10', 'Wheel Alignment', 220, ''),
(204, 1239987, '2025-03-15', 'Fuel System Clean', 175, ''),
(205, 1350023, '2020-02-10', 'Flat Tires', 2000, ''),
(206, 1350023, '2021-05-18', 'General Inspection', 250, ''),
(207, 1350023, '2022-07-22', 'Brake Fluid Change', 170, ''),
(208, 1350023, '2023-02-25', 'Oil Change', 300, ''),
(209, 1350023, '2023-11-15', 'Transmission Repair', 450, ''),
(210, 1350023, '2024-04-30', 'AC Service', 280, ''),
(211, 1350023, '2024-09-05', 'Suspension Fix', 500, ''),
(212, 1350023, '2025-02-14', 'Battery Change', 190, ''),
(213, 1350023, '2025-03-05', 'Engine Repair', 600, ''),
(214, 1557777, '2020-06-08', 'Brake Check', 180, ''),
(215, 1557777, '2021-08-20', 'Coolant Flush', 130, ''),
(216, 1557777, '2022-12-01', 'Engine Repair', 420, ''),
(217, 1557777, '2023-05-17', 'Flat Tires', 2100, ''),
(218, 1557777, '2023-10-27', 'Wheel Alignment', 250, ''),
(219, 1557777, '2024-06-10', 'General Inspection', 275, ''),
(220, 1557777, '2025-01-12', 'Battery Change', 200, ''),
(221, 1608888, '2020-01-05', 'Tire Replacement', 190, ''),
(222, 1608888, '2021-03-15', 'Engine Repair', 300, ''),
(223, 1608888, '2022-07-09', 'Brake Fluid Change', 160, ''),
(224, 1608888, '2023-02-20', 'AC Service', 280, ''),
(225, 1608888, '2023-09-08', 'General Inspection', 250, ''),
(226, 1608888, '2024-03-21', 'Oil Change', 200, ''),
(227, 1608888, '2025-02-10', 'Fuel System Clean', 180, ''),
(228, 1659999, '2020-05-30', 'Coolant Flush', 110, ''),
(229, 1659999, '2021-11-20', 'Suspension Fix', 400, ''),
(230, 1659999, '2022-08-15', 'Transmission Repair', 450, ''),
(231, 1659999, '2023-04-01', 'Brake Check', 220, ''),
(232, 1659999, '2023-11-10', 'Oil Change', 180, ''),
(233, 1659999, '2024-07-05', 'Tire Replacement', 270, ''),
(234, 1659999, '2025-03-01', 'Battery Change', 300, ''),
(235, 1706780, '2020-04-10', 'Brake Fluid Change', 140, ''),
(236, 1706780, '2021-09-22', 'Wheel Alignment', 190, ''),
(237, 1706780, '2022-11-18', 'Flat Tires', 2000, ''),
(238, 1706780, '2023-07-05', 'Coolant Flush', 120, ''),
(239, 1706780, '2024-02-14', 'Engine Repair', 490, ''),
(240, 1706780, '2024-10-30', 'AC Service', 260, ''),
(241, 1706780, '2025-03-20', 'Battery Change', 220, ''),
(242, 1751548, '2020-01-25', 'General Inspection', 180, ''),
(243, 1751548, '2021-06-08', 'Tire Replacement', 300, ''),
(244, 1751548, '2022-10-15', 'Brake Check', 200, ''),
(245, 1751548, '2023-05-12', 'Engine Repair', 400, ''),
(246, 1751548, '2024-01-20', 'Oil Change', 270, ''),
(247, 1751548, '2025-02-01', 'Wheel Alignment', 250, ''),
(248, 1801234, '2020-03-22', 'Brake Fluid Change', 160, ''),
(249, 1801234, '2021-08-05', 'Coolant Flush', 150, ''),
(250, 1801234, '2022-12-20', 'Suspension Fix', 300, ''),
(251, 1801234, '2023-07-15', 'Battery Change', 250, ''),
(252, 1801234, '2024-02-22', 'Flat Tires', 2200, ''),
(253, 1801234, '2025-01-10', 'Engine Repair', 550, ''),
(254, 1851480, '2020-06-30', 'General Inspection', 200, ''),
(255, 1851480, '2021-10-20', 'Tire Replacement', 180, ''),
(256, 1851480, '2022-11-01', 'Brake Check', 190, ''),
(257, 1851480, '2023-06-15', 'Fuel System Clean', 175, ''),
(258, 1851480, '2024-04-05', 'Oil Change', 300, ''),
(259, 1851480, '2025-02-15', 'Battery Change', 220, ''),
(260, 1901234, '2020-02-28', 'Brake Fluid Change', 180, ''),
(261, 1901234, '2021-07-10', 'Engine Repair', 350, ''),
(262, 1901234, '2022-10-05', 'Suspension Fix', 420, ''),
(263, 1901234, '2023-08-25', 'Coolant Flush', 130, ''),
(264, 1901234, '2024-03-14', 'Transmission Repair', 490, ''),
(265, 1901234, '2025-01-25', 'Wheel Alignment', 200, ''),
(266, 1952345, '2020-05-01', 'Brake Check', 220, ''),
(267, 1952345, '2021-12-15', 'Flat Tires', 2100, ''),
(268, 1952345, '2022-08-07', 'Battery Change', 250, ''),
(269, 1952345, '2023-10-12', 'General Inspection', 270, ''),
(270, 1952345, '2024-05-28', 'Oil Change', 280, ''),
(271, 1952345, '2025-02-28', 'Fuel System Clean', 190, ''),
(272, 1651544, NULL, 'Oil change', NULL, NULL),
(274, 1451888, '2025-04-26', 'Oil change', 100, NULL),
(280, 1350023, NULL, 'bruh', NULL, NULL),
(281, 1350023, '2025-04-18', 'bruuuh', 120, NULL),
(282, 1204879, NULL, 'Oil change', NULL, NULL);

--
-- Triggers `maintenances`
--
DELIMITER $$
CREATE TRIGGER `update_vehicle_status_after_delete` AFTER DELETE ON `maintenances` FOR EACH ROW BEGIN
    UPDATE vehicules v
    SET v.status = CASE 
                     WHEN EXISTS (
                           SELECT 1 
                           FROM maintenances m 
                           WHERE m.matricule = v.matricule 
                             AND (m.date_maintenance IS NULL OR m.price IS NULL)
                     ) THEN 0
                     ELSE 1
                   END
    WHERE v.matricule = OLD.matricule;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_vehicle_status_after_insert` AFTER INSERT ON `maintenances` FOR EACH ROW BEGIN
    UPDATE vehicules v
    SET v.status = CASE 
                     WHEN EXISTS (
                           SELECT 1 
                           FROM maintenances m 
                           WHERE m.matricule = v.matricule 
                             AND (m.date_maintenance IS NULL OR m.price IS NULL)
                     ) THEN 0
                     ELSE 1
                   END
    WHERE v.matricule = NEW.matricule;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_vehicle_status_after_update` AFTER UPDATE ON `maintenances` FOR EACH ROW BEGIN
    UPDATE vehicules v
    SET v.status = CASE 
        WHEN EXISTS (
            SELECT 1 
            FROM maintenances m 
            WHERE m.matricule = v.matricule 
              AND (m.date_maintenance IS NULL OR m.price IS NULL)
        ) THEN 0
        ELSE 1
    END;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `moniteur`
--

CREATE TABLE `moniteur` (
  `idMoniteur` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `dateNaissance` date DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `telephone` int(15) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `vehiculeType` enum('moto','camion','voiture') NOT NULL,
  `salaire` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `moniteur`
--

INSERT INTO `moniteur` (`idMoniteur`, `nom`, `prenom`, `dateNaissance`, `email`, `telephone`, `adresse`, `vehiculeType`, `salaire`) VALUES
(1, 'Ben Ali', 'Khaled', '1985-03-20', 'khaled.benali@example.com', 218123456, 'Tunis, Tunisie', 'moto', 3200000),
(2, 'Atboubai', 'Samir', '1990-07-05', 'samir.taboubi@example.com', 218987654, 'Sousse, Tunisie', 'moto', 3200000),
(3, 'El Haj', 'Ali', '1982-12-12', 'ali.haj@example.com', 218345678, 'sfax, Tunisie', 'moto', 3200000),
(4, 'Zahrani', 'Meriem', '1995-06-30', 'meriem.zahrani@example.com', 218765432, 'Monastir, Tunisie', 'moto', 3200000),
(5, 'Chazli', 'Adel', '1987-11-25', 'adel.chazli@example.com', 218654321, 'Kairouan, Tunisie', 'voiture', 3200000),
(6, 'Cherif', 'Sami', '1980-01-15', 'sami.cherif@example.com', 218789654, 'Nabeul, Tunisie', 'voiture', 3200000),
(7, 'Ben Hussein', 'Hussein', '1992-04-22', 'hussein.benhessein@example.com', 218321654, 'sousse, Tunisie', 'voiture', 3200000),
(8, 'Qatami', 'Sara', '1989-09-10', 'sara.qatami@example.com', 218456789, 'Bizerte, Tunisie', 'voiture', 3200000),
(9, 'Farhati', 'Imane', '1994-08-18', 'iman.farhati@example.com', 218321987, 'Gabès, Tunisie', 'voiture', 3200000),
(10, 'Ben Abdallah', 'Slim', '1983-05-14', 'slim.benabdallah@example.com', 218654987, 'Tunis, Tunisie', 'camion', 320015);

-- --------------------------------------------------------

--
-- Table structure for table `paiements`
--

CREATE TABLE `paiements` (
  `id` int(11) NOT NULL,
  `candidat_id` int(11) NOT NULL,
  `montant` decimal(10,2) NOT NULL,
  `date_paiement` date DEFAULT curdate(),
  `etat` enum('en attente','fait') NOT NULL,
  `recu` longblob DEFAULT NULL,
  `is_facilite` tinyint(1) NOT NULL DEFAULT 0,
  `numero_tranche` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `paiements`
--

INSERT INTO `paiements` (`id`, `candidat_id`, `montant`, `date_paiement`, `etat`, `recu`, `is_facilite`, `numero_tranche`) VALUES
(1, 1, 150.00, '2025-03-01', 'fait', NULL, 0, NULL),
(2, 1, 100.00, '2025-03-15', 'fait', 0x3d3d3d3d205245c3875520444520504149454d454e54203d3d3d3d0a494420647520706169656d656e743a20320a446174653a20323032352d30332d31350a43616e64696461743a2053616d69205a616872616e690a4d6f6e74616e743a203130302e300ac3897461743a20656e20617474656e74650a2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d0a, 0, 0),
(3, 2, 200.00, '2025-03-10', 'fait', NULL, 0, NULL),
(4, 2, 50.00, '2025-04-04', 'fait', NULL, 0, 0),
(5, 3, 75.00, '2025-03-05', 'fait', NULL, 0, NULL),
(6, 4, 120.00, '2025-03-22', 'en attente', NULL, 0, NULL),
(7, 1, 200.00, '2025-04-04', 'fait', 0x3d3d3d3d205245c3875520444520504149454d454e54203d3d3d3d0a494420647520706169656d656e743a20370a446174653a20323032352d30342d30340a43616e64696461743a2053616d69205a616872616e690a4d6f6e74616e743a203230302e300ac3897461743a20666169740a2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d0a, 0, 0),
(8, 3, 50.00, '2025-04-04', 'en attente', NULL, 1, 1),
(9, 3, 50.00, '2025-05-04', 'en attente', NULL, 1, 2),
(10, 9, 166.67, '2025-04-04', 'en attente', NULL, 1, 1),
(11, 9, 166.67, '2025-05-04', 'en attente', NULL, 1, 2),
(12, 9, 166.67, '2025-04-06', 'fait', NULL, 1, 3),
(1, 1, 150.00, '2025-03-01', 'fait', NULL, 0, NULL),
(2, 1, 100.00, '2025-03-15', 'fait', 0x3d3d3d3d205245c3875520444520504149454d454e54203d3d3d3d0a494420647520706169656d656e743a20320a446174653a20323032352d30332d31350a43616e64696461743a2053616d69205a616872616e690a4d6f6e74616e743a203130302e300ac3897461743a20656e20617474656e74650a2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d0a, 0, 0),
(3, 2, 200.00, '2025-03-10', 'fait', NULL, 0, NULL),
(4, 2, 50.00, '2025-04-04', 'fait', NULL, 0, 0),
(5, 3, 75.00, '2025-03-05', 'fait', NULL, 0, NULL),
(6, 4, 120.00, '2025-03-22', 'en attente', NULL, 0, NULL),
(7, 1, 200.00, '2025-04-04', 'fait', 0x3d3d3d3d205245c3875520444520504149454d454e54203d3d3d3d0a494420647520706169656d656e743a20370a446174653a20323032352d30342d30340a43616e64696461743a2053616d69205a616872616e690a4d6f6e74616e743a203230302e300ac3897461743a20666169740a2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d0a, 0, 0),
(8, 3, 50.00, '2025-04-04', 'en attente', NULL, 1, 1),
(9, 3, 50.00, '2025-05-04', 'en attente', NULL, 1, 2),
(10, 9, 166.67, '2025-04-04', 'en attente', NULL, 1, 1),
(11, 9, 166.67, '2025-05-04', 'en attente', NULL, 1, 2),
(12, 9, 166.67, '2025-04-06', 'fait', NULL, 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `seances_code`
--

CREATE TABLE `seances_code` (
  `id` int(11) NOT NULL,
  `type` enum('camion','voiture','moto') NOT NULL,
  `date` timestamp NULL DEFAULT NULL,
  `duree` time NOT NULL DEFAULT '01:00:00',
  `ingenieur_id` int(11) NOT NULL,
  `candidat_id` int(11) NOT NULL,
  `price` float NOT NULL DEFAULT 100
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seances_code`
--

INSERT INTO `seances_code` (`id`, `type`, `date`, `duree`, `ingenieur_id`, `candidat_id`, `price`) VALUES
(21, 'camion', '2025-04-02 07:19:00', '01:00:00', 4, 5, 100),
(22, 'voiture', '2025-04-06 11:24:00', '01:00:00', 3, 11, 100),
(23, 'voiture', '2025-04-05 12:41:00', '01:00:00', 5, 16, 100),
(24, 'voiture', '2025-04-09 13:07:00', '01:00:00', 4, 2, 100),
(25, 'moto', '2025-04-04 09:55:00', '01:00:00', 1, 14, 100),
(26, 'moto', '2025-04-02 08:59:00', '01:00:00', 2, 4, 100),
(27, 'voiture', '2025-04-09 12:10:00', '01:00:00', 4, 8, 100),
(28, 'camion', '2025-04-02 07:56:00', '01:00:00', 1, 10, 100),
(29, 'camion', '2025-04-01 07:22:00', '01:00:00', 3, 13, 100),
(30, 'voiture', '2025-04-10 11:22:00', '01:00:00', 3, 12, 100),
(31, 'moto', '2025-04-04 09:33:00', '01:00:00', 2, 9, 100),
(32, 'camion', '2025-04-13 13:45:00', '01:00:00', 5, 15, 100),
(33, 'moto', '2025-04-11 08:11:00', '01:00:00', 3, 7, 100),
(34, 'voiture', '2025-04-03 10:50:00', '01:00:00', 2, 3, 100),
(36, 'voiture', '2025-04-12 11:07:00', '01:00:00', 1, 4, 100),
(37, 'moto', '2025-04-05 12:36:00', '01:00:00', 3, 10, 100),
(38, 'camion', '2025-04-08 14:00:00', '01:00:00', 4, 2, 100),
(39, 'moto', '2025-04-14 07:17:00', '01:00:00', 5, 9, 100),
(40, 'voiture', '2025-04-02 10:41:00', '01:00:00', 2, 14, 100),
(41, 'camion', '2025-04-07 11:10:00', '01:00:00', 1, 11, 100),
(42, 'moto', '2025-04-10 09:55:00', '01:00:00', 4, 8, 100),
(43, 'voiture', '2025-04-09 08:09:00', '01:00:00', 3, 5, 100),
(44, 'camion', '2025-04-04 12:47:00', '01:00:00', 5, 16, 100),
(45, 'moto', '2025-04-13 09:11:00', '01:00:00', 2, 15, 100),
(46, 'voiture', '2025-04-12 07:30:00', '01:00:00', 1, 3, 100),
(47, 'camion', '2025-04-03 13:15:00', '01:00:00', 3, 12, 100),
(48, 'moto', '2025-04-11 11:45:00', '01:00:00', 4, 13, 100),
(49, 'voiture', '2025-04-06 08:55:00', '01:00:00', 5, 7, 100),
(51, 'moto', '2025-04-14 13:07:00', '01:00:00', 3, 10, 100),
(52, 'voiture', '2025-04-02 09:44:00', '01:00:00', 4, 8, 100),
(53, 'camion', '2025-04-08 12:20:00', '01:00:00', 1, 11, 100),
(54, 'moto', '2025-04-09 11:30:00', '01:00:00', 5, 9, 100),
(55, 'voiture', '2025-04-03 13:50:00', '01:00:00', 2, 14, 100),
(56, 'camion', '2025-04-05 07:10:00', '01:00:00', 3, 5, 100),
(57, 'moto', '2025-04-01 10:45:00', '01:00:00', 4, 16, 100),
(58, 'voiture', '2025-04-10 12:35:00', '01:00:00', 1, 12, 100),
(59, 'camion', '2025-04-12 08:25:00', '01:00:00', 2, 15, 100),
(60, 'moto', '2025-04-06 13:55:00', '01:00:00', 3, 3, 100),
(61, 'voiture', '2025-04-04 07:50:00', '01:00:00', 5, 10, 100),
(62, 'camion', '2025-04-08 10:20:00', '01:00:00', 4, 2, 100),
(63, 'moto', '2025-04-07 11:15:00', '01:00:00', 1, 7, 100),
(64, 'voiture', '2025-04-02 09:05:00', '01:00:00', 2, 11, 100),
(65, 'camion', '2025-04-09 12:55:00', '01:00:00', 3, 8, 100),
(66, 'moto', '2025-04-05 08:40:00', '01:00:00', 4, 13, 100),
(67, 'voiture', '2025-04-01 13:30:00', '01:00:00', 5, 1, 100),
(68, 'camion', '2025-04-08 10:00:00', '01:00:00', 1, 13, 100),
(69, 'moto', '2025-04-10 10:30:00', '01:30:00', 1, 5, 110),
(70, 'camion', '2025-04-10 08:00:00', '01:00:00', 5, 3, 100),
(71, 'camion', '2025-04-11 12:00:00', '01:00:00', 5, 9, 20),
(72, 'camion', '2025-04-11 10:59:00', '01:00:00', 5, 12, 20),
(73, 'voiture', '2025-04-12 09:00:00', '01:00:00', 3, 13, 20),
(74, 'voiture', '2025-04-15 12:00:00', '01:00:00', 4, 99999999, 20),
(75, 'voiture', '2025-04-19 11:00:00', '01:00:00', 4, 99999999, 20),
(76, 'voiture', '2025-04-16 09:00:00', '01:00:00', 3, 99999999, 20);

--
-- Triggers `seances_code`
--
DELIMITER $$
CREATE TRIGGER `after_delete_seances_code` AFTER DELETE ON `seances_code` FOR EACH ROW BEGIN
    -- Update all candidates
    UPDATE candidat 
    SET nbr_seance_code_atteint = (
        SELECT COUNT(*) 
        FROM seances_code 
        WHERE candidat_id = candidat.CIN
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_insert_seances_code` AFTER INSERT ON `seances_code` FOR EACH ROW BEGIN
    -- Update all candidates
    UPDATE candidat 
    SET nbr_seance_code_atteint = (
        SELECT COUNT(*) 
        FROM seances_code 
        WHERE candidat_id = candidat.CIN
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_seances_code_delete` AFTER DELETE ON `seances_code` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_seances_code_insert` AFTER INSERT ON `seances_code` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_seances_code_update` AFTER UPDATE ON `seances_code` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_update_seances_code` AFTER UPDATE ON `seances_code` FOR EACH ROW BEGIN
    -- Update all candidates
    UPDATE candidat 
    SET nbr_seance_code_atteint = (
        SELECT COUNT(*) 
        FROM seances_code 
        WHERE candidat_id = candidat.CIN
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `seances_conduite`
--

CREATE TABLE `seances_conduite` (
  `idSeanceConduite` int(11) NOT NULL,
  `type` enum('voiture','camion','moto') NOT NULL,
  `date_time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `duree` time NOT NULL DEFAULT '01:00:00',
  `moniteur_id` int(11) NOT NULL,
  `candidat_id` int(11) NOT NULL,
  `matricule_vehicule` int(11) DEFAULT NULL,
  `price` float NOT NULL DEFAULT 100,
  `locationX` float DEFAULT NULL,
  `locationY` float DEFAULT NULL,
  `locationName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seances_conduite`
--

INSERT INTO `seances_conduite` (`idSeanceConduite`, `type`, `date_time`, `duree`, `moniteur_id`, `candidat_id`, `matricule_vehicule`, `price`, `locationX`, `locationY`, `locationName`) VALUES
(1, 'voiture', '2025-03-25 07:00:00', '01:00:00', 8, 1, 1851480, 100, NULL, NULL, ''),
(2, 'voiture', '2025-03-26 09:00:00', '01:00:00', 8, 2, 1608888, 100, NULL, NULL, ''),
(5, 'voiture', '2025-03-29 13:00:00', '01:00:00', 5, 5, 1751548, 100, NULL, NULL, ''),
(6, 'voiture', '2025-03-30 10:00:00', '01:00:00', 6, 7, 1901234, 100, NULL, NULL, ''),
(7, 'voiture', '2025-04-01 12:00:00', '01:00:00', 7, 8, 1557777, 100, NULL, NULL, ''),
(8, 'voiture', '2025-04-02 08:00:00', '01:00:00', 8, 9, 1706780, 100, NULL, NULL, ''),
(134, 'voiture', '2025-04-23 11:30:00', '01:00:00', 8, 10, 1851480, 100, NULL, NULL, ''),
(135, 'voiture', '2025-03-31 07:00:00', '01:00:00', 8, 11, 1608888, 100, NULL, NULL, ''),
(136, 'voiture', '2025-03-25 13:00:00', '01:00:00', 8, 12, 1851480, 100, NULL, NULL, ''),
(137, 'voiture', '2025-03-27 09:30:00', '01:00:00', 8, 13, 1608888, 100, NULL, NULL, ''),
(138, 'voiture', '2025-03-31 10:00:00', '01:00:00', 8, 14, 1851480, 100, NULL, NULL, ''),
(139, 'voiture', '2025-03-07 07:00:00', '01:00:00', 8, 15, 1608888, 100, NULL, NULL, ''),
(140, 'voiture', '2025-03-08 09:15:00', '01:00:00', 3, 16, 1851480, 100, NULL, NULL, ''),
(141, 'voiture', '2025-03-08 10:30:00', '01:00:00', 3, 1, 1608888, 100, NULL, NULL, ''),
(142, 'voiture', '2025-03-08 12:00:00', '01:00:00', 3, 2, 1608888, 100, NULL, NULL, ''),
(162, 'voiture', '2025-03-27 08:00:00', '01:00:00', 8, 10, 1851480, 100, NULL, NULL, ''),
(163, 'voiture', '2025-03-27 09:00:00', '01:00:00', 3, 11, 1608888, 100, NULL, NULL, ''),
(164, 'voiture', '2025-03-27 10:00:00', '01:00:00', 8, 12, 1851480, 100, NULL, NULL, ''),
(165, 'voiture', '2025-03-27 11:00:00', '01:00:00', 3, 13, 1608888, 100, NULL, NULL, ''),
(166, 'voiture', '2025-03-27 12:00:00', '01:00:00', 8, 14, 1851480, 100, NULL, NULL, ''),
(167, 'voiture', '2025-03-27 13:00:00', '01:00:00', 3, 15, 1608888, 100, NULL, NULL, ''),
(168, 'voiture', '2025-03-27 14:00:00', '01:00:00', 8, 16, 1851480, 100, NULL, NULL, ''),
(170, 'voiture', '2025-03-28 08:00:00', '01:00:00', 4, 3, 1751548, 100, NULL, NULL, ''),
(171, 'voiture', '2025-03-28 09:00:00', '01:00:00', 5, 4, 1901234, 100, NULL, NULL, ''),
(172, 'voiture', '2025-03-28 10:00:00', '01:00:00', 6, 5, 1557777, 100, NULL, NULL, ''),
(173, 'voiture', '2025-03-28 11:00:00', '01:00:00', 7, 1, 1706780, 100, NULL, NULL, ''),
(174, 'voiture', '2025-03-28 12:00:00', '01:00:00', 8, 7, 1851480, 100, NULL, NULL, ''),
(175, 'voiture', '2025-03-28 13:00:00', '01:00:00', 3, 8, 1608888, 100, NULL, NULL, ''),
(176, 'voiture', '2025-03-28 14:00:00', '01:00:00', 4, 9, 1751548, 100, NULL, NULL, ''),
(178, 'voiture', '2025-04-03 09:00:00', '01:00:00', 6, 11, 1557777, 100, NULL, NULL, ''),
(179, 'voiture', '2025-04-05 10:00:00', '01:00:00', 7, 12, 1706780, 100, NULL, NULL, ''),
(180, 'voiture', '2025-04-07 11:00:00', '01:00:00', 8, 13, 1851480, 100, NULL, NULL, ''),
(181, 'camion', '2025-04-07 09:30:00', '01:00:00', 10, 3, 1204879, 100, 36.8208, 10.183, 'Lycée Louis Pasteur, 4, شارع لويس براي, Les Jardins, حي الحدائق, معتمدية حي الخضراء, Tunis, 1002, Tunisia'),
(182, 'camion', '2025-04-07 12:00:00', '01:30:00', 10, 12, 1239987, 100, 36.824, 10.2056, 'البحيرة, معتمدية حي الخضراء, Tunis, 2035, Tunisia'),
(183, 'camion', '2025-04-10 07:00:00', '01:00:00', 10, 9, 1204879, 100, 36.8381, 10.2252, 'الطريق الوطنية تونس - المرسى, Hay Taeib M\'hirik, الشرقية, معتمدية حي الخضراء, Tunis, 2045, Tunisia'),
(184, 'voiture', '2025-04-10 07:00:00', '01:00:00', 5, 10, 1706780, 100, 36.8206, 10.2334, 'Difaf al Boukhaïra, البحيرة, معتمدية حي الخضراء, Tunis, 1053, Tunisia'),
(185, 'moto', '2025-04-10 09:00:00', '01:00:00', 3, 11, 1751548, 100, 36.8203, 10.1785, 'نهج القدس, الطيب المهيري, معتمدية باب بحر, Tunis, 1002, Tunisia'),
(186, 'camion', '2025-04-11 10:00:00', '01:00:00', 10, 9, 1239987, 20, 36.834, 10.226, 'Happy Land, ممر بحيرة مانيتوبا, ضفاف البحيرة, البحيرة, معتمدية حي الخضراء, Tunis, 1055, Tunisia'),
(187, 'voiture', '2025-04-14 07:00:00', '01:00:00', 9, 99999999, 1706780, 20, 36.8309, 10.2243, 'شارع الشيخ زايد, ضفاف البحيرة, البحيرة, معتمدية حي الخضراء, Tunis, 1055, Tunisia');

--
-- Triggers `seances_conduite`
--
DELIMITER $$
CREATE TRIGGER `after_delete_seances_conduit` AFTER DELETE ON `seances_conduite` FOR EACH ROW BEGIN
    -- Update all candidates
    UPDATE candidat 
    SET nbr_seance_conduit_atteint = (
        SELECT COUNT(*) 
        FROM seances_conduite 
        WHERE candidat_id = candidat.CIN
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_insert_seances_conduite` AFTER INSERT ON `seances_conduite` FOR EACH ROW BEGIN
    -- Update all candidates
    UPDATE candidat 
    SET nbr_seance_conduit_atteint = (
        SELECT COUNT(*) 
        FROM seances_conduite 
        WHERE candidat_id = candidat.CIN
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_seances_conduite_delete` AFTER DELETE ON `seances_conduite` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_seances_conduite_insert` AFTER INSERT ON `seances_conduite` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_seances_conduite_update` AFTER UPDATE ON `seances_conduite` FOR EACH ROW BEGIN
    -- Update the payment amount for all candidates
    UPDATE paiements p
    SET p.montant = (
        -- Sum from seances_conduite
        SELECT COALESCE(SUM(sc.price), 0)
        FROM seances_conduite sc
        WHERE sc.candidat_id = p.candidat_id
    ) + (
        -- Sum from seances_code
        SELECT COALESCE(SUM(scd.price), 0)
        FROM seances_code scd
        WHERE scd.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_code
        SELECT COALESCE(SUM(ec.price), 0)
        FROM examen_code ec
        WHERE ec.candidat_id = p.candidat_id
    ) + (
        -- Sum from examen_conduite
        SELECT COALESCE(SUM(ecd.price), 0)
        FROM examen_conduite ecd
        WHERE ecd.candidat_id = p.candidat_id
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_update_seances_conduite` AFTER UPDATE ON `seances_conduite` FOR EACH ROW BEGIN
    -- Update all candidates
    UPDATE candidat 
    SET nbr_seance_conduit_atteint = (
        SELECT COUNT(*) 
        FROM seances_conduite 
        WHERE candidat_id = candidat.CIN
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tarif`
--

CREATE TABLE `tarif` (
  `codevoiture` int(11) NOT NULL DEFAULT 0,
  `codemoto` int(11) NOT NULL DEFAULT 0,
  `codecamion` int(11) NOT NULL DEFAULT 0,
  `conduitvoiture` int(11) NOT NULL DEFAULT 0,
  `conduitmoto` int(11) NOT NULL DEFAULT 0,
  `conduitcamion` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tarif`
--

INSERT INTO `tarif` (`codevoiture`, `codemoto`, `codecamion`, `conduitvoiture`, `conduitmoto`, `conduitcamion`) VALUES
(20, 20, 20, 20, 20, 20),
(20, 20, 20, 20, 20, 20);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_name`, `password`) VALUES
('admin', 'admin'),
('Ala', '123');

-- --------------------------------------------------------

--
-- Table structure for table `vehicules`
--

CREATE TABLE `vehicules` (
  `matricule` int(11) NOT NULL,
  `marque` varchar(50) NOT NULL,
  `kilometer` float NOT NULL,
  `kilometer_restant` float NOT NULL,
  `status` tinyint(1) NOT NULL,
  `type` enum('moto','voiture','camion') NOT NULL,
  `date_assurance` date DEFAULT NULL,
  `prix_assurance` float NOT NULL,
  `documents` varchar(255) DEFAULT NULL,
  `factures` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicules`
--

INSERT INTO `vehicules` (`matricule`, `marque`, `kilometer`, `kilometer_restant`, `status`, `type`, `date_assurance`, `prix_assurance`, `documents`, `factures`) VALUES
(1200154, 'mazda', 250000, 5000, 1, 'camion', '2025-04-05', 1000, NULL, NULL),
(1204879, 'ford', 140200, 0, 0, 'camion', '2025-08-13', 150, NULL, NULL),
(1237874, 'honda', 12540, 1200, 1, 'moto', '2025-04-03', 1000, NULL, NULL),
(1239987, 'Isuzu', 100100, 3000, 1, 'camion', '2026-01-23', 900, NULL, NULL),
(1350023, 'opel', 546464, 0, 0, 'voiture', '2025-03-07', 5000, NULL, NULL),
(1451888, 'Tata', 784555, 0, 1, 'camion', '2025-05-02', 1400, NULL, NULL),
(1557777, 'Honda', 45000, 5000, 1, 'voiture', '2024-11-25', 620, NULL, NULL),
(1608888, 'Nissan', 500, 19500, 0, 'voiture', '2025-01-15', 550, NULL, NULL),
(1651544, 'Lada', 54870, 0, 0, 'voiture', '2025-05-10', 127, NULL, NULL),
(1659999, 'Nissan', 320000, 1000, 1, 'camion', '2025-03-19', 750, NULL, NULL),
(1706780, 'BMW', 80000, 2000, 1, 'voiture', '2024-08-30', 450, NULL, NULL),
(1751548, 'Honda', 5000, 3000, 1, 'moto', '2026-02-01', 300, NULL, NULL),
(1801234, 'Isuzu', 250777, 2000, 0, 'camion', '2025-02-01', 700, NULL, NULL),
(1851480, 'Toyota', 12000.5, 8000, 1, 'voiture', '2024-01-10', 600, NULL, NULL),
(1901234, 'Ford', 25000, 0, 1, 'voiture', '2024-05-15', 500, NULL, NULL),
(1952345, 'Mazda', 50000, 3500, 1, 'camion', '2025-06-04', 2000, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `candidat`
--
ALTER TABLE `candidat`
  ADD PRIMARY KEY (`CIN`),
  ADD KEY `fk_matricule` (`id_vehicule`);

--
-- Indexes for table `documents`
--
ALTER TABLE `documents`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_documents_candidat` (`candidat_id`);

--
-- Indexes for table `examen_code`
--
ALTER TABLE `examen_code`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_seances_code_id_candidat` (`candidat_id`),
  ADD KEY `fk_ingenieur` (`ingenieur_id`);

--
-- Indexes for table `examen_conduite`
--
ALTER TABLE `examen_conduite`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_sc_moniteur` (`moniteur_id`),
  ADD KEY `idx_sc_candidat` (`candidat_id`),
  ADD KEY `idx_sc_vehicule` (`matricule_vehicule`);

--
-- Indexes for table `facture`
--
ALTER TABLE `facture`
  ADD PRIMARY KEY (`id_facture`);

--
-- Indexes for table `ingenieurs`
--
ALTER TABLE `ingenieurs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `maintenances`
--
ALTER TABLE `maintenances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `maintenances_ibfk_1` (`matricule`);

--
-- Indexes for table `moniteur`
--
ALTER TABLE `moniteur`
  ADD PRIMARY KEY (`idMoniteur`);

--
-- Indexes for table `paiements`
--
ALTER TABLE `paiements`
  ADD KEY `fk_paiements_candidat` (`candidat_id`);

--
-- Indexes for table `seances_code`
--
ALTER TABLE `seances_code`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_seances_code_id_candidat` (`candidat_id`),
  ADD KEY `fk_ingenieur` (`ingenieur_id`);

--
-- Indexes for table `seances_conduite`
--
ALTER TABLE `seances_conduite`
  ADD PRIMARY KEY (`idSeanceConduite`),
  ADD KEY `idx_sc_moniteur` (`moniteur_id`),
  ADD KEY `idx_sc_candidat` (`candidat_id`),
  ADD KEY `idx_sc_vehicule` (`matricule_vehicule`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_name`);

--
-- Indexes for table `vehicules`
--
ALTER TABLE `vehicules`
  ADD PRIMARY KEY (`matricule`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `examen_code`
--
ALTER TABLE `examen_code`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- AUTO_INCREMENT for table `examen_conduite`
--
ALTER TABLE `examen_conduite`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;

--
-- AUTO_INCREMENT for table `facture`
--
ALTER TABLE `facture`
  MODIFY `id_facture` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `ingenieurs`
--
ALTER TABLE `ingenieurs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12547899;

--
-- AUTO_INCREMENT for table `maintenances`
--
ALTER TABLE `maintenances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=285;

--
-- AUTO_INCREMENT for table `seances_code`
--
ALTER TABLE `seances_code`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- AUTO_INCREMENT for table `seances_conduite`
--
ALTER TABLE `seances_conduite`
  MODIFY `idSeanceConduite` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=188;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `candidat`
--
ALTER TABLE `candidat`
  ADD CONSTRAINT `fk_candidat_vehicule` FOREIGN KEY (`id_vehicule`) REFERENCES `vehicules` (`matricule`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `documents`
--
ALTER TABLE `documents`
  ADD CONSTRAINT `fk_documents_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `examen_code`
--
ALTER TABLE `examen_code`
  ADD CONSTRAINT `fk_examen_code_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_examen_code_ingenieur` FOREIGN KEY (`ingenieur_id`) REFERENCES `ingenieurs` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `examen_conduite`
--
ALTER TABLE `examen_conduite`
  ADD CONSTRAINT `fk_examen_conduite_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_examen_conduite_moniteur` FOREIGN KEY (`moniteur_id`) REFERENCES `moniteur` (`idMoniteur`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_examen_conduite_vehicule` FOREIGN KEY (`matricule_vehicule`) REFERENCES `vehicules` (`matricule`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `maintenances`
--
ALTER TABLE `maintenances`
  ADD CONSTRAINT `fk_maintenances_vehicule` FOREIGN KEY (`matricule`) REFERENCES `vehicules` (`matricule`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `paiements`
--
ALTER TABLE `paiements`
  ADD CONSTRAINT `fk_paiements_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `seances_code`
--
ALTER TABLE `seances_code`
  ADD CONSTRAINT `fk_moniteur` FOREIGN KEY (`ingenieur_id`) REFERENCES `moniteur` (`idMoniteur`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_seances_code_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_seances_code_ingenieur` FOREIGN KEY (`ingenieur_id`) REFERENCES `ingenieurs` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `seances_conduite`
--
ALTER TABLE `seances_conduite`
  ADD CONSTRAINT `fk_sc_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sc_moniteur` FOREIGN KEY (`moniteur_id`) REFERENCES `moniteur` (`idMoniteur`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sc_vehicule` FOREIGN KEY (`matricule_vehicule`) REFERENCES `vehicules` (`matricule`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_seances_conduite_candidat` FOREIGN KEY (`candidat_id`) REFERENCES `candidat` (`CIN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_seances_conduite_moniteur` FOREIGN KEY (`moniteur_id`) REFERENCES `moniteur` (`idMoniteur`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_seances_conduite_vehicule` FOREIGN KEY (`matricule_vehicule`) REFERENCES `vehicules` (`matricule`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
