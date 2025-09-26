package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.MouvementCompteCourant;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface MouvementCompteCourantRepository {
    List<MouvementCompteCourant> findAll();
    MouvementCompteCourant findById(int id);
    MouvementCompteCourant save(MouvementCompteCourant mouvementCompteCourant);
    MouvementCompteCourant update(MouvementCompteCourant mouvementCompteCourant);
    void delete(int id);
}
