package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column
    private String title;
    @NotBlank
    @Column
    private String text;

    @NotNull(message = "Options are required")
    @ElementCollection
    @Size(min = 2)
    private List<String> options;

    @ElementCollection
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "email")
    private User user;


    @JsonIgnore
    @OneToMany(mappedBy = "quiz",cascade = CascadeType.ALL)
    private List<QuizCompletion> completions;


}
