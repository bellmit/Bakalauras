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

CREATE TABLE `Modifications` (
  `ID` int(30) NOT NULL,
  `HTML` text,
  `date` varchar(255) DEFAULT NULL,
  `AnalystsID` int(30) NOT NULL,
  `ReportsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Notifications` (
  `ID` int(30) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `period` varchar(255) DEFAULT NULL,
  `InvestorsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Portfolios` (
  `ID` int(30) NOT NULL,
  `value` decimal(30,0) DEFAULT NULL,
  `changeValue` decimal(30,0) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

CREATE TABLE `Requests` (
  `ID` int(30) NOT NULL,
  `ticker` varchar(255) NOT NULL,
  `form` varchar(255) NOT NULL,
  `year` int(30) NOT NULL,
  `quarter` int(30) DEFAULT NULL,
  `AnalystsID` int(30) DEFAULT NULL,
  `InvestorsID` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Trades` (
  `ID` int(30) NOT NULL,
  `ticker` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `price` decimal(30,0) NOT NULL,
  `currentPrice` decimal(30,0) DEFAULT NULL,
  `quantity` decimal(30,0) NOT NULL,
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

CREATE TABLE `Watchlists` (
  `ID` int(30) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `InvestorsID` int(30) NOT NULL,
  `CompaniesID` int(30) NOT NULL,
  `ticker` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT;
ALTER TABLE `Portfolios`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
ALTER TABLE `Ratios`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT;
ALTER TABLE `Reports`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
ALTER TABLE `Requests`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;
ALTER TABLE `Trades`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT;
ALTER TABLE `Users`
  MODIFY `ID` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;
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
