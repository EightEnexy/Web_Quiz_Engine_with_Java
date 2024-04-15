package engine.repository;
import engine.model.QuizCompletion;
import engine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCompletionRepository extends PagingAndSortingRepository<QuizCompletion, Long>, JpaRepository<QuizCompletion, Long> {
    Page<QuizCompletion> findAllByUserEmail(String userEmail, Pageable pageable);

}
