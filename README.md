# 🏋️‍♂️ SportsPro — Backend Documentation

**Spring Boot + PostgreSQL + JWT Authentication**

---

## 👨‍💻 Author
**Sakindu Ranepura**  
LinkedIn: www.linkedin.com/in/sakindu-heshan-a8b7b32b5

---

## 📘 Overview
**SportsPro** is a social networking backend designed for athletes, coaches, and sports professionals.  
It powers user profiles, posts, job opportunities, and a complete networking module with connection requests.

Built using **Spring Boot**, **JPA/Hibernate**, **JWT Security**, and **PostgreSQL** (migrated from XAMPP MySQL) for scalability, reliability, and production readiness.

---

## ⚙️ Tech Stack

| Layer | Technology |
|--------|-------------|
| **Backend Framework** | Spring Boot (v3.x) |
| **Database** | PostgreSQL (previously MySQL via XAMPP) |
| **ORM / Persistence** | Spring Data JPA + Hibernate |
| **Security** | Spring Security + JWT |
| **Build Tool** | Maven |
| **File Storage** | Local Upload Directory (`/uploads/`) |
| **Mapping** | MapStruct (DTO ↔ Entity) |
| **Validation** | Jakarta Validation |

---

## 🚀 Key Features

- 🔒 JWT Authentication for secure access  
- 🧱 Layered Architecture (Controller → Service → Repository)  
- 🔄 DTO Mapping via MapStruct  
- 🧍 User Profiles with avatar uploads and skill tags  
- 📰 Posts Feed (create, view, interact)  
- 💼 Opportunities (job/gig listings)  
- 🤝 Networking System (send, accept, remove connections)  
- 🔍 Global Search API across users, posts, opportunities  
- 🗂️ Local File Upload Handling via `/uploads/**`

---

## 🗄️ Database Migration — XAMPP (MySQL) → PostgreSQL

Originally, **SportsPro** used **MySQL through XAMPP** during early development.  
To improve scalability and performance, the backend was migrated to **PostgreSQL**.

### **Migration Summary**

1. **Export MySQL DB**
   - Use phpMyAdmin → Export → SQL file.
2. **Create PostgreSQL Database**
   ```sql
   CREATE DATABASE sportspro;
   CREATE USER sports_user WITH PASSWORD 'yourpassword';
   GRANT ALL PRIVILEGES ON DATABASE sportspro TO sports_user;
