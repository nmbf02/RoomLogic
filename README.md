# RoomLogic

# Información T2 - S1

Asignación:
2) Crear una app en android Android Java en la que tenga lo siguiente: Login, Consulta a una Api-Rest (Desarrollada en Rust) para ver los datos consultados, mínimo dos mantenimiento, un proceso con uso de hilos (Carga de imagenes y busqueda de imagenes en la web o proceso de facturación en el que se imprima una factura, se guarden los datos de manera offline y online) para guardar los datos a un servidor (Back-End). (Todo esto debe usar una técnica de MVVM (Manejo de datos) y Hilos en la aplicación para actualizar los estados de nuestra App. Esta actividad nos permitirá utilizar todos los cpu del android e ios y no usar el hilo principal)

# Sistema de Gestión de Hoteles: RoomLogic

## Procesos a realizar:
## Android Java:

Login
Registrar Clientes
Registrar Habitaciones
Registrar Reservas
Consultar Clientes
Consultar Habitaciones
Consultar Reservas
Consulta de los clients que tendrán reservas el día actual
Generar un reporte y guardarlo por pdf 
Proceso con hilos: Carga de imágenes y búsqueda de imágenes en la web

------------------------------------------------------------

## Utilizar:
Procesos en paralelo para usa la CPU
Técnica de MVVM

------------------------------------------------------------

## Api-Rest:
Creada en PowerShell

------------------------------------------------------------

PhpMyAdmin

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS RoomLogic;

-- Usar la base de datos
USE RoomLogic;

-- Crear la tabla de Usuarios (Users)
CREATE TABLE IF NOT EXISTS Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY, -- ID único del usuario
    Username VARCHAR(50) UNIQUE NOT NULL, -- Nombre de usuario único
    Password VARCHAR(255) NOT NULL -- Contraseña encriptada
);

-- Crear la tabla de Clientes (Guests)
CREATE TABLE IF NOT EXISTS Guests (
    GuestID INT AUTO_INCREMENT PRIMARY KEY, -- ID único del cliente
    Name VARCHAR(100) NOT NULL, -- Nombre del cliente
    Email VARCHAR(100) UNIQUE NOT NULL, -- Correo único
    PhoneNumber VARCHAR(20), -- Número de teléfono
    Address VARCHAR(255), -- Dirección
    Status VARCHAR(10) DEFAULT 'Active' -- Estado del cliente (Active/Inactive)
);

-- Crear la tabla de Habitaciones (Rooms)
CREATE TABLE IF NOT EXISTS Rooms (
    RoomID INT AUTO_INCREMENT PRIMARY KEY, -- ID único de la habitación
    RoomNumber INT UNIQUE NOT NULL, -- Número de la habitación
    Type VARCHAR(10) NOT NULL, -- Tipo de habitación (ejemplo: Single, Double)
    PricePerNight DECIMAL(10, 2) NOT NULL, -- Precio por noche
    Status VARCHAR(15) DEFAULT 'Available' -- Estado de la habitación (Available/Occupied/Maintenance)
);

-- Crear la tabla de Reservas (Reservations)
CREATE TABLE IF NOT EXISTS Reservations (
    ReservationID INT AUTO_INCREMENT PRIMARY KEY, -- ID único de la reserva
    GuestID INT NOT NULL, -- ID del cliente
    RoomID INT NOT NULL, -- ID de la habitación
    CheckInDate DATE NOT NULL, -- Fecha de entrada
    CheckOutDate DATE NOT NULL, -- Fecha de salida
    Status VARCHAR(15) DEFAULT 'Pending', -- Estado de la reserva (Pending/Confirmed/Cancelled)
    FOREIGN KEY (GuestID) REFERENCES Guests(GuestID) ON DELETE CASCADE,
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) ON DELETE CASCADE
);

