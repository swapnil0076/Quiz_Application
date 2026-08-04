package org.app.quiz_application.service;

import org.app.quiz_application.model.Question;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionsService {

    private final List<Question> questions = new ArrayList<>();
    private final Map<String, Map<Integer, String>> submissions = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @PostConstruct
    public void seedSampleQuestions() {
        addQuiz(new Question(0, "What is the capital of France?",
                List.of("Berlin", "Madrid", "Paris", "Rome"), "Paris"));
        addQuiz(new Question(0, "Which planet is known as the Red Planet?",
                List.of("Earth", "Mars", "Jupiter", "Venus"), "Mars"));
        addQuiz(new Question(0, "What is HTML an abbreviation for?",
                List.of("Hyper Text Markup Language", "High Text Machine Language", "Hyperlink Text Markup Language", "Home Tool Markup Language"), "Hyper Text Markup Language"));
        addQuiz(new Question(0, "Which language runs in a web browser?",
                List.of("Java", "C++", "Python", "JavaScript"), "JavaScript"));
        addQuiz(new Question(0, "What is the largest ocean on Earth?",
                List.of("Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"), "Pacific Ocean"));
        addQuiz(new Question(0, "Who wrote Romeo and Juliet?",
                List.of("William Shakespeare", "Charles Dickens", "Mark Twain", "Jane Austen"), "William Shakespeare"));
        addQuiz(new Question(0, "What is the chemical symbol for gold?",
                List.of("Go", "Gd", "Au", "Ag"), "Au"));
        addQuiz(new Question(0, "How many continents are there?",
                List.of("Five", "Six", "Seven", "Eight"), "Seven"));
        addQuiz(new Question(0, "Which data structure uses FIFO order?",
                List.of("Stack", "Queue", "Tree", "Graph"), "Queue"));
        addQuiz(new Question(0, "What does CPU stand for?",
                List.of("Central Processing Unit", "Computer Personal Unit", "Central Program Utility", "Core Processing User"), "Central Processing Unit"));
        addQuiz(new Question(0, "Which is the smallest prime number?",
                List.of("0", "1", "2", "3"), "2"));
        addQuiz(new Question(0, "What is the process by which plants make food?",
                List.of("Respiration", "Photosynthesis", "Digestion", "Fermentation"), "Photosynthesis"));
        addQuiz(new Question(0, "Which HTTP method is commonly used to create a resource?",
                List.of("GET", "POST", "PUT", "DELETE"), "POST"));
        addQuiz(new Question(0, "What is the square root of 144?",
                List.of("10", "11", "12", "14"), "12"));
        addQuiz(new Question(0, "Which country is famous for the Taj Mahal?",
                List.of("India", "Egypt", "China", "Nepal"), "India"));
        addQuiz(new Question(0, "What does URL stand for?",
                List.of("Universal Resource Link", "Uniform Resource Locator", "Unified Routing Language", "User Resource Location"), "Uniform Resource Locator"));
        addQuiz(new Question(0, "Which gas do humans need to breathe?",
                List.of("Carbon dioxide", "Nitrogen", "Oxygen", "Hydrogen"), "Oxygen"));
        addQuiz(new Question(0, "Which collection does not allow duplicate elements in Java?",
                List.of("List", "Set", "Queue", "ArrayList"), "Set"));
        addQuiz(new Question(0, "How many days are in a leap year?",
                List.of("364", "365", "366", "367"), "366"));
        addQuiz(new Question(0, "Which instrument is used to measure temperature?",
                List.of("Barometer", "Thermometer", "Hygrometer", "Speedometer"), "Thermometer"));
    }

    public synchronized List<Question> loadQuiz(boolean admin) {
        return questions.stream().map(question -> copyQuestion(question, admin)).toList();
    }

    public synchronized Question addQuiz(Question question) {
        if (question.getId() == 0) {
            question.setId(nextId.getAndIncrement());
        }
        questions.add(question);
        return question;
    }

    public synchronized Question editQuiz(int id, Question updatedQuestion) {
        for (Question question : questions) {
            if (question.getId() == id) {
                question.setQuestionText(updatedQuestion.getQuestionText());
                question.setOptions(updatedQuestion.getOptions());
                if (updatedQuestion.getCorrectAnswer() != null) {
                    question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                }
                return question;
            }
        }
        return null;
    }

    public synchronized boolean deleteQuiz(int id) {
        return questions.removeIf(question -> question.getId() == id);
    }

    public boolean submitAnswers(String username, Map<Integer, String> answers) {
        if (username == null || username.isBlank()) {
            return false;
        }
        submissions.put(username, new ConcurrentHashMap<>(answers));
        return true;
    }

    public Map<String, Object> getResult(String username) {
        Map<Integer, String> answers = submissions.get(username);
        int correct = 0;
        if (answers != null) {
            for (Question question : questions) {
                if (Objects.equals(question.getCorrectAnswer(), answers.get(question.getId()))) {
                    correct++;
                }
            }
        }
        return Map.of("username", username, "score", correct,
                "total", questions.size(), "submitted", answers != null);
    }

    private Question copyQuestion(Question question, boolean includeAnswer) {
        return new Question(question.getId(), question.getQuestionText(), question.getOptions(),
                includeAnswer ? question.getCorrectAnswer() : null);
    }
}
