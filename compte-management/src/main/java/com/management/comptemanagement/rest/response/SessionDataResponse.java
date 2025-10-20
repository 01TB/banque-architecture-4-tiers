package com.management.comptemanagement.rest.response;

import com.management.comptemanagement.entity.ActionRole;
import com.management.comptemanagement.entity.Direction;
import com.management.comptemanagement.entity.Utilisateur;
import java.util.List;

public class SessionDataResponse {
    private Utilisateur utilisateur;
    private List<Direction> directions;
    private List<ActionRole> actionsRoles;

    public SessionDataResponse(Utilisateur utilisateur, List<Direction> directions, List<ActionRole> actionsRoles) {
        this.utilisateur = utilisateur;
        this.directions = directions;
        this.actionsRoles = actionsRoles;
    }

    // Getters et setters
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public List<ActionRole> getActionsRoles() {
        return actionsRoles;
    }

    public void setActionsRoles(List<ActionRole> actionsRoles) {
        this.actionsRoles = actionsRoles;
    }
}