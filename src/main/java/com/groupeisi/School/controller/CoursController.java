package com.groupeisi.School.controller;

import com.groupeisi.School.model.Cours;
import com.groupeisi.School.service.CoursService;
import com.groupeisi.School.service.SalleService;
import com.groupeisi.School.service.ProfesseurService;
import com.groupeisi.School.service.ClasseService;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cours")
public class CoursController {

    private final CoursService coursService;
    private final SalleService salleService;
    private final ProfesseurService professeurService;
    private final ClasseService classeService;

    @Autowired
    public CoursController(CoursService coursService, SalleService salleService, ProfesseurService professeurService, ClasseService classeService) {
        this.coursService = coursService;
        this.salleService = salleService;
        this.professeurService = professeurService;
        this.classeService = classeService;
    }

    @GetMapping
    public String afficherListeCours(Model model) {
        model.addAttribute("cours", coursService.findAll());
        return "cours/listcours";
    }

    @GetMapping("/add")
    public String ajouterCoursForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("salles", salleService.findAll());
        model.addAttribute("professeurs", professeurService.findAll());
        model.addAttribute("classes", classeService.findAll());
        return "cours/addcours";
    }

    @GetMapping("/{id}/edit")
    public String modifierCoursForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cours> coursOpt = coursService.findById(id);
        if (coursOpt.isPresent()) {
            model.addAttribute("cours", coursOpt.get());
            model.addAttribute("salles", salleService.findAll());
            model.addAttribute("professeurs", professeurService.findAll());
            model.addAttribute("classes", classeService.findAll());
            return "cours/editcours";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours non trouvé avec l'ID: " + id);
            return "redirect:/cours";
        }
    }

    @PostMapping("/save")
    public String saveCours(@ModelAttribute Cours cours, RedirectAttributes redirectAttributes) {
        try {
            if (cours.getId() == null) {
                coursService.save(cours);
                redirectAttributes.addFlashAttribute("successMessage", "Cours créé avec succès!");
            } else {
                coursService.update(cours.getId(), cours);
                redirectAttributes.addFlashAttribute("successMessage", "Cours modifié avec succès!");
            }
            return "redirect:/cours";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement: " + e.getMessage());
            return "redirect:/cours";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCours(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            coursService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cours supprimé avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/cours";
    }
}
