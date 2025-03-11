package com.groupeisi.School.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;

    @OneToMany(mappedBy = "classe")
    private List<Etudiant> etudiants;

    @OneToMany(mappedBy = "classe")
    private List<Cours> cours;

}
