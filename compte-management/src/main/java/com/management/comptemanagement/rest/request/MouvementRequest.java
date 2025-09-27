package com.management.comptemanagement.rest.request;

import java.math.BigDecimal;
import java.time.Instant;

public class MouvementRequest {
    private BigDecimal montant;
    private Instant dateMouvement;
    private String description;

    // Getters et Setters
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public Instant getDateMouvement() { return dateMouvement; }
    public void setDateMouvement(Instant dateMouvement) { this.dateMouvement = dateMouvement; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}