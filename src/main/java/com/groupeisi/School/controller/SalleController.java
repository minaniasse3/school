package com.groupeisi.School.controller;

import com.groupeisi.School.model.Salle;
import com.groupeisi.School.service.SalleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salles")
public class SalleController {

    private final SalleService salleService;

    // Injection par constructeur
    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    // Affichage de la liste des salles
    @GetMapping
    public String afficherListeSalles(Model model) {
        model.addAttribute("salles", salleService.findAll());
        return "salles/listesalles";  // Vue pour afficher la liste des salles
    }

    // Affichage du formulaire d'ajout d'une salle
    @GetMapping("/add")
    public String ajouterSalleForm(Model model) {
        model.addAttribute("salle", new Salle());
        return "salles/addsalle"; // Vue pour ajouter une salle
    }

    // Affichage du formulaire de modification d'une salle
    @GetMapping("/{id}/edit")
    public String modifierSalleForm(@PathVariable Long id, Model model) {
        Salle salle = salleService.findById(id).orElse(null);
        if (salle == null) {
            model.addAttribute("errorMessage", "Salle non trouvée.");
            return "redirect:/salles";
        }
        model.addAttribute("salle", salle);
        return "salles/editsalle"; // Vue pour modifier une salle
    }

    // Sauvegarde de la salle (ajout ou mise à jour)
    @PostMapping("/save")
    public String saveSalle(@ModelAttribute Salle salle, Model model) {
        // Si l'ID est nul, il s'agit d'un ajout, sinon, c'est une mise à jour
        if (salle.getId() == null) {
            salleService.addSalle(salle); // Ajout d'une nouvelle salle
            model.addAttribute("successMessage", "Salle ajoutée avec succès!");
        } else {
            Salle updatedSalle = salleService.updateSalle(salle.getId(), salle); // Mise à jour de la salle existante
            if (updatedSalle != null) {
                model.addAttribute("successMessage", "Salle mise à jour avec succès!");
            } else {
                model.addAttribute("errorMessage", "Erreur lors de la mise à jour de la salle.");
            }
        }
        return "redirect:/salles"; // Redirige vers la liste des salles après l'enregistrement
    }

    // Suppression d'une salle
    @GetMapping("/{id}/delete")
    public String deleteSalle(@PathVariable Long id, Model model) {
        salleService.deleteById(id);
        model.addAttribute("successMessage", "Salle supprimée avec succès!");
        return "redirect:/salles"; // Redirige vers la liste des salles après suppression
    }
}
