package com.groupeisi.School.repository;

import com.groupeisi.School.model.Classe;
import com.groupeisi.School.model.EmploiDuTemps;
import com.groupeisi.School.model.Professeur;
import com.groupeisi.School.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface EmploiDuTempsRepository extends JpaRepository<EmploiDuTemps, Long> {

    // Vérifie si un professeur a un cours à la même heure (en tenant compte de l'heure de début et de fin)
    boolean existsByProfesseurAndHeureDebutAndHeureFin(Professeur professeur, LocalTime heureDebut, LocalTime heureFin);

    // Vérifie si une salle est occupée à la même heure (en tenant compte de l'heure de début et de fin)
    boolean existsBySalleAndHeureDebutAndHeureFin(Salle salle, LocalTime heureDebut, LocalTime heureFin);

    // Recherche des emplois du temps d'une classe pour un jour spécifique
    List<EmploiDuTemps> findByClasseAndJour(Classe classe, String jour);
}
