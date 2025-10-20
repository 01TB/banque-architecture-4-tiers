package com.management.comptemanagement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "mouvement_compte_depot", schema = "public")
public class MouvementCompteDepot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "date_mouvement", nullable = false)
    private Instant dateMouvement;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_type_mouvement", nullable = false)
    private TypeMouvementCompteDepot idTypeMouvement;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_compte_depot", nullable = false)
    private CompteDepot idCompteDepot;

    @Column(name = "actif")
    private Boolean actif;

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

    public TypeMouvementCompteDepot getIdTypeMouvement() {
        return idTypeMouvement;
    }

    public void setIdTypeMouvement(TypeMouvementCompteDepot idTypeMouvement) {
        this.idTypeMouvement = idTypeMouvement;
    }

    public CompteDepot getIdCompteDepot() {
        return idCompteDepot;
    }

    public void setIdCompteDepot(CompteDepot idCompteDepot) {
        this.idCompteDepot = idCompteDepot;
    }

    public Boolean getActif() {return actif;}
    public void setActif(Boolean actif) {this.actif = actif;}

}