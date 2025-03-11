package com.groupeisi.School.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private boolean status;
    private int capacite;



    @OneToMany(mappedBy = "salle")
    private List<Cours> cours;
}
