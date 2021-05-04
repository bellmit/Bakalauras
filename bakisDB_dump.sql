SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `bakisDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `bakisDB`;

CREATE TABLE `Companies` (
  `ID` int(30) NOT NULL,
  `ticker` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(39, 'AAPL', 'Apple Inc.');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(40, 'NFLX', 'Netflix');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(41, 'DIS', 'Walt Disney Co.');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(42, 'FB', 'Facebook');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(43, 'SPOT', 'Spotify');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(44, 'GOOGL', 'Google');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(45, 'MSFT', 'Microsoft');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(46, 'AMZN', 'Amazon');
INSERT INTO `Companies` (`ID`, `ticker`, `name`) VALUES(47, 'TSLA', 'Tesla');

CREATE TABLE `Filings` (
  `ID` int(30) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ref` varchar(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `form` varchar(255) NOT NULL,
  `cik` varchar(255) NOT NULL,
  `accno` varchar(255) NOT NULL,
  `CompaniesID` int(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(48, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', NULL);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(50, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', NULL);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(51, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', NULL);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(52, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', NULL);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(53, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', 39);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(54, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', 39);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(55, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', 39);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(56, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', 39);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(57, 'filing', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', 39);
INSERT INTO `Filings` (`ID`, `name`, `ref`, `date`, `form`, `cik`, `accno`, `CompaniesID`) VALUES(58, 'filinggggggggggggggggg', 'https://filing.com', '2021.05.01', 'test', '00000123456', 'IMCKIFY', NULL);

CREATE TABLE `Modifications` (
  `ID` int(30) NOT NULL,
  `HTML` text,
  `date` varchar(255) DEFAULT NULL,
  `AnalystsID` int(30) NOT NULL,
  `ReportsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Modifications` (`ID`, `HTML`, `date`, `AnalystsID`, `ReportsID`) VALUES(37, 'LOL<></>httpssssssssssssssss', 'siandien', 10, 32);

CREATE TABLE `Notifications` (
  `ID` int(30) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `period` varchar(255) DEFAULT NULL,
  `InvestorsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Portfolios` (
  `ID` int(30) NOT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `changeValue` decimal(20,2) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Portfolios` (`ID`, `value`, `changeValue`, `date`) VALUES(1, '1', '1', '1');
INSERT INTO `Portfolios` (`ID`, `value`, `changeValue`, `date`) VALUES(2, '2', '2', '2');
INSERT INTO `Portfolios` (`ID`, `value`, `changeValue`, `date`) VALUES(3, NULL, NULL, NULL);
INSERT INTO `Portfolios` (`ID`, `value`, `changeValue`, `date`) VALUES(4, NULL, NULL, NULL);
INSERT INTO `Portfolios` (`ID`, `value`, `changeValue`, `date`) VALUES(5, NULL, NULL, NULL);

CREATE TABLE `Ratios` (
  `ID` int(30) NOT NULL,
  `formula` text NOT NULL,
  `type` varchar(255) NOT NULL,
  `AnalystsID` int(30) NOT NULL,
  `ReportsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Reports` (
  `ID` int(30) NOT NULL,
  `cik` varchar(255) NOT NULL,
  `ticker` varchar(255) NOT NULL,
  `form` varchar(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `location` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Reports` (`ID`, `cik`, `ticker`, `form`, `date`, `location`) VALUES(32, '00000123456', 'IMCKIFY', 'test', '2021.05.01', '/path/to/file');

CREATE TABLE `Requests` (
  `ID` int(30) NOT NULL,
  `ticker` varchar(255) NOT NULL,
  `form` varchar(255) NOT NULL,
  `year` int(30) NOT NULL,
  `quarter` int(30) DEFAULT NULL,
  `AnalystsID` int(30) DEFAULT NULL,
  `InvestorsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Requests` (`ID`, `ticker`, `form`, `year`, `quarter`, `AnalystsID`, `InvestorsID`) VALUES(25, 'req', 'req', 2021, 1, 10, 10);
INSERT INTO `Requests` (`ID`, `ticker`, `form`, `year`, `quarter`, `AnalystsID`, `InvestorsID`) VALUES(26, 'req', 'req', 2021, 1, 10, 10);
INSERT INTO `Requests` (`ID`, `ticker`, `form`, `year`, `quarter`, `AnalystsID`, `InvestorsID`) VALUES(27, 'un', 'un', 2021, 1, NULL, 10);
INSERT INTO `Requests` (`ID`, `ticker`, `form`, `year`, `quarter`, `AnalystsID`, `InvestorsID`) VALUES(29, 'req', 'req', 2021, 1, NULL, 10);
INSERT INTO `Requests` (`ID`, `ticker`, `form`, `year`, `quarter`, `AnalystsID`, `InvestorsID`) VALUES(30, 'req', 'req', 2021, 1, 10, 10);

CREATE TABLE `Trades` (
  `ID` int(30) NOT NULL,
  `ticker` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `price` decimal(20,2) NOT NULL,
  `currentPrice` decimal(20,2) DEFAULT NULL,
  `quantity` decimal(20,2) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `dateValid` varchar(255) DEFAULT NULL,
  `datePlaced` varchar(255) DEFAULT NULL,
  `InvestorsID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Users` (
  `ID` int(30) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `PortfoliosID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Users` (`ID`, `username`, `password`, `email`, `role`, `PortfoliosID`) VALUES(1, 'investor', 'investor', 'investor@email.com', 'Investor', 1);
INSERT INTO `Users` (`ID`, `username`, `password`, `email`, `role`, `PortfoliosID`) VALUES(10, 'test22', 'test22', 'test22', 'test22', 2);

CREATE TABLE `Watchlists` (
  `ID` int(30) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `InvestorsID` int(30) NOT NULL,
  `CompaniesID` int(30) NOT NULL,
  `ticker` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Watchlists` (`ID`, `name`, `InvestorsID`, `CompaniesID`, `ticker`) VALUES(43, 'SNP', 1, 39, 'AAPL');
INSERT INTO `Watchlists` (`ID`, `name`, `InvestorsID`, `CompaniesID`, `ticker`) VALUES(44, 'SNP', 1, 40, 'NFLX');
INSERT INTO `Watchlists` (`ID`, `name`, `InvestorsID`, `CompaniesID`, `ticker`) VALUES(46, 'SNP', 1, 40, 'NFLXXXXXX');


ALTER TABLE `Companies`
  ADD PRIMARY KEY (`ID`);

ALTER TABLE `Filings`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_Companies` (`CompaniesID`);

ALTER TABLE `Modifications`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_UsersModifications` (`AnalystsID`),
  ADD KEY `fkc_ReportsModifications` (`ReportsID`);

ALTER TABLE `Notifications`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_UsersNotifications` (`InvestorsID`);

ALTER TABLE `Portfolios`
  ADD PRIMARY KEY (`ID`);

ALTER TABLE `Ratios`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_UsersRatios` (`AnalystsID`),
  ADD KEY `fkc_ReportsRatios` (`ReportsID`);

ALTER TABLE `Reports`
  ADD PRIMARY KEY (`ID`);

ALTER TABLE `Requests`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_UsersRequestsAnalyst` (`AnalystsID`),
  ADD KEY `fkc_UsersRequestsInvestor` (`InvestorsID`);

ALTER TABLE `Trades`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_UsersTrades` (`InvestorsID`);

ALTER TABLE `Users`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_PortfoliosUsers` (`PortfoliosID`);

ALTER TABLE `Watchlists`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fkc_UsersWatchlists` (`InvestorsID`),
  ADD KEY `fkc_Watchlists` (`CompaniesID`);


ALTER TABLE `Companies`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;
ALTER TABLE `Filings`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
ALTER TABLE `Modifications`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;
ALTER TABLE `Notifications`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
ALTER TABLE `Portfolios`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
ALTER TABLE `Ratios`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
ALTER TABLE `Reports`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
ALTER TABLE `Requests`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;
ALTER TABLE `Trades`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
ALTER TABLE `Users`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
ALTER TABLE `Watchlists`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

ALTER TABLE `Filings`
  ADD CONSTRAINT `fkc_Companies` FOREIGN KEY (`CompaniesID`) REFERENCES `Companies` (`ID`);

ALTER TABLE `Modifications`
  ADD CONSTRAINT `fkc_ReportsModifications` FOREIGN KEY (`ReportsID`) REFERENCES `Reports` (`ID`),
  ADD CONSTRAINT `fkc_UsersModifications` FOREIGN KEY (`AnalystsID`) REFERENCES `Users` (`ID`);

ALTER TABLE `Notifications`
  ADD CONSTRAINT `fkc_UsersNotifications` FOREIGN KEY (`InvestorsID`) REFERENCES `Users` (`ID`);

ALTER TABLE `Ratios`
  ADD CONSTRAINT `fkc_ReportsRatios` FOREIGN KEY (`ReportsID`) REFERENCES `Reports` (`ID`),
  ADD CONSTRAINT `fkc_UsersRatios` FOREIGN KEY (`AnalystsID`) REFERENCES `Users` (`ID`);

ALTER TABLE `Requests`
  ADD CONSTRAINT `fkc_UsersRequestsAnalyst` FOREIGN KEY (`AnalystsID`) REFERENCES `Users` (`ID`),
  ADD CONSTRAINT `fkc_UsersRequestsInvestor` FOREIGN KEY (`InvestorsID`) REFERENCES `Users` (`ID`);

ALTER TABLE `Trades`
  ADD CONSTRAINT `fkc_UsersTrades` FOREIGN KEY (`InvestorsID`) REFERENCES `Users` (`ID`);

ALTER TABLE `Users`
  ADD CONSTRAINT `fkc_PortfoliosUsers` FOREIGN KEY (`PortfoliosID`) REFERENCES `Portfolios` (`ID`);

ALTER TABLE `Watchlists`
  ADD CONSTRAINT `fkc_UsersWatchlists` FOREIGN KEY (`InvestorsID`) REFERENCES `Users` (`ID`),
  ADD CONSTRAINT `fkc_Watchlists` FOREIGN KEY (`CompaniesID`) REFERENCES `Companies` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
