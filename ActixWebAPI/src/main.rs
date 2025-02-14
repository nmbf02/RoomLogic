use actix_web::{web, App, HttpResponse, HttpServer, Responder};
use tokio::task;
use std::time::Duration;
use serde::{Deserialize, Serialize};
use serde_json::json;
use reqwest::Client;
use dotenv::dotenv;
use std::env;

/// Estructura para manejar la solicitud de notificación
#[derive(Serialize, Deserialize)]
struct NotificationPayload {
    title: String,
    body: String,
    token: String, // Token del dispositivo al que se enviará la notificación
}

/// Endpoint de prueba para verificar si la API está funcionando
async fn health_check() -> impl Responder {
    HttpResponse::Ok().body("API funcionando correctamente")
}

/// Función para enviar notificación push con Firebase Cloud Messaging (FCM)
async fn send_push_notification(device_token: &str) -> Result<(), reqwest::Error> {
    dotenv().ok();

    let fcm_server_key = env::var("FCM_SERVER_KEY").expect("Error: FCM_SERVER_KEY no está en .env");
    let client = Client::new();

    let payload = json!({
        "to": device_token,
        "notification": {
            "title": "Tarea Completada",
            "body": "Tu tarea ha finalizado exitosamente."
        }
    });

    let response = client
        .post("https://fcm.googleapis.com/fcm/send")
        .header("Authorization", format!("key={}", fcm_server_key))
        .header("Content-Type", "application/json")
        .json(&payload)
        .send()
        .await?;

    println!("Notificación enviada con éxito: {:?}", response.text().await?);
    Ok(())
}

/// Simulación de una tarea en paralelo con notificación push
async fn execute_task(device_token: web::Json<NotificationPayload>) -> impl Responder {
    let token = device_token.token.clone();
    let token_clone = token.clone();

    task::spawn(async move {
        println!("Iniciando tarea en segundo plano...");
        tokio::time::sleep(Duration::from_secs(5)).await;
        println!("Tarea completada!");

        // Enviar notificación push al finalizar la tarea
        if let Err(e) = send_push_notification(&token_clone).await {
            eprintln!("Error enviando notificación: {:?}", e);
        }
    });

    // Retornar la respuesta sin mover `token`
    HttpResponse::Ok().json(json!({ "message": "Tarea en ejecución", "token": token }))
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    dotenv().ok();
    println!("Iniciando ActixWebAPI en http://127.0.0.1:5000");

    HttpServer::new(|| {
        App::new()
            .route("/", web::get().to(health_check)) // Endpoint de prueba
            .route("/execute_task", web::post().to(execute_task)) // Endpoint para ejecutar tareas
    })
    .bind("0.0.0.0:5000")?
    .run()
    .await
}