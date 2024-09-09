package com.example.eiadwarfdragqueens.user.modelEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data()
@NoArgsConstructor
@AllArgsConstructor()
public class UserId implements Serializable {
    private String typeId;
    private Long id;
}
