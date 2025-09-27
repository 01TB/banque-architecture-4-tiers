package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
        import java.util.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final RestTemplate restTemplate;

    private final String API_BASE_URL = "http://localhost:8090/compte-management-1.0-SNAPSHOT/api";

    public ClientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Liste des clients
    @GetMapping
    public String listeClients(Model model,
                               @RequestParam(required = false) String nom,
                               @RequestParam(required = false) String prenom,
                               @RequestParam(required = false) String email) {

        String url = API_BASE_URL + "/clients";

        // Si des paramètres de recherche sont présents
        if (nom != null || prenom != null || email != null) {
            StringBuilder searchUrl = new StringBuilder(API_BASE_URL + "/clients/search?");
            List<String> params = new ArrayList<>();

            if (nom != null && !nom.isEmpty()) params.add("nom=" + nom);
            if (prenom != null && !prenom.isEmpty()) params.add("prenom=" + prenom);
            if (email != null && !email.isEmpty()) params.add("email=" + email);

            searchUrl.append(String.join("&", params));
            url = searchUrl.toString();
        }

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                model.addAttribute("clients", response.get("clients"));
                model.addAttribute("count", response.get("count"));
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des clients");
        }

        return "clients/liste";
    }

    // Détail d'un client
    @GetMapping("/{id}")
    public String detailClient(@PathVariable Integer id, Model model) {
        try {
            // Informations du client
            Map<String, Object> client = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + id,
                    Map.class
            );
            model.addAttribute("client", client);

            // Solde avec intérêt
            Map<String, Object> soldeAvecInteret = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + id + "/solde-avec-interet",
                    Map.class
            );
            model.addAttribute("soldeAvecInteret", soldeAvecInteret);

            // Solde total
            Map<String, Object> soldeTotal = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + id + "/solde-totale",
                    Map.class
            );
            model.addAttribute("soldeTotal", soldeTotal);

        } catch (Exception e) {
            model.addAttribute("error", "Client non trouvé");
            return "redirect:/clients";
        }

        return "clients/detail";
    }

    // Formulaire de création
    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("client", new HashMap<String, Object>());
        return "clients/formulaire";
    }

    // Création d'un client
    @PostMapping
    public String creerClient(@RequestParam Map<String, String> formData) {
        Map<String, Object> clientRequest = new HashMap<>();
        clientRequest.put("matricule", formData.get("matricule"));
        clientRequest.put("nom", formData.get("nom"));
        clientRequest.put("prenom", formData.get("prenom"));
        clientRequest.put("dateNaissance", formData.get("dateNaissance"));
        clientRequest.put("adresse", formData.get("adresse"));
        clientRequest.put("email", formData.get("email"));
        clientRequest.put("telephone", formData.get("telephone"));

        try {
            restTemplate.postForObject(API_BASE_URL + "/clients", clientRequest, Map.class);
        } catch (Exception e) {
            // Gérer l'erreur
        }

        return "redirect:/clients";
    }

    // Formulaire de modification
    @GetMapping("/{id}/modifier")
    public String formulaireModification(@PathVariable Integer id, Model model) {
        try {
            Map<String, Object> client = restTemplate.getForObject(
                    API_BASE_URL + "/clients/" + id,
                    Map.class
            );
            model.addAttribute("client", client);
        } catch (Exception e) {
            return "redirect:/clients";
        }
        return "clients/formulaire";
    }

    // Modification d'un client
    @PostMapping("/{id}")
    public String modifierClient(@PathVariable Integer id, @RequestParam Map<String, String> formData) {
        Map<String, Object> clientRequest = new HashMap<>();
        clientRequest.put("matricule", formData.get("matricule"));
        clientRequest.put("nom", formData.get("nom"));
        clientRequest.put("prenom", formData.get("prenom"));
        clientRequest.put("dateNaissance", formData.get("dateNaissance"));
        clientRequest.put("adresse", formData.get("adresse"));
        clientRequest.put("email", formData.get("email"));
        clientRequest.put("telephone", formData.get("telephone"));

        try {
            restTemplate.put(API_BASE_URL + "/clients/" + id, clientRequest);
        } catch (Exception e) {
            // Gérer l'erreur
        }

        return "redirect:/clients/" + id;
    }

    // Suppression d'un client
    @PostMapping("/{id}/supprimer")
    public String supprimerClient(@PathVariable Integer id) {
        try {
            restTemplate.delete(API_BASE_URL + "/clients/" + id);
        } catch (Exception e) {
            // Gérer l'erreur
        }

        return "redirect:/clients";
    }
}