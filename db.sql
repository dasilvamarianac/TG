--------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- Trabalho de Graduação - Mariana Cristina da Silva - Fatec Sorocaba 01/2016 --
-- -----------------------------------------------------------------------------
--              Desenvolvimento de Aplicativo para Interpretação e            --
--                  Tradução Simultânea de Linguagem de Sinais                --
-- -----------------------------------------------------------------------------
--                             Aplicativo SignsOn                             --
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- Tabela - Usuário (user)
-- -----------------------------------------------------------------------------
CREATE TABLE `user` (
  `idUser` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `status` INT(1) NOT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
-- -----------------------------------------------------------------------------
-- Tabela - Sinal (gesture)
-- -----------------------------------------------------------------------------
CREATE TABLE `gesture` (
  `idgesture` INT(11) NOT NULL,
  `symbol` VARCHAR(50) NOT NULL,
  `image` VARCHAR(500) NOT NULL,
  `type` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idgesture`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
-- -----------------------------------------------------------------------------
-- Tabela - Sinal do Usuário (usergesture)
-- -----------------------------------------------------------------------------
CREATE TABLE `usergesture` (
  `iduser` INT(11) NOT NULL,
  `idgesture` INT(11) NOT NULL,
  `sensor1` VARCHAR(10) NOT NULL,
  `sensor2` VARCHAR(10) NOT NULL,
  `sensor3` VARCHAR(10) NOT NULL,
  `sensor4` VARCHAR(10) NOT NULL,
  `sensor5` VARCHAR(10) NOT NULL,
  `sensor6` VARCHAR(10) NOT NULL,
  `sensor7` VARCHAR(10) NOT NULL,
  `sensor8` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`iduser`,`idgesture`),
  KEY `fkgesture_idx` (`idgesture`),
  CONSTRAINT `fkgesture` FOREIGN KEY (`idgesture`)
  REFERENCES `gesture` (`idgesture`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkuser` FOREIGN KEY (`iduser`)
  REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
-- ------------------------------------------------------------------------------
-- ------------------------------------------------------------------------------
