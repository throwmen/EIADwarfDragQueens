package com.example.eiadwarfdragqueens.reporte.modelEntity;

import com.example.eiadwarfdragqueens.user.modelEntity.User;
import com.example.eiadwarfdragqueens.evento.modelEntity.Evento;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="reportes")
@Data
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "user_type_id", referencedColumnName = "typeId"),
        @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private User user;

    @OneToOne
    @JoinColumn(name = "evento_id", referencedColumnName = "id")
    private Evento evento;
}
