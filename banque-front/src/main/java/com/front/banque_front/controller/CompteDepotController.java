package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/comptes-depot")
public class CompteDepotController {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:8090/compte-management-1.0-SNAPSHOT/api";

    public CompteDepotController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/client/{idClient}")
    public String gestionCompteDepot(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client
            Map<String, Object> client = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + idClient,
                    Map.class
            );
            model.addAttribute("client", client);

            // Solde brut
            Map<String, Object> soldeBrut = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/client/" + idClient + "/solde-brut",
                    Map.class
            );
            model.addAttribute("soldeBrut", soldeBrut);

            // Intérêts
            Map<String, Object> interets = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/client/" + idClient + "/interets",
                    Map.class
            );
            model.addAttribute("interets", interets);

            // Solde réel
            Map<String, Object> soldeReel = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-depot/client/" + idClient + "/solde-reel",
                    Map.class
            );
            model.addAttribute("soldeReel", soldeReel);

        } catch (Exception e) {
            model.addAttribute("error", "Compte dépôt non trouvé");
        }

        return "comptes/depot";
    }

    @PostMapping("/client/{idClient}/crediter")
    public String crediterCompteDepot(@PathVariable Integer idClient,
                                      @RequestParam Double montant,
                                      @RequestParam String description,
                                      RedirectAttributes redirectAttributes) {

        Map<String, Object> mouvementRequest = new HashMap<>();
        mouvementRequest.put("montant", montant);
        mouvementRequest.put("description", description);
        mouvementRequest.put("dateMouvement", new Date().toInstant().toString());

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    API_BASE_URL + "/comptes-depot/client/" + idClient + "/crediter",
                    mouvementRequest,
                    Map.class
            );

            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                redirectAttributes.addFlashAttribute("success", "Dépôt effectué avec succès");
            } else {
                String errorMessage = (String) response.get("error");
                redirectAttributes.addFlashAttribute("error", errorMessage != null ? errorMessage : "Erreur lors du dépôt");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération de dépôt");
        }

        return "redirect:/comptes-depot/client/" + idClient;
    }

    @PostMapping("/client/{idClient}/debiter")
    public String debiterCompteDepot(@PathVariable Integer idClient,
                                     @RequestParam Double montant,
                                     @RequestParam String description,
                                     RedirectAttributes redirectAttributes) {

        Map<String, Object> mouvementRequest = new HashMap<>();
        mouvementRequest.put("montant", montant);
        mouvementRequest.put("description", description);
        mouvementRequest.put("dateMouvement", new Date().toInstant().toString());

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    API_BASE_URL + "/comptes-depot/client/" + idClient + "/debiter",
                    mouvementRequest,
                    Map.class
            );

            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                redirectAttributes.addFlashAttribute("success", "Retrait effectué avec succès");
            } else {
                String errorMessage = (String) response.get("error");
                redirectAttributes.addFlashAttribute("error", errorMessage != null ? errorMessage : "Erreur lors du retrait");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'opération de retrait");
        }

        return "redirect:/comptes-depot/client/" + idClient;
    }
}