# Order & Payment System (Backend Service)

## 1. Problem Statement

*This project represents an Order–Payment backend service designed to manage the business lifecycle of customer orders and their associated payments in a controlled and consistent manner.*

The system allows users to create, update, cancel, and view orders while enforcing clear business rules that govern **when an order can be modified or cancelled**.

The Payment module handles payment processing for orders, supports controlled retries for failed transactions, and ensures that **duplicate payments are prevented**.
Order and payment states are kept in sync to ensure the system never enters an inconsistent or ambiguous state.

All operations are validated against the **current business state** of the order and payment, and any action that violates defined business rules is explicitly rejected.

Overall, the service focuses on:

* maintaining reliable order and payment lifecycles,
* safeguarding data consistency, and
* enforcing business correctness across all operations.

---

## 2. In Scope

* **Order Management**
* **Payment Management**

The scope includes **API design, implementation, and documentation only** (no UI).

Functional scope:

1. Create an order
2. Modify an order
3. Cancel an order
4. Process payment for an order
5. Retry payment
6. Local deployment and Docker deployment

---

## 3. Out of Scope

* Notifications
* Cart management
* Integration with external payment gateways
* Shipping management
* Cloud integration and deployment
* Admin users modifying orders on behalf of customers

---

## 4. High-Level Architecture

```mermaid
flowchart LR
    HTTPClient --> OrderController
    HTTPClient --> PaymentController

    OrderController --> OrderService
    PaymentController --> PaymentService

    OrderService --> OrderRepository
    PaymentService --> PaymentRepository

    OrderRepository --> Database
    PaymentRepository --> Database

    subgraph Monolithic["Monolithic Application"]
        subgraph Controller["REST Controllers"]
            OrderController
            PaymentController
        end

        subgraph Service["Service Layer"]
            OrderService
            PaymentService
        end

        subgraph DataAccess["Data Access Layer"]
            OrderRepository
            PaymentRepository
        end
    end
```

### Module Responsibilities

**Order Module**

* Manages the order lifecycle

  * Create a new order (status = `CREATED`)
  * Modify an order
  * Cancel an order (status = `ORDER_CANCELLED`)
  * Retrieve order details

**Payment Module**

* Manages the payment lifecycle

  * Process payment for an order
  * Retry a failed payment
  * Retrieve payment details
  * Communicate with the Order module to keep order and payment states in sync

**Interaction Between Order and Payment Modules**

* The Payment module interacts with the Order module to enforce business rules:

  * If payment succeeds, both Order and Payment transition to `PAYMENT_DONE`
  * If payment fails, both Order and Payment transition to `PAYMENT_FAILED`
  * To enforce validations such as duplicate payment prevention, the Payment module queries the Order module to determine the current order state
* All inter-module communication is **synchronous** and executed within a **single transaction**

**Data Management**

* The system follows a **single database architecture**

* Deployed using Docker with separate containers for the application and database

---

## 5. Order Lifecycle States

An order can exist in one of the following states:

```mermaid
flowchart LR
    CREATED --> PAYMENT_DONE
    CREATED --> PAYMENT_FAILED
    PAYMENT_FAILED --> PAYMENT_DONE
    CREATED --> ORDER_CANCELLED
    PAYMENT_FAILED --> ORDER_CANCELLED
```

### Order States

* **CREATED** – Order successfully created
* **PAYMENT_DONE** – Payment completed successfully
* **PAYMENT_FAILED** – Payment attempt failed
* **ORDER_CANCELLED** – Order cancelled by the user

### Payment States

* **PAYMENT_DONE** – Payment completed successfully
* **PAYMENT_FAILED** – Payment attempt failed
* **PAYMENT_CANCELLED** – Payment cancelled due to order cancellation

### Order Modification Rules

* An order **cannot be modified** once payment has been initiated
  (`PAYMENT_DONE`, `PAYMENT_FAILED`)
* A cancelled order **cannot be modified**
* An order **cannot be cancelled** once payment is successful
* Duplicate cancellation requests are rejected

---

## 6. Payment Flow & Idempotency

* Payment can be initiated **only when the order state is `CREATED`**
* If payment fails:

  * Payment is marked as `PAYMENT_FAILED`
  * The associated order is also marked as `PAYMENT_FAILED`
* To prevent duplicate payments:

  * The system enforces that **an order can have at most one successful payment**
  * Before processing payment, the system validates the order state and rejects the request if the order is already `PAYMENT_DONE`
* During retry:

  * The system validates that the order is not already successfully paid
* Payment status checks are always performed at the **order level**, as the system guarantees only one successful payment per order
* Payment failure is modeled as a **state transition**, not an exception.
  Both Order and Payment transition to `PAYMENT_FAILED` atomically, allowing safe retries without recreating business intent.

### Successful Payment

```mermaid
flowchart LR
    subgraph "Successful Payment"
        subgraph "Order Module"
            O_CREATED[CREATED] --> O_DONE[PAYMENT_DONE]
        end

        subgraph "Payment Module"
            P_DONE[PAYMENT_DONE]
        end
    end
```

### Order Cancel

```mermaid
flowchart LR
    subgraph "Order Cancel"
        subgraph "Order Module"
            O_CREATED[CREATED] --> O_CANCELLED[ORDER_CANCELLED]
            O_FAILED[PAYMENT_FAILED] --> O_CANCELLED[ORDER_CANCELLED]
        end
        subgraph "Payment Module"
            P_FAILED[PAYMENT_FAILED] --> P_CANCELLED[PAYMENT_CANCELLED]
            %% Payment cancellation is triggered by Order cancellation
        end
    end

```


### Order Cancellation Rules

* An order may be cancelled only if payment has **not** been completed successfully.
* Orders with a **failed payment** (`PAYMENT_FAILED`) are eligible for cancellation.
* Orders with a **successful payment** (`PAYMENT_DONE`) cannot be cancelled.
* When an order is cancelled:
  * All associated payments must be in a cancellable state.
  * If any payment is already completed successfully, the cancellation request is rejected.
* Order cancellation and payment state updates are performed atomically to ensure consistency.
* Order cancellation triggers payment cancellation. All associated payments in a cancellable state (PAYMENT_FAILED) are transitioned to PAYMENT_CANCELLED as part of the same transaction.

### Failed Payment

```mermaid
flowchart LR
    subgraph "Failed Payment"
        subgraph "Order Module"
            O_CREATED[CREATED] --> O_FAILED[PAYMENT_FAILED]
        end

        subgraph "Payment Module"
            P_FAILED[PAYMENT_FAILED]
        end
    end
```

### Retried Payment

```mermaid
flowchart LR
    subgraph "Retried Payment"
        subgraph "Order Module"
            O_FAILED[PAYMENT_FAILED] --> O_DONE[PAYMENT_DONE]
        end

        subgraph "Payment Module"
            P_FAILED[PAYMENT_FAILED] --> P_DONE[PAYMENT_DONE]
        end
    end
```

---

## 7. Failure Handling

* **Payment failure**
  Payment failure is handled as a **state change**, not an exception.
  When payment fails:

  * Payment is marked as `PAYMENT_FAILED`
  * The corresponding order is also marked as `PAYMENT_FAILED`
  * Both updates occur within a single transaction

  This ensures the order never remains stuck in `CREATED` and clearly communicates that a payment retry is required.

* **Duplicate orders**
  The system prevents the creation of multiple orders for the same request.

* **Payment retry**
  Duplicate payments are prevented during retries or client refreshes.
  Before saving any payment, the system checks whether the order is already `PAYMENT_DONE` and rejects the operation if so.
  This guarantees that an order can have **at most one successful payment**, regardless of retry attempts.

---

## 8. Error Handling Strategy

The system uses a **centralized exception handling mechanism** via `@RestControllerAdvice`, which converts domain exceptions into HTTP responses.

By using `@RestControllerAdvice`:

* Controllers and services remain focused on the **success path**
* There are no repetitive `try/catch` blocks cluttering endpoint logic
* Error mapping is centralized, ensuring a **consistent response structure**

Services throw **domain-specific exceptions** (e.g., `OrderNotFoundException`, `DuplicatePaymentException`) without concern for HTTP semantics.

The `@RestControllerAdvice` acts as a mapper that translates these domain exceptions into appropriate HTTP responses, for example:

* `DuplicatePaymentException` → **409 Conflict**
* `OrderAlreadyCancelledException` → **400 Bad Request**

---

## 9. API List (Names Only)

* `POST /orders` – Create an order
* `GET /orders/{orderId}` – Retrieve order details
* `PUT /orders/{orderId}` – Modify order details
* `PUT /orders/{orderId}/cancel` – Cancel an order
* `POST /payments` – Process payment for an order
* `PUT /payments/{paymentId}/retry` – Retry payment
* `GET /payments/{paymentId}` – Retrieve payment details

> No API versioning or advanced filtering in this phase.

---

## 10. Docker Deployment

The application is containerized using Docker and deployed as **separate containers** for the application and the database.

### Deployment Model

* **Application container**
  * Runs the Spring Boot application as an executable JAR
  * Exposes HTTP APIs on port `8081`

* **Database container**
  * Runs PostgreSQL as a separate container
  * Persists data using a Docker volume

The containers communicate over a **Docker bridge network**, allowing the application to connect to the database using the service name as the hostname.

### Container Isolation & Networking

* The application and database run in **isolated containers**
* Communication between containers is **internal to Docker**
* The application connects to PostgreSQL using:

jdbc:postgresql://postgres:5432/orderdb

* Database ports are exposed **only for local development and debugging**
and are not required for container-to-container communication

### Data Persistence

* PostgreSQL data is stored in a **named Docker volume**
* Data remains intact across container restarts

### Orchestration

* Docker Compose is used to:
* Start both containers together
* Define networking and dependencies
* Ensure the database is healthy before the application starts

This deployment approach keeps infrastructure simple while demonstrating
clear separation of concerns, reproducibility, and environment isolation.


## 10. Key Design Decisions

* *A monolithic architecture was chosen to prioritize transactional consistency.
  The core requirement of this system is to ensure that Order and Payment states remain perfectly synchronized. In a monolith, both entities can be updated within a single ACID transaction, guaranteeing that a successful payment is never associated with a failed order.*

* *A synchronous monolithic design was intentionally selected to focus on backend correctness rather than infrastructure complexity.
  Cloud deployment is treated as an environment concern, not a domain concern. This separation ensures that the domain model and business logic are solid before introducing deployment-specific complexity.*

* *Deterministic failure simulation was chosen instead of random failures.
  Random failures make systems difficult to test and reason about. Deterministic failures allow the system to clearly demonstrate state transitions and recovery logic, making the system testable, explainable, and stable.*

---

## 11. Non-Goals

* High availability
* Horizontal scaling
* Distributed transactions
* Event-driven architecture
* External integrations

---
