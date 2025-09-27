package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.TypeMouvementCompteCourant;
import com.management.comptemanagement.persistance.repository.TypeMouvementCompteCourantRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class TypeMouvementCompteCourantRepositoryImp implements TypeMouvementCompteCourantRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<TypeMouvementCompteCourant> findAll() {
        TypedQuery<TypeMouvementCompteCourant> query = em.createQuery("SELECT t FROM TypeMouvementCompteCourant t ORDER BY t.id", TypeMouvementCompteCourant.class);
        return query.getResultList();
    }

    @Override
    public TypeMouvementCompteCourant findById(int id) {
        return em.find(TypeMouvementCompteCourant.class, id);
    }

    @Override
    public TypeMouvementCompteCourant save(TypeMouvementCompteCourant typeMouvementCompteCourant) {
        em.persist(typeMouvementCompteCourant);
        return typeMouvementCompteCourant;
    }

    @Override
    public TypeMouvementCompteCourant update(TypeMouvementCompteCourant typeMouvementCompteCourant) {
        return em.merge(typeMouvementCompteCourant);
    }

    @Override
    public void delete(int id) {
        TypeMouvementCompteCourant typeMouvementCompteCourant = findById(id);
        if (typeMouvementCompteCourant != null) {
            em.remove(typeMouvementCompteCourant);
        }
    }
}

