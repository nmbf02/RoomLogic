# 🏨 RoomLogic - Sistema de Gestión de Hoteles

## 🌐 Descripción

RoomLogic es una aplicación de gestión hotelera desarrollada en Android Java, utilizando la arquitectura MVVM y procesos en paralelo para optimizar el rendimiento en dispositivos Android. Esta aplicación se conecta a una API-REST desarrollada en Rust con  ActixWeb, permitiendo consultas en tiempo real, sincronización online y offline, y el uso de notificaciones push mediante Firebase Cloud Messaging (FCM). Además, la API ha sido dockerizada y desplegada en Digital Ocean, garantizando escalabilidad y balanceo de carga.

## 🛠️ Procesos Implementados

### 👨‍🏫 Registro y Consulta:

- Login de usuarios
- Registrar Clientes
- Registrar Habitaciones
- Registrar Reservas
- Consultar Clientes
- Consultar Habitaciones
- Consultar Reservas
- Consulta de clientes con reservas en el día actual
- Generación de reportes en PDF
- Proceso con hilos: Carga y búsqueda de imágenes en la web

### 🛠️ API Backend en ActixWeb (Rust):

- Procesamiento de tareas en paralelo
- Endpoint para solicitud de tareas
- Envio de notificaciones push cuando la tarea se completa
- API dockerizada y desplegada en Digital Ocean
- Balanceo de carga y escalabilidad

### 📢 Notificaciones Push:

- Integración con Firebase Cloud Messaging (FCM)
- Notificaciones enviadas desde la API cuando una tarea se completa
- Manejo de notificaciones en la aplicación Android

## 💪 Tecnologías Utilizadas

- **Lenguaje:** Android Java, Rust
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Base de Datos:** PhpMyAdmin
- **Backend:** API-REST desarrollada en Rust (ActixWeb)
- **Sincronización:** Online y offline
- **Procesamiento:** Hilos y procesos en paralelo para mejor rendimiento en Android
- **Notificaciones Push:** Firebase Cloud Messaging (FCM)
- **Docker:** Contenerización del backend
- **Despliegue en la nube:** Digital Ocean

## 📃 Script de Base de Datos

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

## 🌟 Implementación de la API en ActixWeb

La API en Rust se encarga de manejar tareas en paralelo, enviar notificaciones push a la aplicación móvil y sincronizar datos con la base de datos.

### 🛠️ Endpoints de la API:

- `POST /execute_task` - Ejecuta una tarea en segundo plano y envía una notificación push al finalizar.
- `GET /health_check` - Verifica si la API está funcionando correctamente.

### 🏡 Despliegue en Digital Ocean

- **Dockerizado**: La API ha sido contenerizada usando Docker.
- **Escalabilidad**: Se implementó balanceo de carga para manejar múltiples solicitudes concurrentes.
- **Configuración de variables de entorno**: Se almacenó la clave de FCM en el `.env` dentro del contenedor.

## 🌟 Autor

Este sistema ha sido desarrollado por:

**Nathaly Berroa** - Desarrolladora Principal - [GitHub](https://github.com/nmbf02)

Si deseas contribuir o reportar errores, por favor abre un issue en el repositorio. 🚀✨
