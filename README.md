# 🏨 RoomLogic - Sistema de Gestión de Hoteles

## 🌐 Descripción

**RoomLogic** es una aplicación de gestión hotelera desarrollada en **Android Java**, utilizando la arquitectura **MVVM** y procesos en **paralelo** para optimizar el rendimiento en dispositivos Android. Esta aplicación se conecta a una **API-REST** desarrollada en **Rust** para manejar la sincronización de datos entre el cliente y el servidor, permitiendo consultas en tiempo real y almacenamiento tanto online como offline.

El sistema gestiona clientes, habitaciones y reservas de hotel, además de generar reportes en **PDF** y utilizar **hilos** para la carga y búsqueda de imágenes en la web, asegurando una experiencia fluida para el usuario.

## 🔄 Procesos Implementados

### 👨‍🏫 Registro y Consulta:
- Login de usuarios
- Registrar Clientes
- Registrar Habitaciones
- Registrar Reservas
- Consultar Clientes
- Consultar Habitaciones
- Consultar Reservas
- Consulta de clientes con reservas en el día actual
- Generación de reportes en **PDF**
- **Proceso con hilos**: Carga y búsqueda de imágenes en la web

## 🛠 Tecnologías Utilizadas

- **Lenguaje:** Android Java
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Base de Datos:** PhpMyAdmin
- **Backend:** API-REST desarrollada en **Rust**
- **Sincronización:** Online y offline
- **Procesamiento:** Hilos y procesos en paralelo para mejor rendimiento en Android

## 📚 Script de Base de Datos

```sql
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS RoomLogic;

-- Usar la base de datos
USE RoomLogic;

-- Crear la tabla de Usuarios (Users)
CREATE TABLE IF NOT EXISTS Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL
);

-- Crear la tabla de Clientes (Guests)
CREATE TABLE IF NOT EXISTS Guests (
    GuestID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(20),
    Address VARCHAR(255),
    Status VARCHAR(10) DEFAULT 'Active'
);

-- Crear la tabla de Habitaciones (Rooms)
CREATE TABLE IF NOT EXISTS Rooms (
    RoomID INT AUTO_INCREMENT PRIMARY KEY,
    RoomNumber INT UNIQUE NOT NULL,
    Type VARCHAR(10) NOT NULL,
    PricePerNight DECIMAL(10, 2) NOT NULL,
    Status VARCHAR(15) DEFAULT 'Available'
);

-- Crear la tabla de Reservas (Reservations)
CREATE TABLE IF NOT EXISTS Reservations (
    ReservationID INT AUTO_INCREMENT PRIMARY KEY,
    GuestID INT NOT NULL,
    RoomID INT NOT NULL,
    CheckInDate DATE NOT NULL,
    CheckOutDate DATE NOT NULL,
    Status VARCHAR(15) DEFAULT 'Pending',
    FOREIGN KEY (GuestID) REFERENCES Guests(GuestID) ON DELETE CASCADE,
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) ON DELETE CASCADE
);
```

## 💼 Autor

Este sistema ha sido desarrollado por:

- **Nathaly Berroa** - *Desarrolladora Principal* - [GitHub](https://github.com/nmbf02)

---

Si deseas contribuir o reportar errores, por favor abre un issue en el repositorio.
🚀✨
