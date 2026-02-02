🌟 Overview

This application allows users to manage projects, boards, and tasks in a collaborative environment.
Users can create projects, invite team members, track task progress, and manage personal profiles.

The project focuses on:

backend architecture
authentication security
relational data modeling
UI ↔ server interaction
real-world feature implementation

🧠 Key Features
🔐 Secure user authentication (Spring Security + BCrypt)
👤 Profile management + image upload
📁 Multi-project support
🧩 Kanban-style boards (To Do / In Progress / Done)
🧑‍🤝‍🧑 Team collaboration
✅ Task creation, editing, status tracking
🔍 Project search
📊 Progress analytics
🧠 Role-based permissions
📱 Responsive UI

🏗 Architecture

The application follows a layered architecture:
Controller → Service → Repository → Database

Backend design highlights

REST-style controller structure
Service layer business logic separation
JPA entity modeling with relationships
Global model attributes via ControllerAdvice
Spring Security session authentication
File upload handling with multipart config
Static resource serving
Error handling & validation

🛠 Tech Stack

Backend
Java 17
Spring Boot
Spring MVC
Spring Security
Spring Data JPA
Hibernate
Thymeleaf

Frontend
HTML / CSS
Bootstrap
jQuery
Thymeleaf templates
Database
MySQL / H2

👩‍💻 Author

Your 김한나
Backend Developer
GitHub: https://github.com/gromcheetos
