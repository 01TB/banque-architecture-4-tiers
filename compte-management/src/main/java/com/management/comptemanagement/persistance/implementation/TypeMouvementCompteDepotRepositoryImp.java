package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.TypeMouvementCompteDepot;
import com.management.comptemanagement.persistance.repository.TypeMouvementCompteDepotRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class TypeMouvementCompteDepotRepositoryImp implements TypeMouvementCompteDepotRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<TypeMouvementCompteDepot> findAll() {
        TypedQuery<TypeMouvementCompteDepot> query = em.createQuery("SELECT t FROM TypeMouvementCompteDepot t ORDER BY t.id", TypeMouvementCompteDepot.class);
        return query.getResultList();
    }

    @Override
    public TypeMouvementCompteDepot findById(int id) {
        return em.find(TypeMouvementCompteDepot.class, id);
    }

    @Override
    public TypeMouvementCompteDepot save(TypeMouvementCompteDepot typeMouvementCompteDepot) {
        em.persist(typeMouvementCompteDepot);
        return typeMouvementCompteDepot;
    }

    @Override
    public TypeMouvementCompteDepot update(TypeMouvementCompteDepot typeMouvementCompteDepot) {
        return em.merge(typeMouvementCompteDepot);
    }

    @Override
    public void delete(int id) {
        TypeMouvementCompteDepot typeMouvementCompteDepot = findById(id);
        if (typeMouvementCompteDepot != null) {
            em.remove(typeMouvementCompteDepot);
        }
    }
}

