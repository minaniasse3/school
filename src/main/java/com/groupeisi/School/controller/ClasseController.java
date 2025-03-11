package com.groupeisi.School.controller;

import com.groupeisi.School.model.Classe;
import com.groupeisi.School.service.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/classes")
public class ClasseController {

    private final ClasseService classeService;

    @Autowired
    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    // Affichage de la liste des classes
    @GetMapping
    public String afficherListeClasse(Model model) {
        model.addAttribute("classes", classeService.findAll());
        return "classes/listclasses"; // Vue pour afficher la liste des classes
    }

    // Affichage du formulaire d'ajout d'une classe
    @GetMapping("/add")
    public String ajouterClasseForm(Model model) {
        model.addAttribute("classe", new Classe());
        return "classes/addclasse"; // Vue pour ajouter une classe
    }

    // Affichage du formulaire de modification de classe
    @GetMapping("/{id}/edit")
    public String modifierClasseForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Classe> classeOpt = classeService.findById(id);
        if (classeOpt.isPresent()) {
            model.addAttribute("classe", classeOpt.get());
            return "classes/editclasse"; // Vue pour modifier une classe
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Classe non trouvée avec l'ID: " + id);
            return "redirect:/classes"; // Redirige vers la liste des classes en cas d'erreur
        }
    }

    // Sauvegarde ou mise à jour d'une classe
    @PostMapping("/save")
    public String saveClasse(@ModelAttribute Classe classe, RedirectAttributes redirectAttributes) {
        try {
            if (classe.getId() == null) {
                // Ajout de la classe
                classeService.addClasse(classe);
                redirectAttributes.addFlashAttribute("successMessage", "Classe créée avec succès!");
            } else {
                // Mise à jour de la classe existante
                classeService.updateClasse(classe.getId(), classe);
                redirectAttributes.addFlashAttribute("successMessage", "Classe modifiée avec succès!");
            }
            return "redirect:/classes"; // Redirige vers la liste des classes après l'enregistrement
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement: " + e.getMessage());
            return "redirect:/classes"; // Redirige avec message d'erreur
        }
    }

    // Suppression d'une classe
    @GetMapping("/{id}/delete")
    public String deleteClasse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            classeService.deleteById(id); // Suppression de la classe
            redirectAttributes.addFlashAttribute("successMessage", "Classe supprimée avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/classes"; // Redirige vers la liste des classes après la suppression
    }
}
