package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.Client;
import com.management.comptemanagement.entity.Utilisateur;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface UtilisateurRepository {
    List<Utilisateur> findAll();
    Utilisateur findById(int id);
    Utilisateur save(Utilisateur utilisateur);
    Utilisateur update(Utilisateur utilisateur);
    void delete(int id);
}