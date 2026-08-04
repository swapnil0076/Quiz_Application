# Quiz Application

A Spring Boot quiz application with Thymeleaf pages, role-based access, in-memory user/question storage, quiz submission, and result calculation.

## Features

- User registration and Spring Security login
- Admin quiz list with add, edit, and delete actions
- User quiz page with multiple-choice questions
- Automatic result calculation
- Black-and-white sketch-style interface
- 20 sample questions loaded at startup

## Pages and image captions

### Login page

<img width="1899" height="877" alt="image" src="https://github.com/user-attachments/assets/5020ba6b-a1b1-4e68-b981-f7e41a9fb898" />

The landing page lets users sign in or open the registration form.

### Registration page

<img width="1896" height="864" alt="image" src="https://github.com/user-attachments/assets/9dfc326c-ef76-4492-b303-a620ddd5d804" />

The registration form validates required fields, email format, and password length.

### Admin quiz list

<img width="1900" height="859" alt="image" src="https://github.com/user-attachments/assets/c0abae3c-dd80-45a9-b145-46d6fde69d33" />

Admins can review all questions, edit existing questions, delete questions, or open the Add Quiz page.

### Add quiz page

<img width="1896" height="871" alt="image" src="https://github.com/user-attachments/assets/7cb15c32-1898-4e51-8407-b79b8177e3d1" />

Admins use this form to create a new multiple-choice question.

### Quiz page

<img width="1902" height="871" alt="image" src="https://github.com/user-attachments/assets/c1b55342-ccdc-44a1-ac5c-e95a38e48b57" />

Regular users select answers and submit the quiz.

### Result page

<img width="1900" height="873" alt="image" src="https://github.com/user-attachments/assets/3a5b84b1-f67b-4016-bb2e-aeb4be962e34" />

The result page displays the submitted user's score and total number of questions.

## Running the application

```bash
./mvnw spring-boot:run
```

Open `http://localhost:8080/`. The application redirects visitors to the login page.

## Main routes

| Page | Route | Access |
|---|---|---|
| Login | `/user/login` | Public |
| Registration | `/user/register` | Public |
| Quiz list | `/quiz/quizList` | Admin |
| Add quiz | `/quiz/addQuiz` | Admin |
| Edit quiz | `/quiz/editQuiz/{id}/page` | Admin |
| Quiz | `/quiz/quiz` | User |
| Result | `/quiz/result-page?username=...` | User |

## Notes

Users and questions are currently stored in memory. They reset when the application restarts. For production use, connect the application to a database and do not allow public users to choose the admin role during registration.
