package br.com.picpaysimplificado.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String doucment;
    @Column(unique = true)
    private String email;
    private String password;


}
