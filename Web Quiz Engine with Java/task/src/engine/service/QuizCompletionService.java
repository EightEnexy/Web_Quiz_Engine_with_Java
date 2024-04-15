package engine.service;

import engine.model.Quiz;
import engine.model.QuizCompletion;
import engine.model.User;
import engine.repository.QuizCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
/**
 * Service class for managing quiz completions. Provides functionality for creating quiz completions
 * and retrieving completion data for quizzes based on user identification.
 */
@Service
public class QuizCompletionService {
    private final QuizCompletionRepository repository;
    /**
     * Constructs a QuizCompletionService with the necessary repository.
     *
     * @param repository The repository used for storing and retrieving quiz completion data.
     */
    @Autowired
    public QuizCompletionService(QuizCompletionRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds all quiz completions for a specific user, sorted by completion date in descending order.
     * The results are paginated.
     *
     * @param email The email of the user whose completions are being queried.
     * @param page The page number of the result set (zero-based).
     * @param pageSize The size of the page to be returned.
     * @return A page of {@link QuizCompletion} instances.
     */

    public Page<QuizCompletion> findAllByUserEmail(String email, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize,Sort.by("completedAt").descending());
        return repository.findAllByUserEmail(email,pageable);

    }

    /**
     * Records a completed quiz for a user. This method creates a new QuizCompletion entry
     * in the repository.
     *
     * @param user The user who completed the quiz.
     * @param quiz The quiz that was completed.
     */
    public void createCompleted(User user, Quiz quiz) {
        QuizCompletion completion = new QuizCompletion();
        completion.setQuiz(quiz);
        completion.setUser(user);
        repository.save(completion);
    }
}
