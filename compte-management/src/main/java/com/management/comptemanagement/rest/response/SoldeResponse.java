package com.management.comptemanagement.rest.response;

import java.math.BigDecimal;

public class SoldeResponse {
    private BigDecimal solde;
    private String message;

    public SoldeResponse(BigDecimal solde) {
        this.solde = solde;
        this.message = "Opération réussie";
    }

    // Getters et Setters
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}