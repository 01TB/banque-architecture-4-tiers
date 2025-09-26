package com.management.comptemanagement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "mouvement_compte_courant", schema = "public")
public class MouvementCompteCourant {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date_mouvement", nullable = false)
    private Instant dateMouvement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_type_mouvement", nullable = false)
    private com.management.comptemanagement.entity.TypeMouvementCompteCourant idTypeMouvement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compte_courant", nullable = false)
    private CompteCourant idCompteCourant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public com.management.comptemanagement.entity.TypeMouvementCompteCourant getIdTypeMouvement() {
        return idTypeMouvement;
    }

    public void setIdTypeMouvement(com.management.comptemanagement.entity.TypeMouvementCompteCourant idTypeMouvement) {
        this.idTypeMouvement = idTypeMouvement;
    }

    public CompteCourant getIdCompteCourant() {
        return idCompteCourant;
    }

    public void setIdCompteCourant(CompteCourant idCompteCourant) {
        this.idCompteCourant = idCompteCourant;
    }

}