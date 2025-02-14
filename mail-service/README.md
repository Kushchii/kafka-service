
Sure, here is a `README.md` for your mail service:

```markdown
# Mail Service

## Overview

The Mail Service is a Spring Boot application designed to handle email-related operations. It uses PostgreSQL for data persistence and RabbitMQ for message brokering.

## Technologies Used

- Java
- Spring Boot
- Gradle
- PostgreSQL
- RabbitMQ
- Reactor (for reactive programming)

## Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/your-repo/mail-service.git
cd mail-service
```

### Build the Project

```sh
./gradlew build
```

### Running with Docker Compose

Ensure Docker and Docker Compose are installed on your machine. To start the services, run:

```sh
docker-compose up
```

This will start the following services:
- `postgres_2`: PostgreSQL database for the mail service
- `rabbitmq`: RabbitMQ message broker

### Running the Application

To run the application locally, use:

```sh
./gradlew bootRun
```

### API Endpoints

#### Send Mail

- **URL**: `/api/mail`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "userId": "string",
    "email": "string"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Mail saved successfully"
  }
  ```