package engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizResponse {
    private boolean success;
    private String feedback;
}
