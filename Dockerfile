# Usa una imagen base de Rust
FROM rust:latest

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia los archivos del proyecto al contenedor
COPY . .

# Instala las dependencias y compila la aplicación
RUN cargo build --release

# Expone el puerto donde corre la API
EXPOSE 8082

# Comando para ejecutar la API
CMD ["./target/release/roomlogic-api"]