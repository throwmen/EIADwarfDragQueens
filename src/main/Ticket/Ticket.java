package com.example.eiadwarfdragqueens.ticket.modelEntity;

import com.example.eiadwarfdragqueens.user.modelEntity.User;
import com.example.eiadwarfdragqueens.event.modelEntity.Event;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name="tickets")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    // Aqui lo mismo, es user, pero tecnicamente lo ponemos como admin u organizer
    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "user_type_id", referencedColumnName = "typeId"),
        @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
}
