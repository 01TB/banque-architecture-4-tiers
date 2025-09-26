package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.TypeMouvementCompteDepot;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface TypeMouvementCompteDepotRepository {
    List<TypeMouvementCompteDepot> findAll();
    TypeMouvementCompteDepot findById(int id);
    TypeMouvementCompteDepot save(TypeMouvementCompteDepot typeMouvementCompteDepot);
    TypeMouvementCompteDepot update(TypeMouvementCompteDepot typeMouvementCompteDepot);
    void delete(int id);
}
