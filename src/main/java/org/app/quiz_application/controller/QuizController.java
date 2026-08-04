package org.app.quiz_application.controller;

import org.app.quiz_application.model.Question;
import org.app.quiz_application.service.QuestionsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import java.security.Principal;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final QuestionsService questionsService;

    public QuizController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping("/addQuiz")
    public String getAddQuizPage(Model model) {
        model.addAttribute("question", new Question());
        return "add-quiz";
    }

    @PostMapping(value = "/questions", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Question> addQuizQuestion(@RequestBody Question question) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionsService.addQuiz(question));
    }

    @GetMapping("/editQuiz/{id}")
    @ResponseBody
    public ResponseEntity<Question> getEditQuizPage(@PathVariable int id) {
        Question question = questionsService.loadQuiz(true).stream()
                .filter(item -> item.getId() == id).findFirst().orElse(null);
        return question == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(question);
    }

    @PutMapping("/questions/{id}")
    @ResponseBody
    public ResponseEntity<Question> editQuizQuestion(@PathVariable int id,
                                                       @RequestBody Question question) {
        Question updated = questionsService.editQuiz(id, question);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/questions/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteQuizQuestion(@PathVariable int id) {
        return questionsService.deleteQuiz(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/questions")
    @ResponseBody
    public List<Question> getQuizQuestions(@RequestParam(defaultValue = "USER") String role) {
        return questionsService.loadQuiz("ADMIN".equalsIgnoreCase(role));
    }

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<String> submitAnswers(@RequestParam String username,
                                                 @RequestBody Map<Integer, String> answers) {
        return questionsService.submitAnswers(username, answers)
                ? ResponseEntity.ok("Answers submitted successfully")
                : ResponseEntity.badRequest().body("Username is required");
    }

    @GetMapping("/result")
    @ResponseBody
    public Map<String, Object> getResult(@RequestParam String username) {
        return questionsService.getResult(username);
    }

    @GetMapping("/quizList")
    public String quizList(Model model) {
        model.addAttribute("questions", questionsService.loadQuiz(true));
        return "quiz-list";
    }

    @PostMapping(value = "/questions", consumes = "application/x-www-form-urlencoded")
    public String addQuestionFromForm(@Valid @ModelAttribute("question") Question question,
                                      BindingResult result) {
        if (result.hasErrors()) return "add-quiz";
        questionsService.addQuiz(question);
        return "redirect:/quiz/quizList";
    }

    @GetMapping("/quiz")
    public String quiz(Model model, Principal principal) {
        model.addAttribute("questions", questionsService.loadQuiz(false));
        model.addAttribute("username", principal.getName());
        return "quiz";
    }

    @GetMapping("/editQuiz/{id}/page")
    public String editQuizPage(@PathVariable int id, Model model) {
        Question question = questionsService.loadQuiz(true).stream()
                .filter(item -> item.getId() == id).findFirst().orElse(null);
        if (question == null) return "redirect:/quiz/quizList";
        model.addAttribute("question", question);
        return "edit-quiz";
    }

    @PostMapping("/editQuiz/{id}")
    public String editQuestionFromForm(@PathVariable int id,
                                       @Valid @ModelAttribute("question") Question question,
                                       BindingResult result) {
        if (result.hasErrors()) return "edit-quiz";
        questionsService.editQuiz(id, question);
        return "redirect:/quiz/quizList";
    }

    @PostMapping("/questions/{id}/delete")
    public String deleteQuestionFromForm(@PathVariable int id) {
        questionsService.deleteQuiz(id);
        return "redirect:/quiz/quizList";
    }

    @PostMapping("/submit-form")
    public String submitQuiz(@RequestParam String username,
                             @RequestParam Map<String, String> formAnswers) {
        Map<Integer, String> answers = new java.util.HashMap<>();
        formAnswers.forEach((key, value) -> {
            if (key.startsWith("answer-")) answers.put(Integer.parseInt(key.substring(7)), value);
        });
        questionsService.submitAnswers(username, answers);
        return "redirect:/quiz/result-page?username=" + username;
    }

    @GetMapping("/result-page")
    public String resultPage(@RequestParam String username, Model model) {
        model.addAllAttributes(questionsService.getResult(username));
        return "result";
    }
}
