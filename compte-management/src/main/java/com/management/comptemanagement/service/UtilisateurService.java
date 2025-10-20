package com.management.comptemanagement.service;

import com.management.comptemanagement.entity.ActionRole;
import com.management.comptemanagement.entity.Direction;
import com.management.comptemanagement.entity.Utilisateur;
import com.management.comptemanagement.persistance.repository.ActionRoleRepository;
import com.management.comptemanagement.persistance.repository.DirectionRepository;
import com.management.comptemanagement.persistance.repository.UtilisateurRepository;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.ejb.Remove;
import java.util.List;

@Stateful
public class UtilisateurService {

    @EJB
    private UtilisateurRepository utilisateurRepository;

    @EJB
    private DirectionRepository directionRepository;

    @EJB
    private ActionRoleRepository actionRoleRepository;

    // Variables de session
    private Utilisateur utilisateurConnecte;
    private List<Direction> toutesDirections;
    private List<ActionRole> toutesActionsRoles;
    private boolean sessionActive = false;

    // Getters pour accéder aux données de session
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public List<Direction> getToutesDirections() {
        return toutesDirections;
    }

    public List<ActionRole> getToutesActionsRoles() {
        return toutesActionsRoles;
    }

    public boolean login(String matricule, String password) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getMatricule().equals(matricule) &&
                    utilisateur.getPassword().equals(password)) {

                // Authentification réussie
                this.utilisateurConnecte = utilisateur;
                this.toutesDirections = directionRepository.findAll();
                this.toutesActionsRoles = actionRoleRepository.findAll();
                this.sessionActive = true;

                return true;
            }
        }
        return false;
    }

    /**
     * Méthode de déconnexion - réinitialise toutes les variables de session
     */
    @Remove
    public void logout() {
        this.utilisateurConnecte = null;
        this.toutesDirections = null;
        this.toutesActionsRoles = null;
        this.sessionActive = false;
        System.out.println("Session utilisateur fermée avec succès");
    }

    /**
     * Vérifie si une session est active
     */
    public boolean isSessionActive() {
        return sessionActive && utilisateurConnecte != null;
    }

    /**
     * Méthode appelée automatiquement avant la destruction du bean
     */
    @PreDestroy
    public void cleanup() {
        System.out.println("Nettoyage de la session UtilisateurService");
        logout();
    }
}