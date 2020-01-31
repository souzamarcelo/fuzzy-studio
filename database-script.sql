/*
 Navicat Premium Data Transfer

 Source Server         : udesc
 Source Server Type    : MySQL
 Source Server Version : 50525
 Source Host           : localhost
 Source Database       : fuzzy

 Target Server Type    : MySQL
 Target Server Version : 50525
 File Encoding         : utf-8

 Date: 05/01/2013 23:12:07 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `antecedentes`
-- ----------------------------
DROP TABLE IF EXISTS `antecedentes`;
CREATE TABLE `antecedentes` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `id_variavelEntrada` int(11) NOT NULL,
  `valor` varchar(45) DEFAULT NULL,
  `id_baseRegras` int(11) NOT NULL,
  `id_termoEntrada` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `entrada` (`id_variavelEntrada`),
  KEY `regraAnt` (`id_baseRegras`),
  KEY `valorTermoEntrada` (`id_termoEntrada`),
  CONSTRAINT `entrada` FOREIGN KEY (`id_variavelEntrada`) REFERENCES `variavelEntrada` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `regraAnt` FOREIGN KEY (`id_baseRegras`) REFERENCES `baseRegras` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `valorTermoEntrada` FOREIGN KEY (`id_termoEntrada`) REFERENCES `termoLinguisticoEntrada` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `baseRegras`
-- ----------------------------
DROP TABLE IF EXISTS `baseRegras`;
CREATE TABLE `baseRegras` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `conector` varchar(4) DEFAULT NULL,
  `descricaoAntecedente` varchar(255) DEFAULT NULL,
  `descricaoConsequente` varchar(255) DEFAULT NULL,
  `id_projeto` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `projetoregra` (`id_projeto`),
  CONSTRAINT `projetoregra2` FOREIGN KEY (`id_projeto`) REFERENCES `projetos` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `consequentes`
-- ----------------------------
DROP TABLE IF EXISTS `consequentes`;
CREATE TABLE `consequentes` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `id_variavelSaida` int(11) NOT NULL,
  `valor` varchar(45) DEFAULT NULL,
  `id_baseRegras` int(11) NOT NULL,
  `id_termoSaida` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `regraCon` (`id_baseRegras`),
  KEY `saida` (`id_variavelSaida`),
  KEY `saidaDaRegra` (`id_variavelSaida`),
  KEY `valorTermoSaida` (`id_termoSaida`),
  CONSTRAINT `regraCon` FOREIGN KEY (`id_baseRegras`) REFERENCES `baseRegras` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `saidaDaRegra` FOREIGN KEY (`id_variavelSaida`) REFERENCES `variavelSaida` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `valorTermoSaida` FOREIGN KEY (`id_termoSaida`) REFERENCES `termoLinguisticoSaida` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `motores`
-- ----------------------------
DROP TABLE IF EXISTS `motores`;
CREATE TABLE `motores` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `posx` int(11) DEFAULT '250',
  `posy` int(11) DEFAULT '300',
  `id_projeto` int(11) NOT NULL,
  `metodo_defuzzificacao` varchar(45) DEFAULT NULL,
  `metodo_agregacao_regras` varchar(45) DEFAULT NULL,
  `conexao_and` varchar(45) DEFAULT NULL,
  `metodo_ativacao_regras` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `projetomotor` (`id_projeto`),
  CONSTRAINT `projetomotor` FOREIGN KEY (`id_projeto`) REFERENCES `projetos` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `motorregra`
-- ----------------------------
DROP TABLE IF EXISTS `motorregra`;
CREATE TABLE `motorregra` (
  `id_motor` int(11) NOT NULL,
  `id_regra` int(11) NOT NULL,
  PRIMARY KEY (`id_motor`,`id_regra`),
  KEY `fkregra` (`id_regra`),
  KEY `fkmotor` (`id_motor`),
  CONSTRAINT `fkmotor` FOREIGN KEY (`id_motor`) REFERENCES `motores` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkregra` FOREIGN KEY (`id_regra`) REFERENCES `baseRegras` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `projetos`
-- ----------------------------
DROP TABLE IF EXISTS `projetos`;
CREATE TABLE `projetos` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `termoLinguisticoEntrada`
-- ----------------------------
DROP TABLE IF EXISTS `termoLinguisticoEntrada`;
CREATE TABLE `termoLinguisticoEntrada` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `funcao` varchar(45) DEFAULT NULL,
  `cor` varchar(45) DEFAULT NULL,
  `valora` double DEFAULT NULL,
  `valorb` double DEFAULT NULL,
  `valorc` double DEFAULT NULL,
  `valord` double DEFAULT NULL,
  `idVariavelEntrada` int(11) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `idVariavelEntrada` (`codigo`),
  KEY `codigo` (`idVariavelEntrada`),
  CONSTRAINT `codigo` FOREIGN KEY (`idVariavelEntrada`) REFERENCES `variavelEntrada` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `termoLinguisticoSaida`
-- ----------------------------
DROP TABLE IF EXISTS `termoLinguisticoSaida`;
CREATE TABLE `termoLinguisticoSaida` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL COMMENT '	',
  `funcao` varchar(45) DEFAULT NULL,
  `cor` varchar(45) DEFAULT NULL,
  `valora` double DEFAULT NULL,
  `valorb` double DEFAULT NULL,
  `valorc` double DEFAULT NULL,
  `valord` double DEFAULT NULL,
  `idVariavelSaida` int(11) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `codigo` (`idVariavelSaida`),
  CONSTRAINT `codigovsaida` FOREIGN KEY (`idVariavelSaida`) REFERENCES `variavelSaida` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `usuarioprojeto`
-- ----------------------------
DROP TABLE IF EXISTS `usuarioprojeto`;
CREATE TABLE `usuarioprojeto` (
  `id_projeto` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  PRIMARY KEY (`id_projeto`,`id_usuario`),
  KEY `id_projeto` (`id_projeto`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `id_projeto` FOREIGN KEY (`id_projeto`) REFERENCES `projetos` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `id_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `usuarios`
-- ----------------------------
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(45) DEFAULT NULL,
  `senha` varchar(16) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `skype` varchar(45) DEFAULT NULL,
  `gtalk` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `user_UNIQUE` (`user`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `variavelEntrada`
-- ----------------------------
DROP TABLE IF EXISTS `variavelEntrada`;
CREATE TABLE `variavelEntrada` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `minimo` double DEFAULT NULL,
  `maximo` double DEFAULT NULL COMMENT '	',
  `unidade` varchar(45) DEFAULT NULL,
  `posx` int(11) DEFAULT '5',
  `posy` int(11) DEFAULT '5',
  `id_projeto` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `projetoentrada` (`id_projeto`),
  CONSTRAINT `projetoentrada` FOREIGN KEY (`id_projeto`) REFERENCES `projetos` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `variavelSaida`
-- ----------------------------
DROP TABLE IF EXISTS `variavelSaida`;
CREATE TABLE `variavelSaida` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `minimo` double DEFAULT NULL COMMENT '	',
  `maximo` double DEFAULT NULL,
  `unidade` varchar(45) DEFAULT NULL,
  `posx` int(11) DEFAULT '5',
  `posy` int(11) DEFAULT '5',
  `id_projeto` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `projetosaida` (`id_projeto`),
  CONSTRAINT `projetosaida` FOREIGN KEY (`id_projeto`) REFERENCES `projetos` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
