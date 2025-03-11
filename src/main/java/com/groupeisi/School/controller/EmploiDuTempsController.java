package com.groupeisi.School.controller;

import com.groupeisi.School.model.EmploiDuTemps;
import com.groupeisi.School.service.EmploiDuTempsService;
import com.groupeisi.School.service.CoursService;
import com.groupeisi.School.service.ProfesseurService;
import com.groupeisi.School.service.SalleService;
import com.groupeisi.School.service.ClasseService;  // Importez le service Classe
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/emplois")
public class EmploiDuTempsController {

    private final EmploiDuTempsService emploiDuTempsService;
    private final CoursService coursService;
    private final ProfesseurService professeurService;
    private final SalleService salleService;
    private final ClasseService classeService;


    // Injection par constructeur
    public EmploiDuTempsController(EmploiDuTempsService emploiDuTempsService, CoursService coursService,
                                   ProfesseurService professeurService, SalleService salleService,
                                   ClasseService classeService) {
        this.emploiDuTempsService = emploiDuTempsService;
        this.coursService = coursService;
        this.professeurService = professeurService;
        this.salleService = salleService;
        this.classeService = classeService;  // Injectez le service Classe ici
    }


    // Affichage de la liste des emplois du temps
    @GetMapping
    public String afficherListeEmploiDuTemps(Model model) {
        model.addAttribute("emplois", emploiDuTempsService.findAll());
        return "emplois/listemplois";  // Vue pour afficher la liste des emplois du temps
    }

    // Affichage du formulaire d'ajout d'un emploi du temps
    @GetMapping("/add")
    public String ajouterEmploiDuTempsForm(Model model) {
        model.addAttribute("emploiDuTemps", new EmploiDuTemps());
        model.addAttribute("cours", coursService.findAll());
        model.addAttribute("professeurs", professeurService.findAll());
        model.addAttribute("salles", salleService.findAll());
        model.addAttribute("classes", classeService.findAll());  // Assurez-vous que la classe est aussi passée
        return "emplois/addemplois";  // Vue pour ajouter un emploi du temps
    }

    // Affichage du formulaire de modification d'un emploi du temps
    @GetMapping("/{id}/edit")
    public String modifierEmploiDuTempsForm(@PathVariable Long id, Model model) {
        EmploiDuTemps emploiDuTemps = emploiDuTempsService.findById(id).orElse(null);
        if (emploiDuTemps == null) {
            model.addAttribute("errorMessage", "Emploi du temps introuvable.");
            return "redirect:/emplois";
        }
        model.addAttribute("emploiDuTemps", emploiDuTemps);
        model.addAttribute("cours", coursService.findAll());
        model.addAttribute("professeurs", professeurService.findAll());
        model.addAttribute("salles", salleService.findAll());
        model.addAttribute("classes", classeService.findAll());  // Assurez-vous que la classe est aussi passée

        return "emplois/editemplois";  // Vue pour modifier un emploi du temps
    }

    // Sauvegarde de l'emploi du temps (création ou mise à jour)
    @PostMapping("/save")
    public String saveEmploiDuTemps(@ModelAttribute EmploiDuTemps emploiDuTemps) {
        if (emploiDuTemps.getId() == null) {
            // Si l'ID est nul, c'est une création
            emploiDuTempsService.create(emploiDuTemps);
        } else {
            // Sinon, c'est une mise à jour
            emploiDuTempsService.update(emploiDuTemps.getId(), emploiDuTemps);
        }
        return "redirect:/emplois";  // Redirige vers la liste des emplois du temps après l'enregistrement
    }

    // Suppression d'un emploi du temps
    @GetMapping("/{id}/delete")
    public String deleteEmploiDuTemps(@PathVariable Long id) {
        emploiDuTempsService.deleteById(id);
        return "redirect:/emplois";  // Redirige vers la liste des emplois du temps après suppression
    }
}
