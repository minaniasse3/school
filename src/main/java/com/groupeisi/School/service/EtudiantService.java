package com.groupeisi.School.service;

import com.groupeisi.School.model.Etudiant;
import com.groupeisi.School.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    // Injection par constructeur
    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    // Récupérer tous les étudiants
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    // Récupérer un étudiant par ID
    public Optional<Etudiant> findById(Long id) {
        return etudiantRepository.findById(id);
    }

    // Ajouter un nouvel étudiant
    public Etudiant create(Etudiant etudiant) {
        if (etudiant.getId() != null) {
            throw new IllegalArgumentException("L'ID doit être nul pour une création !");
        }
        return etudiantRepository.save(etudiant);
    }

    // Modifier un étudiant existant
    public Etudiant update(Long id, Etudiant etudiant) {
        Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
        if (existingEtudiant.isPresent()) {
            Etudiant updatedEtudiant = existingEtudiant.get();

            // Mettre à jour les champs
            updatedEtudiant.setNom(etudiant.getNom());
            updatedEtudiant.setPrenom(etudiant.getPrenom());
            updatedEtudiant.setEmail(etudiant.getEmail());
            updatedEtudiant.setDateNaissance(etudiant.getDateNaissance());
            updatedEtudiant.setClasse(etudiant.getClasse());

            return etudiantRepository.save(updatedEtudiant);
        } else {
            throw new RuntimeException("Étudiant introuvable !");
        }
    }

    // Supprimer un étudiant par ID
    public void deleteById(Long id) {
        etudiantRepository.deleteById(id);
    }
}
