package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.StatutPret;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface StatutPretRepository {
    List<StatutPret> findAll();
    StatutPret findById(int id);
    StatutPret save(StatutPret statutPret);
    StatutPret update(StatutPret statutPret);
    void delete(int id);
}
