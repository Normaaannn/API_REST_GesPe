# 🌐 GESPE API

**API REST para gestión de pedidos – desarrollada con Spring Boot**

Este es el backend del sistema *GESPE* (Gestor de Pedidos), una aplicación de gestión de pedidos con interfaz en español.  
Proporciona una API RESTful construida con **Spring Boot**, utilizando entidades, rutas y estructuras de datos en **español** para mantener coherencia con el frontend en Ionic Angular.

---

## 🚀 Funcionalidades

- 📄 API RESTful con operaciones CRUD completas  
- 🧾 Entidades con nombres en español (`Pedido`, `Cliente`, `Producto`, etc.)  
- 🛡️ Lista para autenticación y autorización (JWT o Basic Auth)  
- 🗄️ Integración con JPA/Hibernate y base de datos relacional  
- 🌍 CORS configurado para integración con frontend

---

## 📁 Entidades principales

- `Pedido` – entidad de pedidos  
- `Cliente` – datos de clientes  
- `Producto` – catálogo de productos  
- `DetallePedido` – líneas de pedido (items)

---

## 📦 Tecnologías utilizadas

- **Spring Boot**  
- **Spring Data JPA**  
- **Spring Security** (opcional)  
- **H2 / MySQL / PostgreSQL** (configurable)  
- **Maven / Gradle**  
- **Java 17 o superior**

---

## 🧪 Cómo ejecutar

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/gespe-api.git
cd gespe-api

# Ejecutar con Maven
./mvnw spring-boot:run

# O, usando Gradle
./gradlew bootRun
