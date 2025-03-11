package com.groupeisi.School.service;

import com.groupeisi.School.model.Classe;
import com.groupeisi.School.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseService {

    private final ClasseRepository classeRepository;

    // Injection par constructeur
    @Autowired
    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    // Méthode pour récupérer toutes les classes
    public List<Classe> findAll() {
        return classeRepository.findAll();
    }

    // Méthode pour récupérer une classe par ID
    public Optional<Classe> findById(Long id) {
        return classeRepository.findById(id);
    }

    // Méthode pour enregistrer une nouvelle classe
    public Classe addClasse(Classe classe) {
        if (classe.getId() != null) {
            throw new IllegalArgumentException("L'ID doit être nul pour une nouvelle classe.");
        }
        return classeRepository.save(classe);
    }

    // Méthode pour mettre à jour une classe existante
    public Classe updateClasse(Long id, Classe classe) {
        if (!classeRepository.existsById(id)) {
            throw new IllegalArgumentException("Aucune classe trouvée avec l'ID spécifié.");
        }
        classe.setId(id); // S'assurer que l'ID reste le même
        return classeRepository.save(classe);
    }

    // Méthode pour supprimer une classe par ID
    public void deleteById(Long id) {
        try {
            classeRepository.deleteById(id);
        } catch (Exception e) {
            // Gestion de l'erreur, log ou message personnalisé
            throw new RuntimeException("Erreur lors de la suppression de la classe.", e);
        }
    }
}
