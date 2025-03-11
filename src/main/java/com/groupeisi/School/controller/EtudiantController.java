package com.groupeisi.School.controller;

import com.groupeisi.School.model.Etudiant;
import com.groupeisi.School.service.EtudiantService;
import com.groupeisi.School.service.ClasseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final ClasseService classeService;

    // Injection par constructeur
    public EtudiantController(EtudiantService etudiantService, ClasseService classeService) {
        this.etudiantService = etudiantService;
        this.classeService = classeService;
    }

    // Affichage de la liste des étudiants
    @GetMapping
    public String afficherListeEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiants/listetudiants";  // Vue pour afficher la liste des étudiants
    }

    // Affichage du formulaire d'ajout d'un étudiant
    @GetMapping("/add")
    public String ajouterEtudiantForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("classes", classeService.findAll());
        return "etudiants/addetudiant";  // Vue pour ajouter un étudiant
    }

    // Affichage du formulaire de modification d'un étudiant
    @GetMapping("/{id}/edit")
    public String modifierEtudiantForm(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantService.findById(id).orElse(null);
        if (etudiant == null) {
            model.addAttribute("errorMessage", "Étudiant introuvable.");
            return "redirect:/etudiants";
        }
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("classes", classeService.findAll());
        return "etudiants/editetudiant";  // Vue pour modifier un étudiant
    }

    // Sauvegarde de l'étudiant (création ou mise à jour)
    @PostMapping("/save")
    public String saveEtudiant(@ModelAttribute Etudiant etudiant) {
        if (etudiant.getId() == null) {
            // Si l'ID est nul, c'est une création
            etudiantService.create(etudiant);
        } else {
            // Sinon, c'est une mise à jour
            etudiantService.update(etudiant.getId(), etudiant);
        }
        return "redirect:/etudiants";  // Redirige vers la liste des étudiants après l'enregistrement
    }

    // Suppression d'un étudiant
    @GetMapping("/{id}/delete")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteById(id);
        return "redirect:/etudiants";  // Redirige vers la liste des étudiants après suppression
    }
}
