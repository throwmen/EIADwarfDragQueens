package com.example.eiadwarfdragqueens.user.modelEntity;

import com.example.eiadwarfdragqueens.portfolio.modelEntity.Portfolio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;


@Entity()
@Table(name="users")
@Data()
@IdClass(UserId.class)
public class User {
    @Id
    private String typeId;

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

}
