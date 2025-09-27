package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.RemboursementPret;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface RemboursementPretRepository {
    List<RemboursementPret> findAll();
    RemboursementPret findById(int id);
    RemboursementPret save(RemboursementPret remboursementPret);
    RemboursementPret update(RemboursementPret remboursementPret);
    void delete(int id);
}
