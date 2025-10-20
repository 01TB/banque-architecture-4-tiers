package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.Utilisateur;
import com.management.comptemanagement.persistance.repository.UtilisateurRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UtilisateurImp implements UtilisateurRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<Utilisateur> findAll() {
        TypedQuery<Utilisateur> query = em.createQuery("SELECT u FROM Utilisateur u ORDER BY u.id", Utilisateur.class);
        return query.getResultList();
    }

    @Override
    public Utilisateur findById(int id) {
        return em.find(Utilisateur.class, id);
    }

    @Override
    public Utilisateur save(Utilisateur client) {
        em.persist(client);
        em.flush(); // Pour obtenir l'ID immédiatement
        return client;
    }

    @Override
    public Utilisateur update(Utilisateur client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        Utilisateur client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }
}