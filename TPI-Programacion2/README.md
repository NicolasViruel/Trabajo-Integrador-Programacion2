# Trabajo Práctico Integrador - Programación 2
# Sistema de Gestión de Empleados y Legajos

**Tecnicatura Universitaria en Programación**  
📍 *Universidad Tecnológica Nacional*  
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Este Trabajo Práctico Integrador tiene como objetivo aplicar los conceptos de Programación Orientada a Objetos, Persistencia con JDBC y Arquitectura en Capas, desarrollando un sistema de gestión de Empleados y sus Legajos.

El sistema permite realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar lógicamente) sobre las entidades Empleado y Legajo, implementando transacciones, validaciones y control de integridad referencial.

## Objetivos Académicos

Este proyecto integra los siguientes conceptos:

### 1. Arquitectura en Capas (Layered Architecture)

- Separación de responsabilidades en cuatro capas:

- Presentación (Main/UI) → Menú de consola

- Lógica de Negocio (Service) → Validaciones y transacciones

- Acceso a Datos (DAO) → Persistencia con JDBC

- Modelo (Entities) → Representación de clases del dominio

### 2. Programación Orientada a Objetos

- Aplicación de principios SOLID

- Uso de interfaces genéricas (GenericDAO, GenericService)

- Herencia mediante clase abstracta Base

- Encapsulamiento y validaciones en getters/setters

- Sobrescritura de toString(), equals() y hashCode()

### 3. Persistencia de Datos con JDBC y MySQL

- Conexión a base de datos mediante DatabaseConnection

- Patrón DAO (Data Access Object)

- Uso de PreparedStatement para evitar SQL Injection

- Manejo de transacciones (commit / rollback)

- Integridad referencial entre Empleado y Legajo (1→1)

### 4. Manejo de Recursos y Excepciones

- Uso de try-with-resources

- Implementación de TransactionManager con AutoCloseable

- Propagación controlada de excepciones

- Validaciones a nivel de base de datos y aplicación

### 5. Patrones de Diseño Implementados

- Factory Pattern → Conexión de base de datos

- Service Layer Pattern → Lógica de negocio desacoplada

- DAO Pattern → Abstracción del acceso a datos

- Soft Delete Pattern → Eliminación lógica de registros

- Funcionalidades Implementadas

- Gestión de Empleados: CRUD completo con validación de DNI único.

- Gestión de Legajos: CRUD completo con relación 1→1 hacia Empleado.

- Búsqueda por DNI o número de legajo.

- Transacciones seguras: creación o actualización de ambos objetos dentro de una misma transacción.

- Soft Delete: eliminación lógica de registros (eliminado = true).

- Rollback automático: ante fallos en operaciones compuestas.

## Requisitos del Sistema
Componente	Versión Requerida
Java JDK	17 o superior
MySQL	8.0 o superior
Gradle	8.12 (incluye wrapper)
SO	Windows / Linux / macOS

## Instalación
1️⃣ Crear Base de Datos

Ejecutar el siguiente script en MySQL Workbench:

    CREATE DATABASE IF NOT EXISTS tpiprog2_empleado_legajo;
    USE tpiprog2_empleado_legajo;
    
    -- Creacion de Tabla "legajo"
    CREATE TABLE IF NOT EXISTS legajo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,

    nro_legajo VARCHAR(20) NOT NULL UNIQUE,
    categoria VARCHAR(30),
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL,
    fecha_alta DATE,
    observaciones VARCHAR(255)
    );

    -- Creacion de Tabla "empleado"
    CREATE TABLE IF NOT EXISTS empleado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,

    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(120),
    fecha_ingreso DATE,
    area VARCHAR(50),

    legajo_id BIGINT UNIQUE,
    CONSTRAINT fk_empleado_legajo
        FOREIGN KEY (legajo_id)
        REFERENCES legajo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

2️⃣ Compilar el Proyecto
# Linux/macOS
./gradlew clean build

# Windows
    gradlew.bat clean build

3️⃣ Configurar la Conexión

Archivo: src/main/resources/database.properties

    db.url=jdbc:mysql://localhost:3306/db_empleados
    db.user=root
    db.password=

4️⃣ Ejecutar

Desde IDE (IntelliJ IDEA / Eclipse):
Ejecutar clase prog2int.Main.Main.prog2int.Main.Main.

Desde consola:

    java -cp "build/classes/java/main:<ruta-mysql-connector>" Main.Main

Uso del Sistema
Menú Principal

    ========= MENU =========
    1. Crear empleado
    2. Listar empleados
    3. Buscar empleado por DNI
    4. Actualizar empleado
    5. Eliminar empleado
    6. Crear legajo
    7. Listar legajos
    8. Buscar legajo por número
    9. Actualizar legajo
    10. Eliminar legajo
    0. Salir

### Ejemplo de Operación

## Crear empleado (con legajo asociado):

    Nombre: Ana
    Apellido: López
    DNI: 30123456
    Email: ana.lopez@empresa.com
    Área: Recursos Humanos
    ¿Desea crear un legajo para este empleado? (s/n): s
    Nro. Legajo: HR-001
    Categoría: Administrativo
    Estado (ACTIVO/INACTIVO): ACTIVO
    Fecha Alta (yyyy-mm-dd): 2023-04-01

    -- Con SQL 
    -- Insertar datos de prueba
    -- Si intentás insertar otro empleado con el mismo legajo_id, MySQL te rechazará el insert (por el UNIQUE),
    -- garantizando la relación 1→1 real.
    
    INSERT INTO legajo (nro_legajo, categoria, estado, fecha_alta)
    VALUES ('TEC-002', 'Tecnico', 'INACTIVO', '2024-06-10');
    
    INSERT INTO empleado (nombre, apellido, dni, email, fecha_ingreso, area, legajo_id)
    VALUES ('Luis', 'Pérez', '28999888', 'luis.perez@empresa.com', '2021-02-15', 'Técnica', 2);


Si alguna de las operaciones (crear legajo / crear empleado) falla, el sistema ejecuta rollback() automático.

    Modelo de Datos
    ┌────────────────────┐         ┌──────────────────┐
    │     empleados      │ 1 → 1   │     legajos      │
    ├────────────────────┤         ├──────────────────┤
    │ id (PK)            │         │ id (PK)          │
    │ nombre             │         │ nro_legajo       │
    │ apellido           │         │ categoria         │
    │ dni (UNIQUE)       │         │ estado (ENUM)     │
    │ email              │         │ fecha_alta        │
    │ area               │         │ observaciones     │
    │ legajo_id (FK)     │───┐     │ eliminado         │
    │ eliminado          │   │     └──────────────────┘
    └────────────────────┘   │
    └── Relación unidireccional (Empleado → Legajo)

## Reglas de Negocio

Un empleado solo puede tener un legajo.

DNI y número de legajo son únicos.

Eliminación lógica: los registros marcados como eliminados no se listan.

Validación de email y campos obligatorios (nombre, apellido, dni, nroLegajo).

Rollback automático si falla la creación o asociación de legajo.

Actualizaciones parciales preservan valores previos si se deja vacío.

    Arquitectura
    ┌────────────────────────────────────┐
    │   Main / UI Layer (AppMenu.java)  │
    │   → Interacción por consola        │
    └────────────────────────────────────┘
    │
    ┌─────────────▼─────────────────────┐
    │   Service Layer                   │
    │   → Validaciones y transacciones  │
    │   EmpleadoService, LegajoService  │
    └─────────────▼─────────────────────┘
    │   DAO Layer (EmpleadoDAO, etc.)   │
    │   → Acceso a BD con JDBC          │
    └─────────────▼─────────────────────┘
    │   Entities Layer (Empleado, Legajo) │
    │   → Modelo de dominio              │
    └────────────────────────────────────┘

## Ejemplo de Transacción

    try (Connection conn = DatabaseConnection.getConnection()) {
    conn.setAutoCommit(false);

    legajoDAO.crear(legajo, conn);
    empleado.setLegajo(legajo);
    empleadoDAO.crear(empleado, conn);

    conn.commit();
    } catch (Exception e) {
    conn.rollback();
    }

## Tecnologías Utilizadas

Lenguaje: Java 17+

Base de Datos: MySQL 8.0+

JDBC Driver: mysql-connector-j 8.4.0

Herramientas: IntelliJ IDEA, MySQL Workbench, Gradle

## Integrantes del Equipo

- Paola Pasallo
- Gonzalo Prados
- Maximiliano Niemec
- Nicolas Viruel
