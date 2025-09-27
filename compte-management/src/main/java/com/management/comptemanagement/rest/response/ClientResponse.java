package com.management.comptemanagement.rest.response;

import com.management.comptemanagement.entity.Client;
import java.time.LocalDate;
import java.time.Instant;

public class ClientResponse {
    private Integer id;
    private String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String adresse;
    private String email;
    private String telephone;
    private Instant dateCreation;

    public ClientResponse(Client client) {
        this.id = client.getId();
        this.matricule = client.getMatricule();
        this.nom = client.getNom();
        this.prenom = client.getPrenom();
        this.dateNaissance = client.getDateNaissance();
        this.adresse = client.getAdresse();
        this.email = client.getEmail();
        this.telephone = client.getTelephone();
        this.dateCreation = client.getDateCreation();
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Instant getDateCreation() { return dateCreation; }
    public void setDateCreation(Instant dateCreation) { this.dateCreation = dateCreation; }
}