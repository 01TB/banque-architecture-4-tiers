package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.HistoriqueStatutPret;
import com.management.comptemanagement.persistance.repository.HistoriqueStatutPretRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
@Stateless
public class HistoriqueStatutPretRepositoryImp implements HistoriqueStatutPretRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<HistoriqueStatutPret> findAll() {
        TypedQuery<HistoriqueStatutPret> query = em.createQuery("SELECT h FROM HistoriqueStatutPret h ORDER BY h.id", HistoriqueStatutPret.class);
        return query.getResultList();
    }

    @Override
    public HistoriqueStatutPret findById(int id) {
        return em.find(HistoriqueStatutPret.class, id);
    }

    @Override
    public HistoriqueStatutPret save(HistoriqueStatutPret historiqueStatutPret) {
        em.persist(historiqueStatutPret);
        return historiqueStatutPret;
    }

    @Override
    public HistoriqueStatutPret update(HistoriqueStatutPret historiqueStatutPret) {
        return em.merge(historiqueStatutPret);
    }

    @Override
    public void delete(int id) {
        HistoriqueStatutPret historiqueStatutPret = findById(id);
        if (historiqueStatutPret != null) {
            em.remove(historiqueStatutPret);
        }
    }
}

