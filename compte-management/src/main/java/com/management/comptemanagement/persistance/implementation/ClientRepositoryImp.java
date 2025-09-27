package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.Client;
import com.management.comptemanagement.persistance.repository.ClientRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClientRepositoryImp implements ClientRepository {

    @PersistenceContext(unitName = "BankBerthin")
    private EntityManager em;

    @Override
    public List<Client> findAll() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c ORDER BY c.id", Client.class);
        return query.getResultList();
    }

    @Override
    public Client findById(int id) {
        return em.find(Client.class, id);
    }

    @Override
    public Client save(Client client) {
        em.persist(client);
        return client;
    }

    @Override
    public Client update(Client client) {
        return em.merge(client);
    }

    @Override
    public void delete(int id) {
        Client client = findById(id);
        if (client != null) {
            em.remove(client);
        }
    }
}

