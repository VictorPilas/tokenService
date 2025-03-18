# Token Service

Token Service es una aplicación Java Spring Boot que proporciona una API REST para la gestión de tokens. Incluye autenticación segura, generación de tokens y mecanismos de limpieza automática.

## Características

- API REST para operaciones con tokens.
- Autenticación segura utilizando Spring Security.
- Servicio automático de limpieza de tokens.
- Dockerizado para facilitar el despliegue.
- Configuración mediante variables de entorno.

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.3
- Maven
- Docker y Docker Compose
- Lombok

## Comenzando

### Requisitos previos

- Java 17
- Maven 3.8+
- Docker (opcional para despliegue en contenedores)

### Ejecución local

1. **Clonar el repositorio:**

```bash
git clone https://github.com/VictorPilas/tokenService/
cd tokenService
```

2. **Configurar las variables de entorno:**

Edita el archivo `.env` según sea necesario.

3. **Construir la aplicación:**

```bash
./mvnw clean install
```

4. **Ejecutar la aplicación:**

```bash
./mvnw spring-boot:run
```

El servicio estará disponible en `http://localhost:8081` por defecto.

### Ejecución con Docker

1. **Construir e iniciar el contenedor:**

```bash
docker-compose up -d
```

2. **Acceder al servicio:**

```bash
http://localhost:8081
```

### Endpoints de la API

| Endpoint                    | Método | Descripción                       |
|----------------------------|--------|-----------------------------------|
| `/token`                | GET   | Obtener un token            |
| `/metrics`              | GET    | Obtener métricas del servicio     |

### Swagger

http://localhost:8080/swagger-ui/index.html

Usuario: admin
Contraseña: password


## Estructura del Proyecto

```
src/main/java/com/example/tokenService/
├── client/               # Clientes externos
├── config/               # Configuración y seguridad
├── controller/           # Controladores REST
├── exception/            # Excepciones y manejadores personalizados
├── model/                # Modelos de datos
├── TokenServiceApplication.java  # Clase principal
```
