package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.ConfigurationCompteDepot;
import com.management.comptemanagement.persistance.repository.ConfigurationCompteDepotRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class ConfigurationCompteDepotRepositoryImp implements ConfigurationCompteDepotRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<ConfigurationCompteDepot> findAll() {
        TypedQuery<ConfigurationCompteDepot> query = em.createQuery("SELECT c FROM ConfigurationCompteDepot c ORDER BY c.id", ConfigurationCompteDepot.class);
        return query.getResultList();
    }

    @Override
    public ConfigurationCompteDepot findById(int id) {
        return em.find(ConfigurationCompteDepot.class, id);
    }

    @Override
    public ConfigurationCompteDepot save(ConfigurationCompteDepot configurationCompteDepot) {
        em.persist(configurationCompteDepot);
        return configurationCompteDepot;
    }

    @Override
    public ConfigurationCompteDepot update(ConfigurationCompteDepot configurationCompteDepot) {
        return em.merge(configurationCompteDepot);
    }

    @Override
    public void delete(int id) {
        ConfigurationCompteDepot configurationCompteDepot = findById(id);
        if (configurationCompteDepot != null) {
            em.remove(configurationCompteDepot);
        }
    }

    @Override
    public ConfigurationCompteDepot findLatestByCompteDepotId(int idCompteDepot) {
        TypedQuery<ConfigurationCompteDepot> query = em.createQuery(
                "SELECT c FROM ConfigurationCompteDepot c WHERE c.idCompteDepot.id = :idCompteDepot ORDER BY c.dateApplication DESC",
                ConfigurationCompteDepot.class);
        query.setParameter("idCompteDepot", idCompteDepot);
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}


