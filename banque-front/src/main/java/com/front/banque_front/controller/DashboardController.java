package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final RestTemplate restTemplate;

    private final String API_BASE_URL = "http://localhost:8090/compte-management-1.0-SNAPSHOT/api";

    public DashboardController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String dashboard(Model model) {
        try {
            // Récupérer tous les clients
            Map<String, Object> clientsResponse = restTemplate.getForObject(
                    API_BASE_URL + "/clients",
                    Map.class
            );

            if (clientsResponse != null) {
                // Nombre total de clients
                Integer clientCount = (Integer) clientsResponse.get("count");
                model.addAttribute("clientCount", clientCount);

                // Liste des clients récents
                List<Map<String, Object>> clients = (List<Map<String, Object>>) clientsResponse.get("clients");
                model.addAttribute("recentClients", clients);

                // Calcul des totaux
                double totalSolde = 0;
                double totalComptesCourants = 0;
                double totalComptesDepot = 0;
                double totalInterets = 0;
                double totalPrets = 0;

                for (Map<String, Object> client : clients) {
                    Integer clientId = (Integer) client.get("id");

                    // Solde total du client
                    Map<String, Object> soldeResponse = restTemplate.getForObject(
                            API_BASE_URL + "/clients/" + clientId + "/solde-totale",
                            Map.class
                    );
                    if (soldeResponse != null) {
                        totalSolde += (Double) soldeResponse.get("solde");
                    }

                    // Solde compte courant
                    Map<String, Object> ccResponse = restTemplate.getForObject(
                            API_BASE_URL + "/comptes-courants/client/" + clientId + "/solde-brut",
                            Map.class
                    );
                    if (ccResponse != null) {
                        totalComptesCourants += (Double) ccResponse.get("solde");
                    }

                    // Solde compte dépôt
                    Map<String, Object> cdResponse = restTemplate.getForObject(
                            API_BASE_URL + "/comptes-depot/client/" + clientId + "/solde-brut",
                            Map.class
                    );
                    if (cdResponse != null) {
                        totalComptesDepot += (Double) cdResponse.get("solde");
                    }

                    // Intérêts compte dépôt
                    Map<String, Object> interetsResponse = restTemplate.getForObject(
                            API_BASE_URL + "/comptes-depot/client/" + clientId + "/interets",
                            Map.class
                    );
                    if (interetsResponse != null) {
                        totalInterets += (Double) interetsResponse.get("solde");
                    }

                    // Prêts
                    Map<String, Object> pretsResponse = restTemplate.getForObject(
                            API_BASE_URL + "/prets/client/" + clientId + "/somme",
                            Map.class
                    );
                    if (pretsResponse != null) {
                        totalPrets += (Double) pretsResponse.get("solde");
                    }
                }

                model.addAttribute("totalSolde", totalSolde);
                model.addAttribute("totalComptesCourants", totalComptesCourants);
                model.addAttribute("totalComptesDepot", totalComptesDepot);
                model.addAttribute("totalInterets", totalInterets);
                model.addAttribute("totalPrets", totalPrets);
            }

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du dashboard");
        }

        return "dashboard";
    }
}