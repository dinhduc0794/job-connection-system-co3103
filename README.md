# GoodJob - Job Connection System

A full-stack job portal platform for job posting and application across various industries and locations, inspired by platforms like VietnamWorks, ITviec, and TopDev.

> **Capstone Project** | Team Size: 5  
> **Timeline**: 10/2024 - 12/2024

---
![trangchu1](https://github.com/user-attachments/assets/1f7d3a5f-44c5-4d7c-b3da-0d494e6e04bb)
![searchjob](https://github.com/user-attachments/assets/0f3f4ad6-3d80-45c3-b83f-34a6eced7e00)

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
## 🚀 Use case diagram  

![job-connection-system drawio](https://github.com/user-attachments/assets/454c36cc-ef4e-415d-83a1-df2b3bbdf6e6)


---

## 🚀 Enhanced entity–relationship diagram (EERD)
![abc drawio](https://github.com/user-attachments/assets/79d773d7-7f57-495a-9cb6-3b919b561414)

---
## 🚀 Relational Schema
![acd drawio](https://github.com/user-attachments/assets/ab1ae25f-7ca3-432e-b0e1-5ca7c64ed0eb)

---

## 🚀 System architecture
![system_architecture](https://github.com/user-attachments/assets/3fa0d0b8-04f2-4b53-b3ce-36e01c069c9e)


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

## 📶 Website Access

Access the deployed website here:  
[http://jobconnection.s3-website-ap-southeast-1.amazonaws.com](http://jobconnection.s3-website-ap-southeast-1.amazonaws.com)

---

## 🧪 Demo Accounts

| Role       | Username | Password   |
|------------|----------|------------|
| Company    | user31   | password31 |
| Job Seeker | user1    | password1  |

---

## 🚀 Some images
![Screenshot 2024-12-13 233825 (1)](https://github.com/user-attachments/assets/9b1667c3-05db-43a8-ba04-50c8a464f623)
![Screenshot 2024-12-14 011354](https://github.com/user-attachments/assets/69657cee-b92c-4237-a3d8-f2ded0e873b4)
![Screenshot 2024-12-14 011628](https://github.com/user-attachments/assets/c652c220-2475-4bf8-ac89-d369ebbca9af)
![trangchu1](https://github.com/user-attachments/assets/a5397b7f-3305-477f-8230-21db252a5794)
![trangchu2](https://github.com/user-attachments/assets/06a04c0f-f7f6-40d1-b4db-d08c9dd19ff9)
![searchjob](https://github.com/user-attachments/assets/667d4698-fd1a-49c0-8aaf-3090258f30e5)
![searchcompany](https://github.com/user-attachments/assets/892b0f9f-62c3-47f2-b3a1-9e78d0ce8584)
![thongbao](https://github.com/user-attachments/assets/6807d517-f178-47c6-8a00-4bca9704ebfc)
![thongbaoapplicant](https://github.com/user-attachments/assets/83736472-7465-4a58-81f7-761e02ff2e77)
![tindaluu](https://github.com/user-attachments/assets/98967883-0c87-48b0-a800-8443da4b3eab)
![trungtuyen](https://github.com/user-attachments/assets/d974a967-dc35-419e-b25b-7e1362c8df12)
![Screenshot 2024-12-14 030726](https://github.com/user-attachments/assets/cff133b7-8264-40d2-930a-49b316a77591)
![Screenshot 2024-12-14 031818 (1)](https://github.com/user-attachments/assets/5cf5fbcf-4176-4495-945b-ce5094a361f3)
![Screenshot 2024-12-14 014653](https://github.com/user-attachments/assets/2f5ecbe3-dc90-4441-89c1-e52a204c4dc2)

---
