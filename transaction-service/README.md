Sure, here is a `README.md` for your transaction service:

```markdown
# Transaction Service

## Overview

The Transaction Service is a Spring Boot application designed to handle transaction-related operations. It uses PostgreSQL for data persistence and RabbitMQ for message brokering.

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
git clone https://github.com/your-repo/transaction-service.git
cd transaction-service
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
- `postgres_1`: PostgreSQL database for the transaction service
- `rabbitmq`: RabbitMQ message broker

### Running the Application

To run the application locally, use:

```sh
./gradlew bootRun
```

### API Endpoints

#### Create Transaction

- **URL**: `/api/transactions`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "id": "string",
    "amount": "number",
    "userId": "string",
    "currency": "string",
    "status": "string",
    "description": "string"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Transaction saved successfully"
  }
  ```