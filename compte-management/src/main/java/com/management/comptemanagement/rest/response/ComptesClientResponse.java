package com.management.comptemanagement.rest.response;

import java.math.BigDecimal;

public class ComptesClientResponse {
    private Integer clientId;
    private String numeroCompteCourant;
    private BigDecimal soldeCompteCourant;
    private String numeroCompteDepot;
    private BigDecimal soldeCompteDepot;
    private String message;

    public ComptesClientResponse(Integer clientId, String numeroCompteCourant,
                                 BigDecimal soldeCompteCourant, String numeroCompteDepot,
                                 BigDecimal soldeCompteDepot) {
        this.clientId = clientId;
        this.numeroCompteCourant = numeroCompteCourant;
        this.soldeCompteCourant = soldeCompteCourant;
        this.numeroCompteDepot = numeroCompteDepot;
        this.soldeCompteDepot = soldeCompteDepot;
        this.message = "Opération réussie";
    }

    // Getters et Setters
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    public String getNumeroCompteCourant() { return numeroCompteCourant; }
    public void setNumeroCompteCourant(String numeroCompteCourant) { this.numeroCompteCourant = numeroCompteCourant; }

    public BigDecimal getSoldeCompteCourant() { return soldeCompteCourant; }
    public void setSoldeCompteCourant(BigDecimal soldeCompteCourant) { this.soldeCompteCourant = soldeCompteCourant; }

    public String getNumeroCompteDepot() { return numeroCompteDepot; }
    public void setNumeroCompteDepot(String numeroCompteDepot) { this.numeroCompteDepot = numeroCompteDepot; }

    public BigDecimal getSoldeCompteDepot() { return soldeCompteDepot; }
    public void setSoldeCompteDepot(BigDecimal soldeCompteDepot) { this.soldeCompteDepot = soldeCompteDepot; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}