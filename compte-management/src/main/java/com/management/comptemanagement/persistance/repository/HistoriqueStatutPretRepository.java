package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.HistoriqueStatutPret;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface HistoriqueStatutPretRepository {
    List<HistoriqueStatutPret> findAll();
    HistoriqueStatutPret findById(int id);
    HistoriqueStatutPret save(HistoriqueStatutPret historiqueStatutPret);
    HistoriqueStatutPret update(HistoriqueStatutPret historiqueStatutPret);
    void delete(int id);
}
