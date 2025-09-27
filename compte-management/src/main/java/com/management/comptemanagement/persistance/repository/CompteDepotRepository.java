package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.CompteDepot;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface CompteDepotRepository {
    List<CompteDepot> findAll();
    CompteDepot findById(int id);
    CompteDepot save(CompteDepot compteDepot);
    CompteDepot update(CompteDepot compteDepot);
    void delete(int id);

    CompteDepot findByIdClient(int idClient);
}

