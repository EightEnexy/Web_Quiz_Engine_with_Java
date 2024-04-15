package engine.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class QuizAnswer {
    private List<Integer> answer = new ArrayList<>();

}
