package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.TypeMouvementCompteCourant;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface TypeMouvementCompteCourantRepository {
    List<TypeMouvementCompteCourant> findAll();
    TypeMouvementCompteCourant findById(int id);
    TypeMouvementCompteCourant save(TypeMouvementCompteCourant typeMouvementCompteCourant);
    TypeMouvementCompteCourant update(TypeMouvementCompteCourant typeMouvementCompteCourant);
    void delete(int id);
}
