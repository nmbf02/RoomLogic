use actix_web::{web, App, HttpServer, Responder, HttpResponse};
use sqlx::mysql::MySqlPool;
use serde::{Deserialize, Serialize};
use std::env;
use dotenvy::dotenv;
use serde_json::json;
use chrono::Utc;

// Nathaly Berroa

// Modelos de datos - Login
#[derive(Deserialize, Serialize)]
struct LoginRequest {
    username: String,
    password: String,
}

// Validacion
#[derive(Serialize)]
struct LoginResponse {
    success: bool,
    message: String,
    token: Option<String>,
}

// Modelos de datos - Rooms
#[derive(Deserialize, Serialize)]
struct Room {
    id: Option<i32>,
    #[serde(rename = "room_number")]
    room_number: String,  // Cambia de i32 a String si la API recibe el número como string
    #[serde(rename = "room_type")]
    room_type: String,
    status: String,
}

// Modelos de datos - Guests
#[derive(Deserialize, Serialize)]
struct Client {
    id: Option<i32>,
    name: String,
    email: String,
    #[serde(alias = "PhoneNumber", rename = "phone_number")] // Acepta PhoneNumber también
    phone_number: String,
    address: String,
    status: String,
}

// Modelos de datos - Reservations
#[derive(Deserialize, Serialize)]
struct Reservation {
    id: Option<i32>,
    #[serde(rename = "guest_id")]
    guest_id: i32,
    #[serde(rename = "room_id")]
    room_id: i32,
    #[serde(rename = "check_in_date")]
    check_in_date: String,
    #[serde(rename = "check_out_date")]
    check_out_date: String,
    status: String,
}

// Handlers

//< ------------------------------ Login ------------------------------ >
// Handler para el login
async fn login_handler(
    login_data: web::Json<LoginRequest>,
    pool: web::Data<MySqlPool>,
) -> impl Responder {
    let query = sqlx::query!(
        "SELECT * FROM Users WHERE Username = ? AND Password = ?",
        login_data.username,
        login_data.password
    )
    .fetch_one(pool.get_ref())
    .await;

    match query {
        Ok(_) => HttpResponse::Ok().json(LoginResponse {
            success: true,
            message: "Inicio de sesión exitoso".to_string(),
            token: Some("fake-jwt-token-123456".to_string()),
        }),
        Err(_) => HttpResponse::Unauthorized().json(LoginResponse {
            success: false,
            message: "Credenciales inválidas".to_string(),
            token: None,
        }),
    }
}

//< ------------------------------ Rooms ------------------------------ >
// Handler para crear una habitación
async fn create_room(
    pool: web::Data<MySqlPool>,
    room: web::Json<Room>,
) -> impl Responder {
    let query = "INSERT INTO rooms (RoomNumber, Type, Status) VALUES (?, ?, ?)";
    match sqlx::query(query)
        .bind(&room.room_number)
        .bind(&room.room_type)
        .bind(&room.status)
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().json(json!({"message": "Room created successfully!"})),
        Err(err) => {
            eprintln!("Error inserting room: {:?}", err);
            HttpResponse::InternalServerError().json(json!({"error": format!("Failed to create room: {:?}", err)}))
        }
    }
}

// Handler para obtener habitaciones
async fn get_rooms(pool: web::Data<MySqlPool>) -> impl Responder {
    let rooms = sqlx::query!(
        "SELECT RoomID as id, RoomNumber as room_number, Type as room_type, Status as status FROM Rooms"
    )
    .fetch_all(pool.get_ref())
    .await;

    match rooms {
        Ok(rows) => {
            let result: Vec<Room> = rows
                .into_iter()
                .map(|row| Room {
                    id: Some(row.id),
                    room_number: row.room_number.to_string(),
                    room_type: row.room_type,
                    status: row.status.unwrap_or_else(|| "Desconocido".to_string()),
                })
                .collect();

            HttpResponse::Ok().json(result)
        }
        Err(_) => HttpResponse::InternalServerError().body("Error al obtener las habitaciones"),
    }
}

// Handler para eliminar una habitación
async fn delete_room(
    pool: web::Data<MySqlPool>,
    room_id: web::Path<i32>,
) -> impl Responder {
    let query = "DELETE FROM rooms WHERE RoomID = ?";
    match sqlx::query(query)
        .bind(room_id.into_inner())
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().body("Room deleted successfully!"),
        Err(err) => {
            eprintln!("Error deleting room: {:?}", err);
            HttpResponse::InternalServerError().body("Failed to delete room")
        }
    }
}

// Handler para actualizar habitaciones
async fn update_room(
    pool: web::Data<MySqlPool>,
    room_id: web::Path<i32>,
    room: web::Json<Room>,
) -> impl Responder {
    let query = "UPDATE rooms SET RoomNumber = ?, Type = ?, Status = ? WHERE RoomID = ?";
    match sqlx::query(query)
        .bind(&room.room_number)
        .bind(&room.room_type)
        .bind(&room.status)
        .bind(room_id.into_inner())
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().body("Room updated successfully!"),
        Err(err) => {
            eprintln!("Error updating room: {:?}", err);
            HttpResponse::InternalServerError().body("Failed to update room")
        }
    }
}

//< ------------------------------ Guests ------------------------------ >
// Handler para crear un cliente
async fn create_client(
    pool: web::Data<MySqlPool>,
    client: web::Json<Client>,
) -> impl Responder {
    let query = "INSERT INTO guests (Name, Email, PhoneNumber, Address, Status) VALUES (?, ?, ?, ?, ?)";
    match sqlx::query(query)
        .bind(&client.name)
        .bind(&client.email)
        .bind(&client.phone_number)
        .bind(&client.address)
        .bind(&client.status)
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().body("Cliente registrado exitosamente"),
        Err(err) => {
            eprintln!("Error al registrar cliente: {:?}", err);
            HttpResponse::InternalServerError().body("Error al registrar cliente")
        }
    }
}

// Handler para obtener todos los clientes
async fn get_clients(pool: web::Data<MySqlPool>) -> impl Responder {
    let clients = sqlx::query!(
        "SELECT GuestID as id, Name as name, Email as email, PhoneNumber as phone_number, Address as address, Status as status FROM guests"
    )
    .fetch_all(pool.get_ref())
    .await;

    match clients {
        Ok(rows) => {
            let result: Vec<Client> = rows
                .into_iter()
                .map(|row| Client {
                    id: Some(row.id),
                    name: row.name,
                    email: row.email,
                    phone_number: row.phone_number.unwrap_or_default(),
                    address: row.address.unwrap_or_default(),
                    status: row.status.unwrap_or_default(),
                })
                .collect();
            HttpResponse::Ok().json(result)
        }
        Err(_) => HttpResponse::InternalServerError().body("Error al obtener los clientes"),
    }
}

// Handler para actualizar un cliente
async fn update_client(
    pool: web::Data<MySqlPool>,
    client_id: web::Path<i32>,
    client: web::Json<Client>,
) -> impl Responder {
    let query = "UPDATE guests SET Name = ?, Email = ?, PhoneNumber = ?, Address = ?, Status = ? WHERE GuestID = ?";
    match sqlx::query(query)
        .bind(&client.name)
        .bind(&client.email)
        .bind(&client.phone_number)
        .bind(&client.address)
        .bind(&client.status)
        .bind(client_id.into_inner())
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().body("Cliente actualizado exitosamente"),
        Err(err) => {
            eprintln!("Error al actualizar cliente: {:?}", err);
            HttpResponse::InternalServerError().body("No se pudo actualizar el cliente")
        }
    }
}

// Handler para eliminar un cliente
async fn delete_client(
    pool: web::Data<MySqlPool>,
    client_id: web::Path<i32>,
) -> impl Responder {
    let query = "DELETE FROM guests WHERE GuestID = ?";
    match sqlx::query(query)
        .bind(client_id.into_inner())
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().body("Cliente eliminado exitosamente"),
        Err(err) => {
            eprintln!("Error al eliminar cliente: {:?}", err);
            HttpResponse::InternalServerError().body("No se pudo eliminar el cliente")
        }
    }
}

//< ------------------------------ Reservations ------------------------------ >
// Handler para crear una reserva
async fn create_reservation(
    pool: web::Data<MySqlPool>,
    reservation: web::Json<Reservation>,
) -> impl Responder {
    let query = "INSERT INTO reservations (GuestID, RoomID, CheckInDate, CheckOutDate, Status)
                 VALUES (?, ?, ?, ?, ?)";

    match sqlx::query(query)
        .bind(&reservation.guest_id)
        .bind(&reservation.room_id)
        .bind(&reservation.check_in_date.to_string())
        .bind(&reservation.check_out_date.to_string())
        .bind(&reservation.status)
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().json(json!({"message": "Reservation created successfully!"})),
        Err(err) => {
            eprintln!("Error inserting reservation: {:?}", err);
            HttpResponse::InternalServerError().json(json!({"error": format!("Failed to create reservation: {:?}", err)}))
        }
    }
}

// Handler para obtener una reserva
async fn get_reservations(pool: web::Data<MySqlPool>) -> impl Responder {
    let reservations = sqlx::query!(
        "SELECT ReservationID as id, GuestID as guest_id, RoomID as room_id,
        DATE_FORMAT(CheckInDate, '%Y-%m-%d') as check_in_date,
        DATE_FORMAT(CheckOutDate, '%Y-%m-%d') as check_out_date,
        Status as status FROM reservations"
    )
    .fetch_all(pool.get_ref())
    .await;

    match reservations {
        Ok(rows) => {
            let result: Vec<Reservation> = rows
                .into_iter()
                .map(|row| Reservation {
                    id: Some(row.id),
                    guest_id: row.guest_id,
                    room_id: row.room_id,
                    check_in_date: row.check_in_date.unwrap_or_else(|| "0000-00-00".to_string()),
                    check_out_date: row.check_out_date.unwrap_or_else(|| "0000-00-00".to_string()),
                    status: row.status.unwrap_or_else(|| "Pending".to_string()),
                })
                .collect();

            HttpResponse::Ok().json(result)
        }
        Err(_) => HttpResponse::InternalServerError().body("Error al obtener las reservaciones"),
    }
}

// Handler para eliminar una reserva
async fn delete_reservation(
    pool: web::Data<MySqlPool>,
    reservation_id: web::Path<i32>,
) -> impl Responder {
    let query = "DELETE FROM reservations WHERE ReservationID = ?";

    match sqlx::query(query)
        .bind(reservation_id.into_inner())
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().json(json!({"message": "Reservation deleted successfully!"})),
        Err(err) => {
            eprintln!("Error deleting reservation: {:?}", err);
            HttpResponse::InternalServerError().json(json!({"error": format!("Failed to delete reservation: {:?}", err)}))
        }
    }
}

// Handler para actualizar una reserva
async fn update_reservation(
    pool: web::Data<MySqlPool>,
    reservation_id: web::Path<i32>,
    reservation: web::Json<Reservation>,
) -> impl Responder {
    let query = "UPDATE reservations SET GuestID = ?, RoomID = ?, CheckInDate = ?, CheckOutDate = ?, Status = ? WHERE ReservationID = ?";

    match sqlx::query(query)
        .bind(&reservation.guest_id)
        .bind(&reservation.room_id)
        .bind(&reservation.check_in_date.to_string())  // ✅ Convertir a String
        .bind(&reservation.check_out_date.to_string()) // ✅ Convertir a String
        .bind(&reservation.status)
        .bind(reservation_id.into_inner())
        .execute(pool.get_ref())
        .await
    {
        Ok(_) => HttpResponse::Ok().json(json!({"message": "Reservation updated successfully!"})),
        Err(err) => {
            eprintln!("Error updating reservation: {:?}", err);
            HttpResponse::InternalServerError().json(json!({"error": format!("Failed to update reservation: {:?}", err)}))
        }
    }
}

// Handler para reservas al dia de hoy
async fn get_reservations_for_today(pool: web::Data<MySqlPool>) -> impl Responder {
    let today_date = Utc::now().format("%Y-%m-%d").to_string();

    let reservations = sqlx::query!(
        "SELECT ReservationID as id, GuestID as guest_id, RoomID as room_id,
        DATE_FORMAT(CheckInDate, '%Y-%m-%d') as check_in_date,
        DATE_FORMAT(CheckOutDate, '%Y-%m-%d') as check_out_date,
        Status as status FROM reservations WHERE CheckInDate = ?",
        today_date
    )
    .fetch_all(pool.get_ref())
    .await;

    match reservations {
        Ok(rows) => {
            let result: Vec<Reservation> = rows
                .into_iter()
                .map(|row| Reservation {
                    id: Some(row.id),
                    guest_id: row.guest_id,
                    room_id: row.room_id,
                    check_in_date: row.check_in_date.unwrap_or_else(|| "0000-00-00".to_string()),
                    check_out_date: row.check_out_date.unwrap_or_else(|| "0000-00-00".to_string()),
                    status: row.status.unwrap_or_else(|| "Pending".to_string()),
                })
                .collect();

            HttpResponse::Ok().json(result)
        }
        Err(err) => {
            eprintln!("Error al obtener las reservas de hoy: {:?}", err);
            HttpResponse::InternalServerError().body("Error al obtener las reservas de hoy")
        }
    }
}

// Main
#[actix_web::main]
async fn main() -> std::io::Result<()> {
    dotenv().ok();

    let database_url = env::var("DATABASE_URL").expect("DATABASE_URL no está configurada");

    let pool = MySqlPool::connect(&database_url)
        .await
        .expect("No se pudo conectar a la base de datos");

    println!("Conexión a la base de datos exitosa");

    HttpServer::new(move || {
        App::new()
            .app_data(web::Data::new(pool.clone()))
            .route("/api/login", web::post().to(login_handler))
            .app_data(web::Data::new(pool.clone()))
            .route("/api/rooms", web::get().to(get_rooms)) // Obtener habitacion
            .route("/api/rooms", web::post().to(create_room)) //Crear habitacion
            .route("/api/rooms/{id}", web::delete().to(delete_room)) //Eliminar habitacion
            .route("/api/rooms/{id}", web::put().to(update_room)) // Actualizar habitacion
            .route("/api/clients", web::get().to(get_clients)) // Obtener clientes
            .route("/api/clients", web::post().to(create_client)) // Crear cliente
            .route("/api/clients/{id}", web::put().to(update_client)) // Actualizar cliente
            .route("/api/clients/{id}", web::delete().to(delete_client)) // Eliminar cliente
            .route("/api/reservations", web::get().to(get_reservations)) // Obtener reservaciones
            .route("/api/reservations", web::post().to(create_reservation)) // Crear reservación
            .route("/api/reservations/{id}", web::put().to(update_reservation)) // Actualizar reservación
            .route("/api/reservations/{id}", web::delete().to(delete_reservation)) // Eliminar reservación
            .route("/api/reservations/today", web::get().to(get_reservations_for_today)) // Obtener reservas del día
    })
    .bind("127.0.0.1:8080")?
    .run()
    .await
}