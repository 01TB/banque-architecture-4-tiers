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
}