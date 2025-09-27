package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Controller
@RequestMapping("/comptes-courants")
public class CompteCourantController {

    private final RestTemplate restTemplate;

    private final String API_BASE_URL = "http://localhost:8090/compte-management-1.0-SNAPSHOT/api";

    public CompteCourantController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/client/{idClient}")
    public String gestionCompteCourant(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client
            Map<String, Object> client = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + idClient,
                    Map.class
            );
            model.addAttribute("client", client);

            // Solde brut
            Map<String, Object> soldeResponse = restTemplate.getForObject(
                    API_BASE_URL + "/comptes-courants/client/" + idClient + "/solde-brut",
                    Map.class
            );
            model.addAttribute("solde", soldeResponse);

        } catch (Exception e) {
            model.addAttribute("error", "Compte courant non trouvé");
        }

        return "comptes/courant";
    }

    @PostMapping("/client/{idClient}/crediter")
    public String crediterCompteCourant(@PathVariable Integer idClient,
                                        @RequestParam Double montant,
                                        @RequestParam String description) {

        Map<String, Object> mouvementRequest = new HashMap<>();
        mouvementRequest.put("montant", montant);
        mouvementRequest.put("description", description);

        try {
            restTemplate.postForObject(
                    API_BASE_URL + "/comptes-courants/client/" + idClient + "/crediter",
                    mouvementRequest,
                    Map.class
            );
        } catch (Exception e) {
            // Gérer l'erreur
        }

        return "redirect:/comptes-courants/client/" + idClient;
    }

    @PostMapping("/client/{idClient}/debiter")
    public String debiterCompteCourant(@PathVariable Integer idClient,
                                       @RequestParam Double montant,
                                       @RequestParam String description) {

        Map<String, Object> mouvementRequest = new HashMap<>();
        mouvementRequest.put("montant", montant);
        mouvementRequest.put("description", description);

        try {
            restTemplate.postForObject(
                    API_BASE_URL + "/comptes-courants/client/" + idClient + "/debiter",
                    mouvementRequest,
                    Map.class
            );
        } catch (Exception e) {
            // Gérer l'erreur
        }

        return "redirect:/comptes-courants/client/" + idClient;
    }
}