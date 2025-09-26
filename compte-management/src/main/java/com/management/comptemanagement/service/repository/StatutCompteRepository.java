package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.StatutCompte;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface StatutCompteRepository {
    List<StatutCompte> findAll();
    StatutCompte findById(int id);
    StatutCompte save(StatutCompte statutCompte);
    StatutCompte update(StatutCompte statutCompte);
    void delete(int id);
}
