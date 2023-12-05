package ie.ncirl.esta.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String subject;
    @Column(nullable = false, columnDefinition = "text")
    private String message;
    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;
}
