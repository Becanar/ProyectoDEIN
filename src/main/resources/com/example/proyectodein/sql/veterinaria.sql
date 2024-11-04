DROP SCHEMA IF EXISTS `veterinaria`;

CREATE SCHEMA IF NOT EXISTS `veterinaria` DEFAULT CHARACTER SET latin1 COLLATE latin1_spanish_ci;

USE `veterinaria`;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;

START TRANSACTION;

-- Tabla `animales`
DROP TABLE IF EXISTS `animales`;

CREATE TABLE IF NOT EXISTS `animales` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `nombre` varchar(50) NOT NULL,
    `especie` varchar(50) NOT NULL,
    `raza` varchar(50) NULL,
    `sexo` enum('Macho', 'Hembra') NOT NULL,
    `edad` int(3) NULL,
    `peso` decimal(5,2) NULL,
    `observaciones` text NULL,
    `fecha_primera_consulta` date NOT NULL,
    `foto` blob NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `animales` (
    `nombre`, `especie`, `raza`, `sexo`, `edad`, `peso`, `observaciones`, `fecha_primera_consulta`
) VALUES
    ('Max', 'Perro', 'Labrador', 'Macho', 5, 22.50, 'Sin observaciones', '2022-03-15'),
    ('Luna', 'Gato', 'Siames', 'Hembra', 3, 5.10, 'Alergia a ciertos alimentos', '2021-06-20'),
    ('Bobby', 'Perro', 'Bulldog', 'Macho', 2, 15.30, 'Fractura reciente en la pata izquierda', '2023-01-10'),
    ('Mia', 'Gato', 'Persa', 'Hembra', 4, 4.50, 'Necesita control de peso', '2022-05-18'),
    ('Rocky', 'Perro', 'Boxer', 'Macho', 6, 30.00, 'Es muy activo', '2020-11-30');

-- Tabla `usuarios`
DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE IF NOT EXISTS `usuarios` (
    `usuario` varchar(50) NOT NULL,
    `password` varchar(50) NOT NULL,
    PRIMARY KEY (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `usuarios` (`usuario`, `password`)
VALUES
    ('admin', 'admin123'),
    ('usuario1', 'pass1234'),
    ('veterinario', 'vetsecure');

COMMIT;
