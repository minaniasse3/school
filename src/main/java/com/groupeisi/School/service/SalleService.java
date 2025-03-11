package com.groupeisi.School.service;

import com.groupeisi.School.model.Salle;
import com.groupeisi.School.repository.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalleService {

    private final SalleRepository salleRepository;

    // Injection par constructeur
    @Autowired
    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    // Méthode pour récupérer toutes les salles
    public List<Salle> findAll() {
        return salleRepository.findAll();
    }

    // Méthode pour récupérer une salle par ID
    public Optional<Salle> findById(Long id) {
        return salleRepository.findById(id);
    }

    // Méthode pour ajouter une nouvelle salle
    public Salle addSalle(Salle salle) {
        return salleRepository.save(salle);  // Sauvegarde d'une nouvelle salle
    }

    // Méthode pour mettre à jour une salle existante
    public Salle updateSalle(Long id, Salle salleDetails) {
        Optional<Salle> salleOpt = salleRepository.findById(id);

        if (salleOpt.isPresent()) {
            Salle salle = salleOpt.get();
            salle.setNom(salleDetails.getNom());  // Mise à jour du nom
            salle.setStatus(salleDetails.isStatus());  // Mise à jour du status
            salle.setCapacite(salleDetails.getCapacite());  // Mise à jour de la capacité
            return salleRepository.save(salle);  // Sauvegarde de la salle mise à jour
        } else {
            return null;  // Si la salle n'existe pas
        }
    }

    // Méthode pour supprimer une salle par ID
    public void deleteById(Long id) {
        salleRepository.deleteById(id);  // Suppression de la salle
    }
}
