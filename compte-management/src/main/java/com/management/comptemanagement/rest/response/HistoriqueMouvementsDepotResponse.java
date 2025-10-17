package com.management.comptemanagement.rest.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class HistoriqueMouvementsDepotResponse {
    private Integer idClient;
    private String numeroCompte;
    private BigDecimal soldeBrut;
    private BigDecimal soldeReel;
    private BigDecimal interetsCumules;
    private List<MouvementDetail> mouvements;

    // Constructeurs
    public HistoriqueMouvementsDepotResponse() {}

    public HistoriqueMouvementsDepotResponse(Integer idClient, String numeroCompte, BigDecimal soldeBrut,
                                             BigDecimal soldeReel, BigDecimal interetsCumules,
                                             List<MouvementDetail> mouvements) {
        this.idClient = idClient;
        this.numeroCompte = numeroCompte;
        this.soldeBrut = soldeBrut;
        this.soldeReel = soldeReel;
        this.interetsCumules = interetsCumules;
        this.mouvements = mouvements;
    }

    // Getters et Setters
    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public BigDecimal getSoldeBrut() {
        return soldeBrut;
    }

    public void setSoldeBrut(BigDecimal soldeBrut) {
        this.soldeBrut = soldeBrut;
    }

    public BigDecimal getSoldeReel() {
        return soldeReel;
    }

    public void setSoldeReel(BigDecimal soldeReel) {
        this.soldeReel = soldeReel;
    }

    public BigDecimal getInteretsCumules() {
        return interetsCumules;
    }

    public void setInteretsCumules(BigDecimal interetsCumules) {
        this.interetsCumules = interetsCumules;
    }

    public List<MouvementDetail> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<MouvementDetail> mouvements) {
        this.mouvements = mouvements;
    }

    // Classe interne pour les détails des mouvements
    public static class MouvementDetail {
        private Integer idMouvement;
        private BigDecimal montant;
        private String description;
        private Instant dateMouvement;
        private String typeMouvement;

        // Constructeurs
        public MouvementDetail() {}

        public MouvementDetail(Integer idMouvement, BigDecimal montant, String description,
                               Instant dateMouvement, String typeMouvement) {
            this.idMouvement = idMouvement;
            this.montant = montant;
            this.description = description;
            this.dateMouvement = dateMouvement;
            this.typeMouvement = typeMouvement;
        }

        // Getters et Setters
        public Integer getIdMouvement() {
            return idMouvement;
        }

        public void setIdMouvement(Integer idMouvement) {
            this.idMouvement = idMouvement;
        }

        public BigDecimal getMontant() {
            return montant;
        }

        public void setMontant(BigDecimal montant) {
            this.montant = montant;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Instant getDateMouvement() {
            return dateMouvement;
        }

        public void setDateMouvement(Instant dateMouvement) {
            this.dateMouvement = dateMouvement;
        }

        public String getTypeMouvement() {
            return typeMouvement;
        }

        public void setTypeMouvement(String typeMouvement) {
            this.typeMouvement = typeMouvement;
        }
    }
}