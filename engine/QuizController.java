package engine;

import engine.model.Answer;
import engine.model.Quiz;
import engine.model.QuizCompletion;
import engine.model.QuizResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class QuizController {

    Logger logger = LoggerFactory.getLogger(QuizController.class);
    @Autowired
    private QuizService quizService;

    @GetMapping("/api/quizzes/{n}")
    public Quiz getQuiz(@PathVariable Long n) {
        return quizService.getQuiz(n);
    }

    @PostMapping("/api/quizzes")
    public Quiz saveQuiz(@Valid @RequestBody Quiz quiz) {
        return quizService.saveQuiz(quiz);
    }

    @GetMapping("api/quizzes")
    public Page<Quiz> getQuizzesList(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        logger.info("quizzes api was called.");
        return quizService.getAllQuizzes(page, size);
    }

    @PostMapping("api/quizzes/{n}/solve")
    public QuizResponse solveQuiz(@RequestBody(required = false) Answer answer, @PathVariable Long n) {
        return quizService.solveQuiz(answer, n);
    }

    @DeleteMapping("api/quizzes/{n}")
    public void solveQuiz(@PathVariable Long n) {
        quizService.deleteQuizBuId(n);
    }

    @GetMapping("/api/quizzes/completed")
    public Page<QuizCompletion> getCompletedQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        logger.info("Completion api was called.");
        return quizService.getCompletedQuizzes(page, size);
    }
}
