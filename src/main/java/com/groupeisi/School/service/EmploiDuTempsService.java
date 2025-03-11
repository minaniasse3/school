package com.groupeisi.School.service;

import com.groupeisi.School.model.EmploiDuTemps;
import com.groupeisi.School.repository.EmploiDuTempsRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmploiDuTempsService {

    private final EmploiDuTempsRepository emploiDuTempsRepository;

    // Injection par constructeur
    public EmploiDuTempsService(EmploiDuTempsRepository emploiDuTempsRepository) {
        this.emploiDuTempsRepository = emploiDuTempsRepository;
    }

    // Méthode pour récupérer tous les emplois du temps
    public List<EmploiDuTemps> findAll() {
        return emploiDuTempsRepository.findAll();
    }

    // Méthode pour récupérer un emploi du temps par ID
    public Optional<EmploiDuTemps> findById(Long id) {
        return emploiDuTempsRepository.findById(id);
    }

    // Méthode pour ajouter un nouvel emploi du temps
    public String create(EmploiDuTemps emploiDuTemps) {
        // Vérification des contraintes : professeur et salle à la même heure
        String errorMessage = validateEmploiDuTemps(emploiDuTemps);
        if (errorMessage != null) {
            return errorMessage;
        }

        // Si tout est valide, on sauvegarde l'emploi du temps
        emploiDuTempsRepository.save(emploiDuTemps);
        return null; // Aucune erreur, l'ajout a réussi
    }

    // Méthode pour mettre à jour un emploi du temps existant
    public String update(Long id, EmploiDuTemps emploiDuTemps) {
        Optional<EmploiDuTemps> existingEmploi = emploiDuTempsRepository.findById(id);

        if (existingEmploi.isPresent()) {
            EmploiDuTemps updatedEmploiDuTemps = existingEmploi.get();

            // Mise à jour des champs avec les nouvelles valeurs
            updatedEmploiDuTemps.setJour(emploiDuTemps.getJour());
            updatedEmploiDuTemps.setHeureDebut(emploiDuTemps.getHeureDebut());
            updatedEmploiDuTemps.setHeureFin(emploiDuTemps.getHeureFin());
            updatedEmploiDuTemps.setCours(emploiDuTemps.getCours());
            updatedEmploiDuTemps.setProfesseur(emploiDuTemps.getProfesseur());
            updatedEmploiDuTemps.setSalle(emploiDuTemps.getSalle());
            updatedEmploiDuTemps.setClasse(emploiDuTemps.getClasse());  // Assurez-vous que la classe est mise à jour aussi

            // Vérification des contraintes : professeur et salle à la même heure
            String errorMessage = validateEmploiDuTemps(updatedEmploiDuTemps);
            if (errorMessage != null) {
                return errorMessage;
            }

            // Sauvegarde de la mise à jour
            emploiDuTempsRepository.save(updatedEmploiDuTemps);
            return null; // Aucune erreur, la mise à jour a réussi
        } else {
            return "Emploi du temps introuvable.";
        }
    }

    // Méthode pour supprimer un emploi du temps par ID
    public void deleteById(Long id) {
        emploiDuTempsRepository.deleteById(id);
    }

    // Méthode pour valider un emploi du temps avant création ou mise à jour
    private String validateEmploiDuTemps(EmploiDuTemps emploiDuTemps) {
        // Vérification si le professeur a déjà un cours à la même heure
        if (emploiDuTempsRepository.existsByProfesseurAndHeureDebutAndHeureFin(
                emploiDuTemps.getProfesseur(),
                emploiDuTemps.getHeureDebut(),
                emploiDuTemps.getHeureFin())) {
            return "Le professeur a déjà un cours à cette heure.";
        }

        // Vérification si la salle est déjà occupée à la même heure
        if (emploiDuTempsRepository.existsBySalleAndHeureDebutAndHeureFin(
                emploiDuTemps.getSalle(),
                emploiDuTemps.getHeureDebut(),
                emploiDuTemps.getHeureFin())) {
            return "La salle est déjà occupée à cette heure.";
        }

        // Vérification si la classe est bien définie
        if (emploiDuTemps.getClasse() == null) {
            return "L'emploi du temps doit être associé à une classe.";
        }

        return null; // Aucune erreur, tout est valide
    }
}
