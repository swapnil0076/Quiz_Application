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

Add screenshots to `docs/images/` and use these comments in the README:

### Login page

```markdown
![Login page showing username and password validation](docs/images/login.png)
```

The landing page lets users sign in or open the registration form.

### Registration page

```markdown
![Registration page with username, password, email, and role fields](docs/images/registration.png)
```

The registration form validates required fields, email format, and password length.

### Admin quiz list

```markdown
![Admin quiz list showing edit and delete actions](docs/images/quiz-list.png)
```

Admins can review all questions, edit existing questions, delete questions, or open the Add Quiz page.

### Add quiz page

```markdown
![Add quiz page with question, answer options, and correct answer fields](docs/images/add-quiz.png)
```

Admins use this form to create a new multiple-choice question.

### Quiz page

```markdown
![User quiz page displaying multiple-choice questions](docs/images/quiz.png)
```

Regular users select answers and submit the quiz.

### Result page

```markdown
![Result page displaying the user's score](docs/images/result.png)
```

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
