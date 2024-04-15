package engine.service;

import engine.exception.QuizNotFoundException;
import engine.exception.UnauthorizedAccessException;
import engine.model.Feedback;
import engine.model.Quiz;
import engine.model.QuizAnswer;
import engine.model.User;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing quizzes. This includes creating quizzes, retrieving quiz information,
 * solving quizzes, and deleting quizzes. It leverages the QuizRepository for persistence operations
 * and QuizCompletionService for recording quiz completions.
 */
@Service
public class QuizzService {
    private final QuizRepository quizRepository;
    private final QuizCompletionService quizCompletionService;

    /**
     * Constructs a QuizzService with necessary dependencies.
     *
     * @param quizRepository        Repository for accessing quiz data.
     * @param quizCompletionService Service for managing quiz completions.
     */
    @Autowired
    public QuizzService(QuizRepository quizRepository, QuizCompletionService quizCompletionService) {
        this.quizRepository = quizRepository;
        this.quizCompletionService = quizCompletionService;
    }

    /**
     * Creates a quiz and associates it with a user.
     *
     * @param quiz The quiz to create.
     * @param user The user who creates the quiz.
     * @return The saved quiz.
     */
    public Quiz createQuiz(Quiz quiz, User user) {
        quiz.setUser(user);
        return quizRepository.save(quiz);
    }

    /**
     * Retrieves all quizzes in a paginated format.
     *
     * @param page     The page number to retrieve.
     * @param pageSize The number of quizzes per page.
     * @return A page of quizzes.
     */
    public Page<Quiz> getAllQuizzes(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return quizRepository.findAll(pageable);
    }

    /**
     * Fetches a quiz by its ID.
     *
     * @param id The ID of the quiz to retrieve.
     * @return The requested quiz.
     * @throws QuizNotFoundException if the quiz cannot be found.
     */
    public Quiz getQuizById(Long id) throws QuizNotFoundException {
        return quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found for ID: " + id));
    }

    /**
     * Attempts to solve a quiz by comparing the provided answers with the correct answers.
     *
     * @param id     The ID of the quiz to solve.
     * @param answer The user's answers.
     * @param user   The user attempting the quiz.
     * @return Feedback indicating whether the answers were correct or not.
     * @throws QuizNotFoundException if the quiz cannot be found.
     */
    public Feedback solveQuiz(Long id, QuizAnswer answer, User user) throws QuizNotFoundException {
       Quiz quiz = getQuizById(id);
        boolean isCorrect = areAnswersCorrect(answer.getAnswer(),quiz.getAnswer());
        if (isCorrect) {
            quizCompletionService.createCompleted(user, quiz);
        }
        return generateFeedback(isCorrect);
    }

    /**
     * Compares two lists of answers to determine if they are equal.
     *
     * @param correctAnswers The correct answers.
     * @param givenAnswers   The answers given by the user.
     * @return true if the answers are the same, false otherwise.
     */
    private boolean areAnswersCorrect(List<Integer> correctAnswers, List<Integer> givenAnswers) {
        return new ArrayList<>(correctAnswers).equals(givenAnswers);
    }

    /**
     * Deletes a quiz if the requesting user is the owner of the quiz.
     *
     * @param quizId    The ID of the quiz to delete.
     * @param userEmail The email of the user attempting to delete the quiz.
     * @throws UnauthorizedAccessException if the user is not authorized to delete the quiz.
     */
    public void deleteQuiz(Long quizId, String userEmail) {
        if (getQuizById(quizId).getUser().getEmail().equals(userEmail)) {
            quizRepository.deleteById(quizId);
        }
        else {
            throw new UnauthorizedAccessException("Unauthorized to delete quiz with ID: " + quizId);
        }
    }

    /**
     * Generates feedback based on whether the quiz answers were correct.
     *
     * @param isCorrect true if the answers were correct, false otherwise.
     * @return Feedback containing the result of the quiz attempt.
     */
    private Feedback generateFeedback(boolean isCorrect) {
        String message = isCorrect ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";
        return new Feedback(isCorrect, message);
    }


}
