package com.management.comptemanagement.rest.response;

import java.math.BigDecimal;
import java.time.Instant;

public class MouvementResponse {
    private Integer idMouvement;
    private BigDecimal montant;
    private String description;
    private Instant dateMouvement;
    private String typeMouvement;

    public MouvementResponse() {
    }

    public MouvementResponse(Integer idMouvement, BigDecimal montant, String description, Instant dateMouvement, String typeMouvement) {
        this.idMouvement = idMouvement;
        this.montant = montant;
        this.description = description;
        this.dateMouvement = dateMouvement;
        this.typeMouvement = typeMouvement;
    }

    public BigDecimal getMontant() {return montant;}
    public void setMontant(BigDecimal montant) {this.montant = montant;}

    public Integer getIdMouvement() {return idMouvement;}
    public void setIdMouvement(Integer idMouvement) {this.idMouvement = idMouvement;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Instant getDateMouvement() {return dateMouvement;}
    public void setDateMouvement(Instant dateMouvement) {this.dateMouvement = dateMouvement;}

    public String getTypeMouvement() {return typeMouvement;}
    public void setTypeMouvement(String typeMouvement) {this.typeMouvement = typeMouvement;}
}