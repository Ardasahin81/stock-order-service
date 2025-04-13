# Build & Run

## Prerequisites

* Docker installed and running.

## Build Docker Image

Navigate to the project's root directory (where the `Dockerfile` is located) and build image with:

```bash
docker build -t stock-order-service:0.0.1 .
```

Run:

```
docker run --rm -p 8080:8080 --name stock-order-service stock-order-service:0.0.1
```

## Testing

You are provided with these 2 useful tools to test the app:

Swagger Url: <http://localhost:8080/swagger-ui.html> \
H2 Console: <http://localhost:8080/h2-console>

H2 connection JDBC Url: jdbc:h2:mem:stockorderservicedb\
username: sa\
password: password

### Test Users

You can use these 2 users:

username: admin\
password: admin

username: trader\
password: trader

Only admin user can match orders.