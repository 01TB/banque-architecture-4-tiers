package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.CompteCourant;
import com.management.comptemanagement.persistance.repository.CompteCourantRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CompteCourantRepositoryImp implements CompteCourantRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<CompteCourant> findAll() {
        TypedQuery<CompteCourant> query = em.createQuery("SELECT c FROM CompteCourant c ORDER BY c.id", CompteCourant.class);
        return query.getResultList();
    }

    @Override
    public CompteCourant findById(int id) {
        return em.find(CompteCourant.class, id);
    }

    @Override
    public CompteCourant save(CompteCourant compteCourant) {
        em.persist(compteCourant);
        return compteCourant;
    }

    @Override
    public CompteCourant update(CompteCourant compteCourant) {
        return em.merge(compteCourant);
    }

    @Override
    public void delete(int id) {
        CompteCourant compteCourant = findById(id);
        if (compteCourant != null) {
            em.remove(compteCourant);
        }
    }
}

