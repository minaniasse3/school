package com.groupeisi.School.service;

import com.groupeisi.School.model.Professeur;
import com.groupeisi.School.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;

    // Injection par constructeur
    @Autowired
    public ProfesseurService(ProfesseurRepository professeurRepository) {
        this.professeurRepository = professeurRepository;
    }

    // Méthode pour récupérer tous les professeurs
    public List<Professeur> findAll() {
        return professeurRepository.findAll();
    }

    // Méthode pour récupérer un professeur par ID
    public Optional<Professeur> findById(Long id) {
        return professeurRepository.findById(id);
    }

    // Méthode pour ajouter un professeur
    public Professeur create(Professeur professeur) {
        return professeurRepository.save(professeur);  // Sauvegarde du professeur (ajout)
    }

    // Méthode pour mettre à jour un professeur
    public Professeur update(Long id, Professeur professeur) {
        Optional<Professeur> existingProfesseur = professeurRepository.findById(id);

        if (existingProfesseur.isPresent()) {
            Professeur updatedProfesseur = existingProfesseur.get();

            // Mise à jour des champs du professeur existant
            updatedProfesseur.setNom(professeur.getNom());
            updatedProfesseur.setPrenom(professeur.getPrenom());
            updatedProfesseur.setEmail(professeur.getEmail());
            updatedProfesseur.setTelephone(professeur.getTelephone());
            return professeurRepository.save(updatedProfesseur);  // Sauvegarde du professeur mis à jour
        } else {
            throw new RuntimeException("Professeur non trouvé avec l'ID : " + id);
        }
    }

    // Méthode pour supprimer un professeur par ID
    public void deleteById(Long id) {
        professeurRepository.deleteById(id);  // Suppression du professeur
    }
}
