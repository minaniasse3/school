package com.groupeisi.School.service;

import com.groupeisi.School.model.Cours;
import com.groupeisi.School.repository.CoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursService {

    private final CoursRepository coursRepository;

    // Injection par constructeur
    @Autowired
    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    // Méthode pour récupérer tous les cours
    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    // Méthode pour récupérer un cours par ID
    public Optional<Cours> findById(Long id) {
        return coursRepository.findById(id);
    }

    // Méthode pour enregistrer un nouveau cours
    public Cours save(Cours cours) {
        if (cours.getId() != null && coursRepository.existsById(cours.getId())) {
            throw new IllegalArgumentException("Un cours avec cet ID existe déjà. Utilisez update pour le modifier.");
        }
        return coursRepository.save(cours);
    }

    // Méthode pour mettre à jour un cours existant
    public Cours update(Long id, Cours cours) {
        if (!coursRepository.existsById(id)) {
            throw new IllegalArgumentException("Aucun cours trouvé avec l'ID spécifié.");
        }
        cours.setId(id); // S'assurer que l'ID reste le même
        return coursRepository.save(cours);
    }

    // Méthode pour supprimer un cours par ID
    public void deleteById(Long id) {
        try {
            coursRepository.deleteById(id);
        } catch (Exception e) {
            // Gestion de l'erreur, log ou custom message
            throw new RuntimeException("Erreur lors de la suppression du cours.", e);
        }
    }
}
