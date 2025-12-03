✈️ Flight Booking Microservices – Distributed System (Spring Boot + WebFlux + RabbitMQ + Config Server)

A production-grade Flight Booking Microservice System built with a modern distributed architecture using:

Reactive WebFlux + R2DBC

Spring MVC + JPA

RabbitMQ asynchronous messaging

Spring Cloud Config (Git-backed)

Eureka Discovery Server

API Gateway

Resilience4j Circuit Breakers

This system simulates a real airline booking workflow with flight search, reservation, email notifications, fault tolerance, reactive performance, and automation testing using Postman/Newman.

🏗️ Architecture Overview
                           ┌──────────────────────────┐
                           │     Git Config Repo      │
                           └────────────┬─────────────┘
                                        │
                     ┌──────────────────▼──────────────────┐
                     │     Spring Cloud Config Server       │
                     └──────────────────┬──────────────────┘
                                        │ (Fetch configs for all services)
               ┌────────────────────────┴─────────────────────────┐
               │                      Eureka Server               │
               └────────────────────────┬─────────────────────────┘
                                        │
                        ┌───────────────┼───────────────────┐
                        │               │                   │
            ┌───────────▼──────┐ ┌─────▼──────────┐ ┌──────▼────────┐
            │  Flight-Service   │ │ Booking-Service │ │ Notification  │
            │ (WebFlux + R2DBC) │ │ (MVC + JPA)     │ │ (RabbitMQ)    │
            └───────────┬──────┘ └─────┬──────────┘ └──────┬────────┘
                        │              │                    │
         (Reactive DB)  │              │                    │ (Listens)
                  MySQL │              │ MySQL              │ RabbitMQ Queue
                        │              │                    │
                        └──────┬───────┴───────┬────────────┘
                               │               │
                               ▼               ▼
                       ┌─────────────────────────────────┐
                       │     Spring Cloud API Gateway    │
                       │         (Port 8080)             │
                       └─────────────────────────────────┘
                                   │
                             Client / Newman

🧩 Microservices
1️⃣ Flight-Service (Reactive WebFlux + R2DBC + MySQL)

✔ Built using Spring WebFlux
✔ Fully reactive end-to-end using R2DBC MySQL driver
✔ Manages flight inventory and search
✔ Provides internal Feign endpoints for Booking-Service
✔ Uses Resilience4j (Circuit Breaker)

2️⃣ Booking-Service (Spring MVC + JPA + RabbitMQ)

✔ Blocking MVC service (suitable for business transactions)
✔ Handles ticket booking, cancellation, fare calculation
✔ Uses RabbitMQ to send email events
✔ Calls reactive Flight-Service via Feign

3️⃣ Notification-Service (RabbitMQ Listener)

✔ Receives messages from RabbitMQ
✔ Sends booking confirmation/cancellation emails
✔ Logs email messages
✔ Exposes REST API to test email sending

4️⃣ Config Server (External Git Repo)

✔ Loads application.yml for all microservices
✔ Ensures central configuration
✔ Supports live refresh (Spring Cloud Bus optional)

5️⃣ Eureka Discovery Server

✔ Auto-discovers all services
✔ Enables client-side load balancing
✔ Required for Gateway + Feign

6️⃣ API Gateway (Spring Cloud Gateway)

✔ Reactive gateway
✔ Routes traffic to all microservices
✔ Applies circuit breaker fallbacks
✔ Exposes actuator endpoints
✔ Single entry point: http://localhost:8080

🛠️ Tech Stack Summary
Layer	Technology
Reactive Framework	Spring WebFlux
MVC Framework	Spring Web MVC
Persistence	R2DBC (MySQL), JPA (Hibernate + MySQL)
Messaging	RabbitMQ AMQP
Service Discovery	Eureka Server
API Gateway	Spring Cloud Gateway
Config Management	Spring Cloud Config Server
Resilience	Resilience4j Circuit Breaker / TimeLimiter
Test Automation	Postman + Newman CLI
Build Tool	Maven
📁 Project Structure
flight-app-microservice-main/
│
├── eureka-server/
├── api-gateway/
├── config-server/
├── flight-service/        (WebFlux + R2DBC)
├── booking-service/       (MVC + JPA + RabbitMQ Producer)
├── notification-service/  (RabbitMQ Consumer)
└── postman/
      └── flight-collection.json

🚀 Running the System
1️⃣ Start External Dependencies
Start MySQL

Create DBs:

CREATE DATABASE flightdb;
CREATE DATABASE bookingdb;
CREATE DATABASE notificationdb;

Start RabbitMQ

Default settings:

Host: localhost

Username: guest

Password: guest

2️⃣ Run Services in Correct Order
1. Config Server
cd config-server
mvn spring-boot:run

2. Eureka Server
cd eureka-server
mvn spring-boot:run

3. Flight-Service (WebFlux)
cd flight-service
mvn spring-boot:run

4. Booking-Service (MVC + RabbitMQ Producer)
cd booking-service
mvn spring-boot:run

5. Notification-Service (RabbitMQ Listener)
cd notification-service
mvn spring-boot:run

6. API Gateway
cd api-gateway
mvn spring-boot:run

🧪 API Testing (Postman + Newman)

Your repo includes the full test collection:
📄 flight-collection.json


flight-collection

▶️ Import into Postman

Postman → Import → Select JSON

▶️ Run via Newman

Install newman:

npm install -g newman


Execute the tests:

newman run flight-collection.json


Generate HTML Report:

newman run flight-collection.json -r htmlextra

📚 Key Features
✔ Reactive Flight Search
✔ Synchronous Booking & Transaction Control
✔ RabbitMQ Email Workflow
✔ Circuit Breaker + Fallback Routes
✔ Centralized Config Server
✔ Distributed Logging
✔ Health Checks via Actuator
✔ API Gateway Routing & Filters
📌 API Endpoints (via Gateway)
✈️ Flight-Service

POST /api/flight/airline/inventory

POST /api/flight/search

GET /api/flight/{id}

GET /api/flight/{id}/seats

🧾 Booking-Service

POST /api/booking/{flightId}

GET /api/booking/ticket/{pnr}

DELETE /api/booking/cancel/{pnr}

📬 Notification-Service

POST /api/notification/test-email

GET /api/notification/logs

⚙️ Gateway / System

GET /actuator/health

GET /fallback/flight

GET /fallback/booking

🛡️ Resilience4j Config

Circuit Breaker

Time Limiter

Retry

Fallback routes through API Gateway

📨 Messaging Flow (RabbitMQ)
Booking-Service → EmailMessage → RabbitMQ Exchange → Notification-Service → Send Email
