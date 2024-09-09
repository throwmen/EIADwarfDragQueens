package com.example.eiadwarfdragqueens.portfolio.modelEntity;

import com.example.eiadwarfdragqueens.user.modelEntity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="portfolios")
@Data
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String biography;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "user_type_id", referencedColumnName = "typeId"),
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private User user;
}
