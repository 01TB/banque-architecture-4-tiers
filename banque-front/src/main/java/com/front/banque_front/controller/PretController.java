package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Controller
@RequestMapping("/prets")
public class PretController {

    private final RestTemplate restTemplate;

    private final String API_BASE_URL = "http://localhost:8090/compte-management-1.0-SNAPSHOT/api";

    public PretController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/client/{idClient}")
    public String gestionPrets(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client
            Map<String, Object> client = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + idClient,
                    Map.class
            );
            model.addAttribute("client", client);

            // Somme totale des prêts
            Map<String, Object> pretsResponse = restTemplate.getForObject(
                    API_BASE_URL + "/prets/client/" + idClient + "/somme",
                    Map.class
            );
            model.addAttribute("prets", pretsResponse);

        } catch (Exception e) {
            model.addAttribute("error", "Aucun prêt trouvé");
        }

        return "prets/gestion";
    }

    // Afficher le formulaire de création d'un prêt
    @GetMapping("/client/{idClient}/nouveau")
    public String afficherFormulaireCreation(@PathVariable Integer idClient, Model model) {
        try {
            // Récupérer les informations du client
            Map<String, Object> client = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + idClient,
                    Map.class
            );
            model.addAttribute("client", client);

        } catch (Exception e) {
            model.addAttribute("error", "Client non trouvé");
            return "redirect:/prets/client/" + idClient;
        }

        return "prets/nouveau-pret";
    }

    // Traiter la création d'un prêt - Version corrigée
    @PostMapping("/creer")
    public String creerPret(@RequestParam Integer idClient,
                            @RequestParam Double montantPret,
                            @RequestParam Double tauxInteretAnnuel,
                            @RequestParam Integer periodiciteRemboursement,
                            Model model) {

        try {
            // Préparer la requête pour l'API
            Map<String, Object> pretRequest = new HashMap<>();
            pretRequest.put("idClient", idClient);
            pretRequest.put("montantPret", montantPret);
            pretRequest.put("tauxInteretAnnuel", tauxInteretAnnuel);
            pretRequest.put("periodiciteRemboursement", periodiciteRemboursement);
            pretRequest.put("dateCreation", new Date().toInstant().toString());

            // Appeler l'API pour créer le prêt
            Map<String, Object> response = restTemplate.postForObject(
                    API_BASE_URL + "/prets",
                    pretRequest,
                    Map.class
            );

            // Vérifier si la création a réussi
            if (response != null && response.containsKey("idPret")) {
                model.addAttribute("success", "Prêt créé avec succès !");
            } else {
                model.addAttribute("error", "Erreur lors de la création du prêt");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création du prêt: " + e.getMessage());
        }

        return "redirect:/prets/client/" + idClient;
    }

}