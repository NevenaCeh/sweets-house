/*
SQLyog Community v11.51 (64 bit)
MySQL - 5.7.14 : Database - recepti
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`recepti` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `recepti`;

/*Table structure for table `admini` */

DROP TABLE IF EXISTS `admini`;

CREATE TABLE `admini` (
  `username` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ime` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prezime` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `admini` */

insert  into `admini`(`username`,`password`,`ime`,`prezime`) values ('admin','admin','admin','admin');

/*Table structure for table `komentarinarecepte` */

DROP TABLE IF EXISTS `komentarinarecepte`;

CREATE TABLE `komentarinarecepte` (
  `komentarid` bigint(20) NOT NULL AUTO_INCREMENT,
  `tekst` text COLLATE utf8_unicode_ci,
  `ostavio` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datum` datetime DEFAULT NULL,
  `recept` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`komentarid`),
  KEY `ostavio` (`ostavio`),
  KEY `recept` (`recept`),
  CONSTRAINT `komentarinarecepte_ibfk_1` FOREIGN KEY (`ostavio`) REFERENCES `korisnici` (`username`),
  CONSTRAINT `komentarinarecepte_ibfk_2` FOREIGN KEY (`recept`) REFERENCES `recepti` (`receptID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `komentarinarecepte` */

insert  into `komentarinarecepte`(`komentarid`,`tekst`,`ostavio`,`datum`,`recept`) values (12,'Probala sam ovaj recept više puta, odličan je. Preporuka svima!','neca','2017-06-03 15:46:55',19),(13,'Odličan recept, hvala deliocu!\r\n\r\nPozdrav!','cehm','2017-06-03 16:52:39',19);

/*Table structure for table `korisnici` */

DROP TABLE IF EXISTS `korisnici`;

CREATE TABLE `korisnici` (
  `username` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ime` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prezime` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `korisnici` */

insert  into `korisnici`(`username`,`password`,`ime`,`prezime`,`email`) values ('cehm','cehm','Mirko','Ceh','cehm@eunet.com'),('neca','neca','Nevena','Ceh','necaceh@gmail.com'),('user','user','user','user','user@gmail.com'),('vericaceh','vericaceh','Verica','Ceh','verica182022@gmail.com');

/*Table structure for table `lajkovi` */

DROP TABLE IF EXISTS `lajkovi`;

CREATE TABLE `lajkovi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recept` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user` (`user`),
  KEY `recept` (`recept`),
  CONSTRAINT `lajkovi_ibfk_1` FOREIGN KEY (`user`) REFERENCES `korisnici` (`username`),
  CONSTRAINT `lajkovi_ibfk_2` FOREIGN KEY (`recept`) REFERENCES `recepti` (`receptID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `lajkovi` */

insert  into `lajkovi`(`id`,`user`,`recept`) values (19,'neca',19),(20,'cehm',19);

/*Table structure for table `recepti` */

DROP TABLE IF EXISTS `recepti`;

CREATE TABLE `recepti` (
  `receptID` bigint(20) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sastojci` text COLLATE utf8_unicode_ci,
  `opis` text COLLATE utf8_unicode_ci,
  `slika` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `napisano` date DEFAULT NULL,
  `receptod` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dozvoljeno` tinyint(1) DEFAULT NULL,
  `kratakopis` varchar(20000) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`receptID`),
  KEY `receptod` (`receptod`),
  CONSTRAINT `recepti_ibfk_1` FOREIGN KEY (`receptod`) REFERENCES `korisnici` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `recepti` */

insert  into `recepti`(`receptID`,`naziv`,`sastojci`,`opis`,`slika`,`napisano`,`receptod`,`dozvoljeno`,`kratakopis`) values (17,'Čokoladni kolač','350 gr čokolade za kuvanje, iseckane\r\n170 gr putera\r\n5 jaja \r\n1 šolja kristal šećera\r\n1,5 kašičica ekstrakta vanile\r\n1/4 kašičice kuhinjske soli\r\n1/4 šolje nezaslađenog kakaoa u prahu\r\nZa preliv:\r\n120 gr čokolade za kuvanje\r\n40 gr putera','Zagrejati rernu na 150 C, a tepsiju u kojoj ćete peći premažite lagano puterom, a sa strane pospite kakao prahom.\r\nIstopite čokoladu i maslac u mikrotalasnoj rerni, ili na pari dok ne postane glatka. U drugoj posudi mutiti jaja, šećer, vanilu, so i 2 kašike vode. Mutiti mikserom dok smesa ne postane penasta. Zatim smanjite brzinu miksera, i postepeno sipajte prethodno rastopljenu čokoladu. Povećajte brzinu i nastavite s mućenjem još oko 30 sekundi, a zatim dodajte kakao prah i mutiti dok se sve ne sjedini.\r\nSipajte testo u već pripremljenu posudu i pecite 40 – 45 minuta, a zatim ostavite da se ohladi i prebacite kolač na tacnu. Pokrijte i držite u frižideru najmanje 6 sati pre služenja ili preko noći.\r\nIstopite čokoladu i maslac, uz mešanje dok ne postane skroz glatko. Toplu glazuru sipajte preko ohlađenog kolača, a nožem izravnajte ivice. Nakon toga, stavite kolač u frižider 20 – 40 minuta, dok se ohladi. Pre služenja ostaviti kolač oko 30 minuta na sobnoj temperature da se prohladi.\r\nSAVET: Kolač secite zagrejanim nožem kako biste dobili ravne ivice kolača.','/img/recepti/thumb_Čokoladni kolač.jpg','2017-06-03','neca',1,'Jednostavno, a lepo i ukusno...'),(18,'Topla čokolada','1l mleka\r\n200g šećera\r\n100g kakaoa\r\n200g čokolade za kuvanje\r\n2dcl slatke pavlake','Kakao pomešajte sa šećerom i dodajte malo mlijeka. Preostalo mleko i slatku pavlaku kuvajte na slaboj vatri neprestano mešajući. U toplo mleko dodajte smesu šećera, kakaa i kratko prokuvajte dok se šećer ne otopi i svi sastojci ne povežu. Mešajte žicom nekoliko minuta, pa u vruću smesu dodajte čokoladu za kuvanje. Promešajte i kuvajte još nekoliko minuta. Ulite u šoljice i po želji ukrasite tučenom slatkom pavlakom.','/img/recepti/thumb_Topla čokolada.jpg','2017-06-03','neca',1,'Najlepše uživanje za decu'),(19,'Mamina čokoladna torta','piškote\r\n125g brašna\r\n5 jaja\r\n125g šećera\r\n20g kakaa\r\nSirup:\r\n150g šećera\r\n2.5dcl vode\r\nZa Fil:\r\n250g čokolade za kuvanje\r\n300g slatke pavlake\r\n3 belanca\r\n180 gr šećera\r\n40 gr vode ','Izmešati kakao i brašno. Jaja i šećer dobro umutiti u penastu masu, polako dodavati, uz lagano mešanje brašna sa kakaom. Sipati u nauljen i brašnom posut kalup za pečenje prečnika do 24cm i peći oko 30-35 minuta na 180 stepeni. Nemojte dodavati prašak za pecivo, nije potrebno jer će piškota i bez njega lepo i ravnomerno narasti, a kasnije će se preliti sirupom tako da neće biti suva. \r\n Sirup – Kuvati vodu i šećer 2 minuta i ostaviti da se skroz ohladi. \r\n Fil-Staviti šecer i vodu da prokuvaju. Kada prokuva smanjiti malo temperaturu i ostaviti da kuva i dalje jer je potrebno da se postigne temperatura oko 125 stepeni. Dok se ovaj sirup kuva umutite belanca u čvrst sneg, polako sipajte vreo sirup u umućena belanca ne prestajući da mutite mikserom velikom brzinom. Mutite jos 3-4 minuta. Dobićete jednu čvrstu penu od belanaca. U tu smesu dodati istopljenu čokoladu koja nije previše vrela, treba da bude mlaka. Dok sipate čokoladu takođe mutite mikserom, srednjom brzinom. Kada se doda čokolada, cela smesa će malo da padne, ali tako treba, nije ništa krenulo naopako. Kada se ovaj deo fila sa belancima i čokoladom ohladi, dodajte, uz lagano mešanje kašikom, čvrsto umućenu slatku pavlaku. Fil ostavite najmanje jedan sat u frižideru pre filovanja. \r\n Postupak filovanja-Pečenu piškotu izvaditi iz kalupa i ostaviti da se ohladi, zatim je iseći vodoravno da se dobiju tri kore. Prvu koru vratiti u kalup, dobro namočiti sirupom, premazati trećinom fila, staviti drugu koru, ponoviti postupak, tako i treću. Staviti u frižider 3-4 sata, izvaditi iz kalupa i ukrasiti po želji. Na ovaj način filovanja u kalupu, jer se piškota malo skupi pri hla?enju, dobi?ete tortu koja je posle vađenja iz kalupa već filovana i okolo, samo malo izravnati fil kasnije ako je potrebno. ','/img/recepti/thumb_Mamina čokoladna torta.jpg','2017-06-03','neca',1,'Idealna torta za rođendane - deca je smažu u sekundi'),(20,'forbidden','forbidden','forbidden','/img/recepti/thumb_forbidden.jpg','2017-06-03','neca',0,'forbidden'),(21,'test','test','test','/img/recepti/thumb_test.jpg','2017-06-03','cehm',0,'test'),(22,'test1','test1','test1','/img/recepti/thumb_test1.jpg','2017-06-03','neca',0,'test1');

/*Table structure for table `utisak` */

DROP TABLE IF EXISTS `utisak`;

CREATE TABLE `utisak` (
  `idUtiska` bigint(20) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `opis` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ostavio` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datum` date DEFAULT NULL,
  PRIMARY KEY (`idUtiska`),
  KEY `ostavio` (`ostavio`),
  CONSTRAINT `utisak_ibfk_1` FOREIGN KEY (`ostavio`) REFERENCES `korisnici` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `utisak` */

insert  into `utisak`(`idUtiska`,`naziv`,`opis`,`ostavio`,`datum`) values (6,'Pohvala!','Veliki izbor recepata!\r\nOduševljena sam!\r\n\r\nSamo tako nastavite!','neca','2017-06-03'),(7,'Super!','Super sajt!','user','2017-06-03');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
