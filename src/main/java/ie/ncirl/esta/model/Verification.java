package ie.ncirl.esta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verifications")
public class Verification {
    @Id
    private Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    @Column(nullable = false)
    private Instant createdOn;
}