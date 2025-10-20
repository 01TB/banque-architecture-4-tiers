package com.management.comptemanagement.rest.response;

import com.management.comptemanagement.entity.Utilisateur;

public class LoginResponse {
    private String message;
    private Utilisateur utilisateur;

    public LoginResponse(String message, Utilisateur utilisateur) {
        this.message = message;
        this.utilisateur = utilisateur;
    }

    // Getters et setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}