package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import engine.users.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @Size(min = 2)
    @NotNull(message = "Should contain at least 2 items.")
    private String[] options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int[] answer = new int[]{};

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
