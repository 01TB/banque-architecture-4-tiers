package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.MouvementCompteDepot;
import com.management.comptemanagement.persistance.repository.MouvementCompteDepotRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class MouvementCompteDepotRepositoryImp implements MouvementCompteDepotRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<MouvementCompteDepot> findAll() {
        TypedQuery<MouvementCompteDepot> query = em.createQuery("SELECT m FROM MouvementCompteDepot m ORDER BY m.id", MouvementCompteDepot.class);
        return query.getResultList();
    }

    @Override
    public MouvementCompteDepot findById(int id) {
        return em.find(MouvementCompteDepot.class, id);
    }

    @Override
    public MouvementCompteDepot save(MouvementCompteDepot client) {
        em.persist(client);
        return client;
    }

    @Override
    public MouvementCompteDepot update(MouvementCompteDepot client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        MouvementCompteDepot client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }
}

