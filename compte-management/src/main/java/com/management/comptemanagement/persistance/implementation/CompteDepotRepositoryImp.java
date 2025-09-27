package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.CompteDepot;
import com.management.comptemanagement.persistance.repository.CompteDepotRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CompteDepotRepositoryImp implements CompteDepotRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<CompteDepot> findAll() {
        TypedQuery<CompteDepot> query = em.createQuery("SELECT c FROM CompteDepot c ORDER BY c.id", CompteDepot.class);
        return query.getResultList();
    }

    @Override
    public CompteDepot findById(int id) {
        return em.find(CompteDepot.class, id);
    }

    @Override
    public CompteDepot save(CompteDepot client) {
        em.persist(client);
        return client;
    }

    @Override
    public CompteDepot update(CompteDepot client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        CompteDepot client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }

    @Override
    public CompteDepot findByIdClient(int idClient) {
        TypedQuery<CompteDepot> query = em.createQuery(
                "SELECT c FROM CompteDepot c WHERE c.idClient.id = :idClient", CompteDepot.class);
        query.setParameter("idClient", idClient);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

