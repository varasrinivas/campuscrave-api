# campuscrave-api

The brains behind the token. Spring Boot 3.5 / Java 21 / Maven backend for
**CampusCrave**, the campus canteen ordering app — and the repo you'll live in
for the *Code With AI* course.

> This repo ships in its honest Day-1 state: the happy path works, the demo
> went great, and several things are quietly wrong. That's not an accident —
> it's the course material. Don't fix what a lab hasn't asked you to fix yet.

## Run it

```bash
./mvnw spring-boot:run
```

- Menu: http://localhost:8080/api/menu
- H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:campuscrave`, user `sa`, empty password)
- The React app (`campuscrave-web`) expects this API on port 8080.

## Test it

```bash
./mvnw test
```

All green. Draw your own conclusions about what that proves.

## The map

```
src/main/java/com/campuscrave/api/
├── controller/   HTTP in: menu, orders, wallet, rush meter, admin
├── service/      the rules: OrderService is where orders become tokens
├── repository/   Spring Data JPA
├── entity/       Student, Dish, Order, OrderItem, Wallet, CanteenConfig
├── dto/          records crossing the HTTP boundary
├── config/       BusinessRules (MAX_ACTIVE_ORDERS = 3, ORDER_CUTOFF 14:30 IST), CutoffPolicy, CORS
└── error/        ApiException family + one handler
```

Schema lives in Flyway (`db/migration`), Day-1 menu in `data.sql`
(Wednesday special: Hyderabadi Biryani, ₹90, stock 3).

## Folders you'll meet later

- `hints/` — optional per-bug reproduction scripts; open only when a lab says so.
- `incident/` — sealed until Episode 42. No peeking; it's better live.
