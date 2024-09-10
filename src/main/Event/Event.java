package com.example.eiadwarfdragqueens.event.modelEntity;

import com.example.eiadwarfdragqueens.user.modelEntity.User;
import com.example.eiadwarfdragqueens.ticket.modelEntity.Ticket;
import com.example.eiadwarfdragqueens.reporte.modelEntity.Reporte;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int cantidadTiquetes;

    // Aqui esta con user, me supongo que despues ponemos que es un/una modelo
    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "user_type_id", referencedColumnName = "typeId"),
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private List<User> modelos;

    @OneToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private Ticket ticket;

    @OneToOne(mappedBy = "evento")
    private Reporte reporte;
}
