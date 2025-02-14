# Etapa de compilación
FROM rust:1.84 AS builder

# Instala dependencias necesarias
RUN apt-get update && apt-get install -y pkg-config libssl-dev

# Crea un directorio de trabajo
WORKDIR /usr/src/app

# Copia archivos de dependencias primero para aprovechar la caché de Docker
COPY Cargo.toml Cargo.lock ./

# Crea un directorio para el código fuente
RUN mkdir src
COPY src/ src/

# Compila la aplicación en modo release
RUN cargo build --release

# Etapa final para reducir el tamaño de la imagen
FROM debian:bookworm-slim AS runtime

# Instala OpenSSL en la imagen final para evitar errores de dependencias
RUN apt-get update && apt-get install -y libssl3 && rm -rf /var/lib/apt/lists/*

# Copia el binario compilado desde la etapa anterior
COPY --from=builder /usr/src/app/target/release/ActixWebAPI /usr/local/bin/ActixWebAPI

# Expone el puerto en el que corre la API
EXPOSE 5000

# Comando para ejecutar la API cuando se levante el contenedor
CMD ["/usr/local/bin/ActixWebAPI"]
