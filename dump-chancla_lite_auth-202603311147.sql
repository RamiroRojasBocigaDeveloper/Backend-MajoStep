/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: chancla_lite_auth
-- ------------------------------------------------------
-- Server version	11.8.3-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES
(1,'Niño','2026-03-15 00:06:43'),
(2,'Niña','2026-03-15 00:06:43'),
(3,'Mujer','2026-03-15 00:06:43'),
(4,'Hombre','2026-03-15 00:06:43');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias_gastos`
--

DROP TABLE IF EXISTS `categorias_gastos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_gastos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias_gastos`
--

LOCK TABLES `categorias_gastos` WRITE;
/*!40000 ALTER TABLE `categorias_gastos` DISABLE KEYS */;
INSERT INTO `categorias_gastos` VALUES
(1,'arriendo','2026-03-15 00:06:43'),
(2,'servicios','2026-03-15 00:06:43'),
(3,'nomina','2026-03-15 00:06:43'),
(4,'otros','2026-03-15 00:06:43');
/*!40000 ALTER TABLE `categorias_gastos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_venta`
--

DROP TABLE IF EXISTS `detalles_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalles_venta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `venta_id` bigint(20) unsigned NOT NULL,
  `producto_id` bigint(20) unsigned NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` double NOT NULL,
  `costo_unitario` double NOT NULL,
  `subtotal_item` double DEFAULT NULL,
  `margen_item` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_detalles_producto` (`producto_id`),
  KEY `idx_detalles_venta_venta_producto` (`venta_id`,`producto_id`),
  CONSTRAINT `fk_detalles_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `fk_detalles_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_detalle` CHECK (`cantidad` > 0 and `precio_unitario` >= 0 and `costo_unitario` >= 0)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_venta`
--

LOCK TABLES `detalles_venta` WRITE;
/*!40000 ALTER TABLE `detalles_venta` DISABLE KEYS */;
INSERT INTO `detalles_venta` VALUES
(1,1,1,1,31000,15500,NULL,NULL,'2026-03-30 00:24:18','2026-03-30 00:24:18'),
(3,3,1,1,31000,15500,NULL,NULL,'2026-03-30 00:32:08','2026-03-30 00:32:08'),
(5,5,1,1,31000,15500,NULL,NULL,'2026-03-31 13:44:53','2026-03-31 13:44:53'),
(9,9,1,1,31000,15500,NULL,NULL,'2026-03-31 15:59:30','2026-03-31 15:59:30'),
(12,12,1,2,31000,15500,NULL,NULL,'2026-03-31 16:19:25','2026-03-31 16:19:25'),
(17,17,1,2,31000,15500,NULL,NULL,'2026-03-31 16:21:29','2026-03-31 16:21:29');
/*!40000 ALTER TABLE `detalles_venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_detalle_venta_ai
AFTER INSERT ON detalles_venta
FOR EACH ROW
BEGIN
  INSERT INTO movimientos_inventario (producto_id, tipo, cantidad, motivo, referencia_id)
  VALUES (NEW.producto_id, 'salida', NEW.cantidad, 'Venta', NEW.venta_id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_detalle_venta_au
AFTER UPDATE ON detalles_venta
FOR EACH ROW
BEGIN
  IF OLD.producto_id = NEW.producto_id THEN
    IF NEW.cantidad > OLD.cantidad THEN
      INSERT INTO movimientos_inventario (producto_id, tipo, cantidad, motivo, referencia_id)
      VALUES (NEW.producto_id, 'salida', NEW.cantidad - OLD.cantidad, 'Ajuste venta', NEW.venta_id);
    ELSEIF NEW.cantidad < OLD.cantidad THEN
      INSERT INTO movimientos_inventario (producto_id, tipo, cantidad, motivo, referencia_id)
      VALUES (NEW.producto_id, 'entrada', OLD.cantidad - NEW.cantidad, 'Ajuste venta', NEW.venta_id);
    END IF;
  ELSE
    INSERT INTO movimientos_inventario (producto_id, tipo, cantidad, motivo, referencia_id)
    VALUES (OLD.producto_id, 'entrada', OLD.cantidad, 'Cambio de producto en venta', NEW.venta_id);
    INSERT INTO movimientos_inventario (producto_id, tipo, cantidad, motivo, referencia_id)
    VALUES (NEW.producto_id, 'salida', NEW.cantidad, 'Cambio de producto en venta', NEW.venta_id);
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_detalle_venta_ad
AFTER DELETE ON detalles_venta
FOR EACH ROW
BEGIN
  INSERT INTO movimientos_inventario (producto_id, tipo, cantidad, motivo, referencia_id)
  VALUES (OLD.producto_id, 'entrada', OLD.cantidad, 'Reverso venta', OLD.venta_id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary table structure for view `estado_stock`
--

DROP TABLE IF EXISTS `estado_stock`;
/*!50001 DROP VIEW IF EXISTS `estado_stock`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `estado_stock` AS SELECT
 1 AS `id`,
  1 AS `nombre`,
  1 AS `referencia`,
  1 AS `categoria`,
  1 AS `stock_actual`,
  1 AS `stock_minimo`,
  1 AS `estado` */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gastos`
--

DROP TABLE IF EXISTS `gastos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `gastos` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sesion_id` bigint(20) unsigned NOT NULL,
  `categoria_gasto_id` int(10) unsigned NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `monto` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_gastos_categoria` (`categoria_gasto_id`),
  KEY `idx_gastos_sesion_categoria` (`sesion_id`,`categoria_gasto_id`),
  CONSTRAINT `fk_gastos_categoria` FOREIGN KEY (`categoria_gasto_id`) REFERENCES `categorias_gastos` (`id`),
  CONSTRAINT `fk_gastos_sesion` FOREIGN KEY (`sesion_id`) REFERENCES `sesiones_trabajo` (`id`),
  CONSTRAINT `chk_monto` CHECK (`monto` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gastos`
--

LOCK TABLES `gastos` WRITE;
/*!40000 ALTER TABLE `gastos` DISABLE KEYS */;
/*!40000 ALTER TABLE `gastos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metodos_pago`
--

DROP TABLE IF EXISTS `metodos_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `metodos_pago` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metodos_pago`
--

LOCK TABLES `metodos_pago` WRITE;
/*!40000 ALTER TABLE `metodos_pago` DISABLE KEYS */;
INSERT INTO `metodos_pago` VALUES
(1,'efectivo','2026-03-15 00:06:43'),
(2,'tarjeta','2026-03-15 00:06:43'),
(3,'transferencia','2026-03-15 00:06:43');
/*!40000 ALTER TABLE `metodos_pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimientos_inventario`
--

DROP TABLE IF EXISTS `movimientos_inventario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimientos_inventario` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) unsigned NOT NULL,
  `tipo` enum('entrada','salida','ajuste') NOT NULL,
  `cantidad` int(11) NOT NULL,
  `motivo` varchar(100) NOT NULL,
  `referencia_id` bigint(20) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_movimientos_producto_fecha` (`producto_id`,`created_at`),
  KEY `idx_movimientos_referencia` (`referencia_id`),
  CONSTRAINT `fk_mov_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `fk_mov_ref_venta` FOREIGN KEY (`referencia_id`) REFERENCES `ventas` (`id`) ON DELETE SET NULL,
  CONSTRAINT `chk_movimiento` CHECK (`cantidad` > 0)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimientos_inventario`
--

LOCK TABLES `movimientos_inventario` WRITE;
/*!40000 ALTER TABLE `movimientos_inventario` DISABLE KEYS */;
INSERT INTO `movimientos_inventario` VALUES
(1,1,'salida',1,'VENTA VEN-20260329-192418-63E0',NULL,'2026-03-30 00:24:18'),
(2,1,'salida',1,'Venta',1,'2026-03-30 00:24:18'),
(5,1,'salida',1,'VENTA VEN-20260329-193208-15E0',NULL,'2026-03-30 00:32:08'),
(6,1,'salida',1,'Venta',3,'2026-03-30 00:32:08'),
(9,1,'salida',1,'VENTA VEN-20260331-084453-8415',NULL,'2026-03-31 13:44:53'),
(10,1,'salida',1,'Venta',5,'2026-03-31 13:44:53'),
(12,1,'entrada',4,'Factura 22',NULL,'2026-03-31 13:47:15'),
(19,1,'salida',1,'VENTA VEN-20260331-105930-A637',NULL,'2026-03-31 15:59:30'),
(20,1,'salida',1,'Venta',9,'2026-03-31 15:59:30'),
(25,1,'salida',2,'VENTA VEN-20260331-111925-6C58',NULL,'2026-03-31 16:19:25'),
(26,1,'salida',2,'Venta',12,'2026-03-31 16:19:25'),
(27,1,'entrada',5,'Compra',NULL,'2026-03-31 16:20:41'),
(36,1,'salida',2,'VENTA VEN-20260331-112129-FE3C',NULL,'2026-03-31 16:21:29'),
(37,1,'salida',2,'Venta',17,'2026-03-31 16:21:29');
/*!40000 ALTER TABLE `movimientos_inventario` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_mov_inv_ai
AFTER INSERT ON movimientos_inventario
FOR EACH ROW
BEGIN
  IF NEW.tipo = 'entrada' THEN
    UPDATE productos SET stock_actual = stock_actual + NEW.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.producto_id;
  ELSE
    -- Maneja 'salida' y 'ajuste' como reducción de stock
    UPDATE productos SET stock_actual = stock_actual - NEW.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.producto_id;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_mov_inv_au
AFTER UPDATE ON movimientos_inventario
FOR EACH ROW
BEGIN
  -- 1. Revertir efecto del estado anterior (OLD)
  IF OLD.tipo = 'entrada' THEN
    UPDATE productos SET stock_actual = stock_actual - OLD.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.producto_id;
  ELSE
    UPDATE productos SET stock_actual = stock_actual + OLD.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.producto_id;
  END IF;
  
  -- 2. Aplicar efecto del nuevo estado (NEW)
  IF NEW.tipo = 'entrada' THEN
    UPDATE productos SET stock_actual = stock_actual + NEW.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.producto_id;
  ELSE
    UPDATE productos SET stock_actual = stock_actual - NEW.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.producto_id;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_mov_inv_ad
AFTER DELETE ON movimientos_inventario
FOR EACH ROW
BEGIN
  IF OLD.tipo = 'entrada' THEN
    UPDATE productos SET stock_actual = stock_actual - OLD.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.producto_id;
  ELSE
    -- Revierte 'salida' y 'ajuste' sumando de nuevo al stock
    UPDATE productos SET stock_actual = stock_actual + OLD.cantidad, updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.producto_id;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `referencia` varchar(50) NOT NULL,
  `categoria_id` int(10) unsigned NOT NULL,
  `precio_compra` double NOT NULL,
  `precio_venta` double NOT NULL,
  `stock_actual` int(11) NOT NULL DEFAULT 0,
  `stock_minimo` int(11) NOT NULL DEFAULT 5,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_productos_referencia` (`referencia`),
  KEY `idx_productos_categoria_activo` (`categoria_id`,`activo`),
  KEY `idx_productos_referencia` (`referencia`),
  CONSTRAINT `fk_productos_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `chk_precios` CHECK (`precio_compra` >= 0 and `precio_venta` >= 0 and `precio_venta` >= `precio_compra`),
  CONSTRAINT `chk_stock` CHECK (`stock_actual` >= 0 and `stock_minimo` >= 0)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES
(1,'Sandalia Xirella','Leonela',3,15500,31000,1,1,1,'2026-03-28 15:34:29','2026-03-31 16:21:29');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `reporte_inventario`
--

DROP TABLE IF EXISTS `reporte_inventario`;
/*!50001 DROP VIEW IF EXISTS `reporte_inventario`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `reporte_inventario` AS SELECT
 1 AS `referencia`,
  1 AS `producto`,
  1 AS `categoria`,
  1 AS `tipo`,
  1 AS `cantidad`,
  1 AS `motivo`,
  1 AS `fecha_movimiento`,
  1 AS `stock_actual`,
  1 AS `referencia_movimiento` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `resumen_diario`
--

DROP TABLE IF EXISTS `resumen_diario`;
/*!50001 DROP VIEW IF EXISTS `resumen_diario`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `resumen_diario` AS SELECT
 1 AS `sesion_id`,
  1 AS `fecha`,
  1 AS `empleado`,
  1 AS `rol`,
  1 AS `venta_bruta`,
  1 AS `margen_dia`,
  1 AS `gastos_dia`,
  1 AS `resultado_neto`,
  1 AS `hora_inicio`,
  1 AS `hora_fin`,
  1 AS `total_ventas` */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES
(1,'vendedor','2026-03-15 00:06:43'),
(2,'administrador','2026-03-15 00:06:43'),
(3,'jefe','2026-03-15 00:06:43');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesiones_trabajo`
--

DROP TABLE IF EXISTS `sesiones_trabajo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesiones_trabajo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint(20) unsigned NOT NULL,
  `hora_inicio` datetime NOT NULL,
  `hora_fin` datetime DEFAULT NULL,
  `fecha` date GENERATED ALWAYS AS (cast(`hora_inicio` as date)) STORED,
  `estado` varchar(20) NOT NULL,
  `rol_usuario` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_sesiones_usuario_fecha` (`usuario_id`,`fecha`),
  KEY `idx_sesiones_estado_fecha` (`estado`,`fecha`),
  CONSTRAINT `fk_sesion_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `chk_horario` CHECK (`hora_fin` is null or `hora_fin` >= `hora_inicio`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesiones_trabajo`
--

LOCK TABLES `sesiones_trabajo` WRITE;
/*!40000 ALTER TABLE `sesiones_trabajo` DISABLE KEYS */;
INSERT INTO `sesiones_trabajo` VALUES
(1,2,'2026-03-29 19:23:51','2026-03-29 19:24:29','2026-03-29','CERRADA','administrador','2026-03-30 00:23:51','2026-03-30 00:24:29'),
(2,2,'2026-03-29 19:24:32','2026-03-29 19:24:41','2026-03-29','CERRADA','administrador','2026-03-30 00:24:32','2026-03-30 00:24:41'),
(3,1,'2026-03-29 19:31:36','2026-03-29 19:32:35','2026-03-29','CERRADA','administrador','2026-03-30 00:31:36','2026-03-30 00:32:35'),
(4,1,'2026-03-29 19:32:39','2026-03-29 19:32:44','2026-03-29','CERRADA','administrador','2026-03-30 00:32:39','2026-03-30 00:32:44'),
(5,2,'2026-03-31 08:26:00','2026-03-31 10:57:32','2026-03-31','CERRADA','administrador','2026-03-31 13:26:00','2026-03-31 15:57:32'),
(6,2,'2026-03-31 10:57:49','2026-03-31 11:18:34','2026-03-31','CERRADA','administrador','2026-03-31 15:57:49','2026-03-31 16:18:34'),
(7,2,'2026-03-31 11:18:52',NULL,'2026-03-31','ABIERTA','administrador','2026-03-31 16:18:52','2026-03-31 16:18:52');
/*!40000 ALTER TABLE `sesiones_trabajo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_sesiones_trabajo_bi
BEFORE INSERT ON sesiones_trabajo
FOR EACH ROW
BEGIN
  DECLARE v_rol_nombre VARCHAR(50);
  SELECT r.nombre INTO v_rol_nombre
  FROM usuarios u
  JOIN roles r ON u.rol_id = r.id
  WHERE u.id = NEW.usuario_id;
  SET NEW.rol_usuario = v_rol_nombre;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subcategorias_gastos`
--

DROP TABLE IF EXISTS `subcategorias_gastos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategorias_gastos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  `categoria_gasto_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategorias_gastos`
--

LOCK TABLES `subcategorias_gastos` WRITE;
/*!40000 ALTER TABLE `subcategorias_gastos` DISABLE KEYS */;
/*!40000 ALTER TABLE `subcategorias_gastos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sueldos_pagados`
--

DROP TABLE IF EXISTS `sueldos_pagados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sueldos_pagados` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint(20) unsigned NOT NULL,
  `sesion_id` bigint(20) unsigned NOT NULL,
  `monto` double NOT NULL,
  `fecha_pago` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sueldo_usuario_sesion` (`usuario_id`,`sesion_id`),
  KEY `fk_sueldos_sesion` (`sesion_id`),
  KEY `idx_sueldos_usuario_fecha` (`usuario_id`,`fecha_pago`),
  CONSTRAINT `fk_sueldos_sesion` FOREIGN KEY (`sesion_id`) REFERENCES `sesiones_trabajo` (`id`),
  CONSTRAINT `fk_sueldos_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sueldos_pagados`
--

LOCK TABLES `sueldos_pagados` WRITE;
/*!40000 ALTER TABLE `sueldos_pagados` DISABLE KEYS */;
/*!40000 ALTER TABLE `sueldos_pagados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `sueldos_pendientes`
--

DROP TABLE IF EXISTS `sueldos_pendientes`;
/*!50001 DROP VIEW IF EXISTS `sueldos_pendientes`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `sueldos_pendientes` AS SELECT
 1 AS `sesion_id`,
  1 AS `fecha`,
  1 AS `usuario_id`,
  1 AS `usuario`,
  1 AS `rol`,
  1 AS `sueldo_diario`,
  1 AS `estado_sesion`,
  1 AS `estado_pago` */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(191) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `rol_id` tinyint(3) unsigned NOT NULL,
  `sueldo_diario` double NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `last_login_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_usuarios_email` (`email`),
  KEY `idx_usuarios_rol_activo` (`rol_id`,`activo`),
  CONSTRAINT `fk_usuarios_roles` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `chk_sueldo_diario` CHECK (`sueldo_diario` >= 0)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES
(1,'admin@tienda.com','$2a$12$LXXDiNNpGHIn1FgTMh8kO.oZtj/zes2UfRYrCAoYbiEeq6gasvip2','Administrador Principal',2,0,1,'2026-03-15 00:06:43','2026-03-16 04:19:04',NULL),
(2,'rami@tienda.com','$2a$12$LXXDiNNpGHIn1FgTMh8kO.oZtj/zes2UfRYrCAoYbiEeq6gasvip2','Rami Test',2,0,1,'2026-03-15 00:06:43','2026-03-16 04:18:19',NULL),
(3,'admin@majostep.com','$2a$10$g0N7g.7MyiwgVgkPfOtkmuyavuk/9eWsqamBeQVwiDFi9RMOTEeDK','Administrador MajoStep',1,35000,1,'2026-03-24 15:52:10','2026-03-28 15:21:47',NULL),
(4,'majo.29.celis@gmail.com','$2a$10$Z9HfBpKyFZOsVD7DVkGpRuEWu32gqEOdV0lBQROX3.aHBqBIpARlG','Mariajose Rojas Celis',1,30000,1,'2026-03-28 18:57:50','2026-03-28 18:57:50',NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `numero_factura` varchar(30) NOT NULL,
  `sesion_id` bigint(20) unsigned NOT NULL,
  `metodo_pago_id` tinyint(3) unsigned NOT NULL,
  `subtotal` double NOT NULL,
  `descuento` double NOT NULL,
  `total` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_ventas_factura` (`numero_factura`),
  KEY `fk_ventas_metodo` (`metodo_pago_id`),
  KEY `idx_ventas_sesion_fecha` (`sesion_id`,`created_at`),
  CONSTRAINT `fk_ventas_metodo` FOREIGN KEY (`metodo_pago_id`) REFERENCES `metodos_pago` (`id`),
  CONSTRAINT `fk_ventas_sesion` FOREIGN KEY (`sesion_id`) REFERENCES `sesiones_trabajo` (`id`),
  CONSTRAINT `chk_totales` CHECK (`subtotal` >= 0 and `descuento` >= 0 and `total` >= 0 and `total` = `subtotal` - `descuento`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES
(1,'VEN-20260329-192418-63E0',1,1,31000,0,31000,'2026-03-30 00:24:18','2026-03-30 00:24:18'),
(3,'VEN-20260329-193208-15E0',3,1,31000,0,31000,'2026-03-30 00:32:08','2026-03-30 00:32:08'),
(5,'VEN-20260331-084453-8415',5,1,31000,0,31000,'2026-03-31 13:44:53','2026-03-31 13:44:53'),
(9,'VEN-20260331-105930-A637',6,1,31000,0,31000,'2026-03-31 15:59:30','2026-03-31 15:59:30'),
(12,'VEN-20260331-111925-6C58',7,1,62000,0,62000,'2026-03-31 16:19:25','2026-03-31 16:19:25'),
(17,'VEN-20260331-112129-FE3C',7,1,62000,0,62000,'2026-03-31 16:21:29','2026-03-31 16:21:29');
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'chancla_lite_auth'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `registrar_sueldo_manual` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `registrar_sueldo_manual`(
  IN p_usuario_id BIGINT UNSIGNED,
  IN p_sesion_id BIGINT UNSIGNED,
  IN p_monto DECIMAL(10,2)
)
BEGIN
  DECLARE v_fecha DATE;
  DECLARE v_categoria_nomina_id INT UNSIGNED;
  DECLARE v_nombre_usuario VARCHAR(100);
  DECLARE sueldo_ya_pagado TINYINT(1) DEFAULT 0;
  DECLARE usuario_rol VARCHAR(50);

  SELECT r.nombre INTO usuario_rol
  FROM usuarios u
  JOIN roles r ON u.rol_id = r.id
  WHERE u.id = p_usuario_id;

  IF usuario_rol NOT IN ('vendedor', 'administrador') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo vendedores y administradores reciben sueldo';
  END IF;

  SELECT fecha INTO v_fecha FROM sesiones_trabajo WHERE id = p_sesion_id;

  SELECT COUNT(*) INTO sueldo_ya_pagado
  FROM sueldos_pagados
  WHERE usuario_id = p_usuario_id AND fecha_pago = v_fecha;

  IF sueldo_ya_pagado > 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya se registró sueldo para este usuario hoy';
  END IF;

  SELECT id INTO v_categoria_nomina_id FROM categorias_gastos WHERE nombre = 'nomina';
  SELECT nombre INTO v_nombre_usuario FROM usuarios WHERE id = p_usuario_id;

  INSERT INTO sueldos_pagados (usuario_id, sesion_id, monto, fecha_pago)
  VALUES (p_usuario_id, p_sesion_id, p_monto, v_fecha);

  INSERT INTO gastos (sesion_id, categoria_gasto_id, descripcion, monto)
  VALUES (p_sesion_id, v_categoria_nomina_id, CONCAT('Sueldo diario - ', v_nombre_usuario), p_monto);

  SELECT 'Sueldo registrado correctamente' AS resultado;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `estado_stock`
--

/*!50001 DROP VIEW IF EXISTS `estado_stock`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `estado_stock` AS select `p`.`id` AS `id`,`p`.`nombre` AS `nombre`,`p`.`referencia` AS `referencia`,`c`.`nombre` AS `categoria`,`p`.`stock_actual` AS `stock_actual`,`p`.`stock_minimo` AS `stock_minimo`,case when `p`.`stock_actual` = 0 then 'sin stock' when `p`.`stock_actual` <= `p`.`stock_minimo` * 0.3 then 'crítico' when `p`.`stock_actual` <= `p`.`stock_minimo` then 'bajo' else 'normal' end AS `estado` from (`productos` `p` join `categorias` `c` on(`p`.`categoria_id` = `c`.`id`)) where `p`.`activo` = 1 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `reporte_inventario`
--

/*!50001 DROP VIEW IF EXISTS `reporte_inventario`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `reporte_inventario` AS select `p`.`referencia` AS `referencia`,`p`.`nombre` AS `producto`,`c`.`nombre` AS `categoria`,`m`.`tipo` AS `tipo`,`m`.`cantidad` AS `cantidad`,`m`.`motivo` AS `motivo`,`m`.`created_at` AS `fecha_movimiento`,`p`.`stock_actual` AS `stock_actual`,case when `m`.`referencia_id` is not null then concat('Venta #',`m`.`referencia_id`) else 'Ajuste manual' end AS `referencia_movimiento` from ((`movimientos_inventario` `m` join `productos` `p` on(`m`.`producto_id` = `p`.`id`)) join `categorias` `c` on(`p`.`categoria_id` = `c`.`id`)) order by `m`.`created_at` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `resumen_diario`
--

/*!50001 DROP VIEW IF EXISTS `resumen_diario`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `resumen_diario` AS select `s`.`id` AS `sesion_id`,`s`.`fecha` AS `fecha`,`u`.`nombre` AS `empleado`,`s`.`rol_usuario` AS `rol`,coalesce(sum(`v`.`total`),0) AS `venta_bruta`,(select coalesce(sum(`dv`.`margen_item`),0) from (`detalles_venta` `dv` join `ventas` `v2` on(`dv`.`venta_id` = `v2`.`id`)) where `v2`.`sesion_id` = `s`.`id`) AS `margen_dia`,coalesce(sum(`g`.`monto`),0) AS `gastos_dia`,coalesce(sum(`v`.`total`),0) - coalesce(sum(`g`.`monto`),0) AS `resultado_neto`,`s`.`hora_inicio` AS `hora_inicio`,`s`.`hora_fin` AS `hora_fin`,count(`v`.`id`) AS `total_ventas` from (((`sesiones_trabajo` `s` join `usuarios` `u` on(`s`.`usuario_id` = `u`.`id`)) left join `ventas` `v` on(`s`.`id` = `v`.`sesion_id`)) left join `gastos` `g` on(`s`.`id` = `g`.`sesion_id`)) group by `s`.`id`,`s`.`fecha`,`u`.`nombre`,`s`.`rol_usuario`,`s`.`hora_inicio`,`s`.`hora_fin` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sueldos_pendientes`
--

/*!50001 DROP VIEW IF EXISTS `sueldos_pendientes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `sueldos_pendientes` AS select `s`.`id` AS `sesion_id`,`s`.`fecha` AS `fecha`,`u`.`id` AS `usuario_id`,`u`.`nombre` AS `usuario`,`s`.`rol_usuario` AS `rol`,`u`.`sueldo_diario` AS `sueldo_diario`,`s`.`estado` AS `estado_sesion`,case when `sp`.`id` is null then 'pendiente' else 'pagado' end AS `estado_pago` from ((`sesiones_trabajo` `s` join `usuarios` `u` on(`s`.`usuario_id` = `u`.`id`)) left join `sueldos_pagados` `sp` on(`s`.`id` = `sp`.`sesion_id`)) where `s`.`estado` = 'cerrada' and `s`.`rol_usuario` in ('vendedor','administrador') and `u`.`sueldo_diario` > 0 and `sp`.`id` is null order by `s`.`fecha` desc,`u`.`nombre` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-03-31 11:47:23
