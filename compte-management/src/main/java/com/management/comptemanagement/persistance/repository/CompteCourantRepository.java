package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.CompteCourant;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface CompteCourantRepository {
    List<CompteCourant> findAll();
    CompteCourant findById(int id);
    CompteCourant save(CompteCourant compteCourant);
    CompteCourant update(CompteCourant compteCourant);
    void delete(int id);

    CompteCourant findByIdClient(int idClient);
}
