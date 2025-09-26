package com.management.comptemanagement.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "historique_statut_pret", schema = "public")
public class HistoriqueStatutPret {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date_modification", nullable = false)
    private Instant dateModification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_statut", nullable = false)
    private com.management.comptemanagement.entity.StatutPret idStatut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pret", nullable = false)
    private com.management.comptemanagement.entity.Pret idPret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public com.management.comptemanagement.entity.StatutPret getIdStatut() {
        return idStatut;
    }

    public void setIdStatut(com.management.comptemanagement.entity.StatutPret idStatut) {
        this.idStatut = idStatut;
    }

    public com.management.comptemanagement.entity.Pret getIdPret() {
        return idPret;
    }

    public void setIdPret(com.management.comptemanagement.entity.Pret idPret) {
        this.idPret = idPret;
    }

}