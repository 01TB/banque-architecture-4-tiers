package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.MouvementCompteDepot;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface MouvementCompteDepotRepository {
    List<MouvementCompteDepot> findAll();
    MouvementCompteDepot findById(int id);
    MouvementCompteDepot save(MouvementCompteDepot mouvementCompteDepot);
    MouvementCompteDepot update(MouvementCompteDepot mouvementCompteDepot);
    void delete(int id);
}
