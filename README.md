# Clinic API

A RESTful API for clinic management system built with Spring Boot 3.2.3 and Java 21.

## Features

- User authentication and authorization (JWT with RSA)
- Email notifications (registration, password reset)
- RESTful API design
- H2 Database support
- API documentation with Swagger UI
- Docker support
- Security with Spring Security

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+** (or use included Maven Wrapper)
- **OpenSSL** (for generating JWT keys)
- **Docker** (optional, for containerized deployment)

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd clinicapi
```

### 2. Generate RSA Keys for JWT

Generate RSA key pair for JWT token signing:

**On Windows (PowerShell):**

```powershell
./scripts/generate-keys.ps1
```

**On Linux/Mac:**

```bash
chmod +x scripts/generate-keys.sh
./scripts/generate-keys.sh
```

This will create `private_key.pem` and `public_key.pem` in the `keys/` directory.

### 3. Configure Environment Variables

Create a `.env` file in the project root with the following variables:

```env
# Server Configuration
SERVER_PORT=8080

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:h2:file:./data/clinicdb
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=password

# JWT Configuration
JWT_PRIVATE_KEY_PATH=keys/private_key.pem
JWT_PUBLIC_KEY_PATH=keys/public_key.pem
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=86400000

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200

# API Key (for internal services)
API_KEY=your-secure-api-key-here

# Frontend URL (for email links)
FRONTEND_URL=http://localhost:3000
```

**Note:** For Gmail, you need to use an [App Password](https://support.google.com/accounts/answer/185833) instead of your regular password.

### 4. Build and Run

#### Option A: Using Maven Wrapper (Recommended)

**On Windows:**

```cmd
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

**On Linux/Mac:**

```bash
./mvnw clean install
./mvnw spring-boot:run
```

#### Option B: Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

#### Option C: Using Docker

Build and run with Docker:

```bash
# Build Docker image
docker build -t clinicapi:latest .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:file:/app/data/clinicdb \
  -e SPRING_DATASOURCE_USERNAME=sa \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e JWT_PRIVATE_KEY_PATH=/app/keys/private_key.pem \
  -e JWT_PUBLIC_KEY_PATH=/app/keys/public_key.pem \
  -e MAIL_USERNAME=your-email@gmail.com \
  -e MAIL_PASSWORD=your-app-password \
  -e CORS_ALLOWED_ORIGINS=http://localhost:3000 \
  -e API_KEY=your-secure-api-key \
  -e FRONTEND_URL=http://localhost:3000 \
  -v $(pwd)/keys:/app/keys \
  -v $(pwd)/data:/app/data \
  clinicapi:latest
```

### 5. Verify Installation

The application should now be running on `http://localhost:8080`

- **API Base URL:** `http://localhost:8080/api/v1`
- **Swagger UI:** `http://localhost:8080/api/v1/swagger-ui.html`
- **H2 Console:** `http://localhost:8080/h2-console`

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/api/v1/swagger-ui.html
```

## Database

The application uses H2 database by default. The database file will be created in `./data/clinicdb.mv.db`.

### H2 Console Access

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/clinicdb`
- Username: `sa`
- Password: `password` (or as configured in .env)

## Project Structure

```
clinicapi/
├── src/
│   ├── main/
│   │   ├── java/com/clinicsystem/clinicapi/
│   │   │   ├── config/          # Application configuration
│   │   │   ├── constant/        # Constants and message codes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── filter/          # Security filters
│   │   │   ├── model/           # Entity models
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── service/         # Business logic
│   │   │   ├── util/            # Utility classes
│   │   │   └── validation/      # Custom validators
│   │   └── resources/
│   │       ├── application.yml  # Configuration file
│   │       └── templates/       # Email templates
│   └── test/                    # Test files
├── scripts/                     # Deployment and utility scripts
├── keys/                        # RSA keys for JWT (generated)
├── data/                        # H2 database files
├── Dockerfile                   # Docker configuration
├── pom.xml                      # Maven dependencies
└── README.md                    # This file
```

## Development

### Running Tests

```bash
./mvnw test
```

### Building for Production

```bash
./mvnw clean package -DskipTests
```

The JAR file will be created in `target/clinicapi-0.0.1-SNAPSHOT.jar`

### Run Production JAR

```bash
java -jar target/clinicapi-0.0.1-SNAPSHOT.jar
```

## Deployment Scripts

The project includes several deployment scripts in the `scripts/` directory:

- `generate-keys.sh` / `generate-keys.ps1` - Generate RSA keys for JWT
- `deploy.sh` - Deploy application script
- `health-check.sh` - Health check script
- `rollback.sh` - Rollback deployment
- `cleanup.sh` - Clean up resources

## Technologies Used

- **Spring Boot 3.2.3** - Application framework
- **Java 21** - Programming language
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **H2 Database** - Embedded database
- **JWT** - Token-based authentication
- **Maven** - Dependency management
- **Docker** - Containerization
- **Swagger/OpenAPI** - API documentation

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, change the `SERVER_PORT` in your `.env` file:

```env
SERVER_PORT=8081
```

### Email Not Sending

1. Verify your SMTP credentials
2. For Gmail, ensure you're using an App Password
3. Check if "Less secure app access" is enabled (if not using App Password)

### Database Connection Issues

Make sure the `data/` directory exists and has write permissions:

```bash
mkdir -p data
```

### JWT Key Issues

Regenerate the keys using:

```bash
./scripts/generate-keys.ps1  # Windows
./scripts/generate-keys.sh   # Linux/Mac
```

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.
