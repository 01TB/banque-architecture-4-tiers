package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.Pret;
import com.management.comptemanagement.persistance.repository.PretRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class PretRepositoryImp implements PretRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<Pret> findAll() {
        TypedQuery<Pret> query = em.createQuery("SELECT p FROM Pret p ORDER BY p.id", Pret.class);
        return query.getResultList();
    }

    @Override
    public Pret findById(int id) {
        return em.find(Pret.class, id);
    }

    @Override
    public Pret save(Pret pret) {
        em.persist(pret);
        return pret;
    }

    @Override
    public Pret update(Pret pret) {
        return em.merge(pret);
    }

    @Override
    public void delete(int id) {
        Pret pret = findById(id);
        if (pret != null) {
            em.remove(pret);
        }
    }
}

