package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.*;

@Controller
@RequestMapping("/auth")
public class UtilisateurController {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:8090/compte-management-1.0-SNAPSHOT/api";

    // Stockage de session (simplifié - dans une vraie app, utiliser HttpSession)
    private Map<String, Object> sessionData = new HashMap<>();

    @Autowired
    public UtilisateurController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Vérifier si déjà connecté
        if (sessionData.containsKey("utilisateur")) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String matricule,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        try {
            // Préparer la requête de login
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("matricule", matricule);
            loginRequest.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, headers);

            // Appeler l'API de login
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_BASE_URL + "/utilisateurs/login",
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();

                // Stocker les données de session
                sessionData.put("utilisateur", responseBody.get("utilisateur"));

                // Récupérer les permissions
                try {
                    ResponseEntity<Map> permissionsResponse = restTemplate.getForEntity(
                            API_BASE_URL + "/utilisateurs/permissions",
                            Map.class
                    );

                    if (permissionsResponse.getStatusCode().is2xxSuccessful() && permissionsResponse.getBody() != null) {
                        Map<String, Object> permissionsBody = permissionsResponse.getBody();
                        sessionData.put("directions", permissionsBody.get("directions"));
                        sessionData.put("actionsRoles", permissionsBody.get("actionsRoles"));
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la récupération des permissions: " + e.getMessage());
                }

                redirectAttributes.addFlashAttribute("success", "Connexion réussie !");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Matricule ou mot de passe incorrect");
                return "redirect:/auth/login";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de connexion: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        try {
            // Appeler l'API de logout
            restTemplate.postForEntity(
                    API_BASE_URL + "/utilisateurs/logout",
                    null,
                    Map.class
            );
        } catch (Exception e) {
            System.out.println("Erreur lors de la déconnexion backend: " + e.getMessage());
        }

        // Nettoyer la session frontend
        sessionData.clear();
        redirectAttributes.addFlashAttribute("success", "Déconnexion réussie");
        return "redirect:/auth/login";
    }

    // Méthode utilitaire pour vérifier si l'utilisateur est connecté
    public boolean isUserLoggedIn() {
        return sessionData.containsKey("utilisateur");
    }

    // Méthode pour récupérer les données de session
    public Map<String, Object> getSessionData() {
        return sessionData;
    }
}