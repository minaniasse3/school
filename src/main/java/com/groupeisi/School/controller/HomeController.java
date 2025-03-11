package com.groupeisi.School.controller;

import com.groupeisi.School.service.ClasseService;
import com.groupeisi.School.service.CoursService;
import com.groupeisi.School.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ClasseService classeService;

    @Autowired
    private CoursService coursService;

    @Autowired
    private SalleService salleService;

    // Méthode pour afficher la page d'accueil avec toutes les fonctionnalités
    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("cours", coursService.findAll());
        model.addAttribute("salles", salleService.findAll());
        return "home";  // Nom de la vue d'accueil

    }
}