package com.management.comptemanagement.rest.request;

import java.math.BigDecimal;
import java.time.Instant;

public class PretRequest {

    private Integer idClient;
    private BigDecimal montantPret;
    private BigDecimal tauxInteretAnnuel;
    private Integer periodiciteRemboursement;
    private Instant dateCreation;

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public BigDecimal getMontantPret() {
        return montantPret;
    }

    public void setMontantPret(BigDecimal montantPret) {
        this.montantPret = montantPret;
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

    public BigDecimal getTauxInteretAnnuel() {
        return tauxInteretAnnuel;
    }

    public void setTauxInteretAnnuel(BigDecimal tauxInteretAnnuel) {
        this.tauxInteretAnnuel = tauxInteretAnnuel;
    }
}
