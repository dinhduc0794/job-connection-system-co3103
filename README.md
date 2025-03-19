# GoodJob - Job Connection System

A full-stack job portal platform for job posting and application across various industries and locations, inspired by platforms like VietnamWorks, ITviec, and TopDev.

> **Capstone Project** | Team Size: 5  
> **Timeline**: 10/2024 - 12/2024

---

## 📌 Description

GoodJob is a job connection system developed for the **Software Engineering Capstone** course. The platform supports job seekers and companies by providing advanced job search, company review, and application management functionalities. It enables seamless interaction between companies and applicants while ensuring security and efficiency.

---

## 🚀 Technologies Used

### Backend:
- Java, Spring Boot
- RESTful API
- Spring Security, JWT Authentication
- Spring Data JPA, Hibernate
- PostgreSQL
- Spring Profiles (multi-environment support)
- Deployment: Railway, AWS EC2

### Frontend:
- ReactJS, JavaScript
- HTML5, CSS3, Tailwind CSS
- Axios for HTTP requests

---

## 👥 Team Roles and Responsibilities

| Name                  | Role                                 | Responsibilities                                                                                  |
|-----------------------|--------------------------------------|--------------------------------------------------------------------------------------------------|
| Nguyễn Đình Đức       | Technical Leader & Backend Developer | Tech stack selection, system design, backend core development, API design, database deployment   |
| Huỳnh Trần Học Đăng   | Frontend Developer                   | UI/UX design, frontend development with ReactJS, integration with backend APIs                   |
| Mai Văn Hoàng Duy     | Frontend Developer                   | UI/UX design, frontend development with ReactJS, integration with backend APIs                   |
| Doãn Đình Hảo         | Backend Developer                    | Backend core development, AWS Deployment                                                         |
| Nguyễn Hoàng Thiện    | Product Owner                        | Write documentation, perform testing, bug tracking, ensure quality standards                     |

---

## 🧹 Core Functionalities

- **User Roles**: Admin, Company, Applicant, Guest — each with distinct permissions and UI.
  
- **Authentication & Authorization**:
  - Secure login and registration for Applicants and Companies.
  - JWT-based role-based access control.

- **Advanced Search**:
  - Job posts: Search by 15 fields.
  - Companies: Search by 7 fields.
  - Address filter: Province → City → District → Ward.

- **User Profile Management**:
  - View, edit (role-dependent), delete account.
  - Change password and view system notifications.

- **Company Rating System**:
  - Rate companies with stars.
  - Real-time rating adjustment based on user feedback.

- **Notification System**:
  - Notifies users for actions such as rating, job application, and status updates.

- **Company Features**:
  - Manage job posts (create, edit, delete).
  - View applications per job post and modify application status.
  - View detailed applicant profiles.

- **Applicant Features**:
  - View and apply for jobs.
  - Manage applications (edit, delete, view status).
  - View and rate companies.
  - Save jobs of interest for later viewing.

---

## 📽️ Demo

Watch the demo video here:  
[![GoodJob Demo](https://img.youtube.com/vi/-fozVZbGKv8/0.jpg)](https://youtube.com/watch?v=-fozVZbGKv8)

---

## 🧪 Demo Accounts

| Role       | Username | Password   |
|------------|----------|------------|
| Company    | user31   | password   |
| Job Seeker | user1    | password1  |

---


## 🛠️ Setup Instructions

### Backend:
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

### Frontend:
```bash
cd frontend
npm install
npm start
```

