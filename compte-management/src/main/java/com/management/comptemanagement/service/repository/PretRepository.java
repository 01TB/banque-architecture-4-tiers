package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.Pret;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface PretRepository {
    List<Pret> findAll();
    Pret findById(int id);
    Pret save(Pret pret);
    Pret update(Pret pret);
    void delete(int id);
}
