package com.groupeisi.School.controller;

import com.groupeisi.School.model.Professeur;
import com.groupeisi.School.service.ProfesseurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    // Injection par constructeur
    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    // Affichage de la liste des professeurs
    @GetMapping
    public String afficherListeProfesseurs(Model model) {
        model.addAttribute("professeurs", professeurService.findAll());
        return "professeurs/listprofesseurs";  // Vue pour afficher la liste des professeurs
    }

    // Affichage du formulaire d'ajout d'un professeur
    @GetMapping("/add")
    public String ajouterProfesseurForm(Model model) {
        model.addAttribute("professeur", new Professeur());
        return "professeurs/addprofesseur"; // Vue pour ajouter un professeur
    }

    // Affichage du formulaire de modification d'un professeur
    @GetMapping("/{id}/edit")
    public String modifierProfesseurForm(@PathVariable Long id, Model model) {
        Professeur professeur = professeurService.findById(id).orElse(null);
        if (professeur == null) {
            model.addAttribute("errorMessage", "Professeur non trouvé.");
            return "redirect:/professeurs";
        }
        model.addAttribute("professeur", professeur);
        return "professeurs/editprofesseur"; // Vue pour modifier un professeur
    }

    // Sauvegarde du professeur (ajout ou mise à jour)
    @PostMapping("/save")
    public String saveProfesseur(@ModelAttribute Professeur professeur, Model model) {
        if (professeur.getId() == null) {
            // Si l'ID est nul, c'est un ajout
            professeurService.create(professeur);  // Appelle la méthode create pour ajouter
        } else {
            // Sinon, c'est une modification
            professeurService.update(professeur.getId(), professeur);  // Appelle la méthode update pour modifier
        }
        model.addAttribute("successMessage", "Professeur enregistré avec succès!");
        return "redirect:/professeurs"; // Redirige vers la liste des professeurs après l'enregistrement
    }

    // Suppression d'un professeur
    @GetMapping("/{id}/delete")
    public String deleteProfesseur(@PathVariable Long id, Model model) {
        professeurService.deleteById(id);
        model.addAttribute("successMessage", "Professeur supprimé avec succès!");
        return "redirect:/professeurs"; // Redirige vers la liste des professeurs après suppression
    }
}
