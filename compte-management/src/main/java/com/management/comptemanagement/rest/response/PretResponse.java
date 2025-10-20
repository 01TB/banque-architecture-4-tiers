package com.management.comptemanagement.rest.response;

import com.management.comptemanagement.entity.Pret;

import java.math.BigDecimal;
import java.time.Instant;

public class PretResponse {

    private Integer idPret;
    private BigDecimal montantPret;
    private BigDecimal tauxInteretAnnuel;
    private Integer periodiciteRemboursement;
    private Instant dateCreation;
    private Instant dateFermeture;

    public PretResponse(Pret pret) {
        this.idPret = pret.getId();
        this.montantPret = pret.getMontantPret();
        this.tauxInteretAnnuel = pret.getTauxInteretAnnuel();
        this.tauxInteretAnnuel = pret.getTauxInteretAnnuel();
        this.dateCreation = pret.getDateCreation();
        this.dateFermeture = pret.getDateFermeture();
    }

    public Integer getIdPret() {
        return idPret;
    }

    public void setIdPret(Integer idPret) {
        this.idPret = idPret;
    }

    public BigDecimal getMontantPret() {
        return montantPret;
    }

    public void setMontantPret(BigDecimal montantPret) {
        this.montantPret = montantPret;
    }

    public BigDecimal getTauxInteretAnnuel() {
        return tauxInteretAnnuel;
    }

    public void setTauxInteretAnnuel(BigDecimal tauxInteretAnnuel) {
        this.tauxInteretAnnuel = tauxInteretAnnuel;
    }

    public Integer getPeriodiciteRemboursement() {
        return periodiciteRemboursement;
    }

    public void setPeriodiciteRemboursement(Integer periodiciteRemboursement) {
        this.periodiciteRemboursement = periodiciteRemboursement;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }
}
