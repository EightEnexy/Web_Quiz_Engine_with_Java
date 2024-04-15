package engine.controller;

import engine.model.Feedback;
import engine.model.Quiz;
import engine.model.QuizAnswer;
import engine.model.User;
import engine.service.QuizCompletionService;
import engine.service.QuizzService;
import engine.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api")
public class QuizController {
    private final QuizzService quizzService;
    private final QuizCompletionService quizCompletionService;
    public QuizController(QuizzService quizzService, UserService userService, QuizCompletionService quizCompletionService) {
        this.quizzService = quizzService;
        this.quizCompletionService = quizCompletionService;
    }

    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody Quiz quiz,@AuthenticationPrincipal User user) {
            return ResponseEntity.ok(quizzService.createQuiz(quiz, user));
    }

    @GetMapping("/quizzes")
    public ResponseEntity<Page<Quiz>> getAllQuizzes(@RequestParam(defaultValue = "0") int page,
                                                    @Min(10) @Max(30) @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(quizzService.getAllQuizzes(page,pageSize));
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
            return ResponseEntity.ok(quizzService.getQuizById(id));
    }
    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<Feedback> solveQuiz(@PathVariable Long id, @RequestBody QuizAnswer answer, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quizzService.solveQuiz(id, answer, user));

    }
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id, @AuthenticationPrincipal User user) {
            quizzService.deleteQuiz(id, user.getEmail());
            return ResponseEntity.noContent().build();

    }
    @GetMapping("/quizzes/completed")
    public ResponseEntity<?> quizzesCompleted(@RequestParam(defaultValue = "0") int page,
                                              @Min(10) @Max(30) @RequestParam(defaultValue = "10") int pageSize, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quizCompletionService.findAllByUserEmail(user.getEmail(), page, pageSize));
    }

}
