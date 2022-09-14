package engine;

import engine.model.Answer;
import engine.model.Quiz;
import engine.model.QuizCompletion;
import engine.model.QuizResponse;
import engine.users.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizCompletionRepo quizCompletionRepo;

    @Autowired
    private UserService userService;

    public Quiz getQuiz(Long id) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            return quizOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found.");
    }

    public Quiz saveQuiz(Quiz quiz) {
        quiz.setUser(userService.getUser(getCurrentUserName()));
        return quizRepository.save(quiz);
    }

    public Page<Quiz> getAllQuizzes (Integer page, Integer size) {
        return quizRepository.findAll(PageRequest.of(page, size));
    }

    public QuizResponse solveQuiz(Answer answer, Long n) {
        if (answer == null) {
            answer = new Answer();
            answer.setAnswer(new int[]{});
        }
        try {
            Quiz quiz = getQuiz(n);
            int[] quizAnswers = quiz.getAnswer();
            if (Arrays.equals(quizAnswers, answer.getAnswer())) {
                QuizCompletion quizCompletion = new QuizCompletion(quiz.getId(),
                        LocalDateTime.now(), getCurrentUserName());
                quizCompletionRepo.save(quizCompletion);
                return new QuizResponse(true, "Congratulations, you're right!");
            }
            return new QuizResponse(false, "Wrong answer! Please, try again.");

        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found.");
        }
    }

    public void deleteQuizBuId(Long id) {
        Quiz quiz = getQuiz(id);
        if (quiz.getUser().getEmail().equals(getCurrentUserName())) {
            quizRepository.delete(quiz);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    public Page<QuizCompletion> getCompletedQuizzes(Integer page, Integer size) {
        Pageable sortedByCompletedAt = PageRequest.of(page, size,
                Sort.by("completedAt").descending());
        return quizCompletionRepo.findAllByUser(getCurrentUserName(), sortedByCompletedAt);
    }

    private String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }
}
