package com.management.comptemanagement.persistance.implementation;

import com.management.comptemanagement.entity.Client;
import com.management.comptemanagement.persistance.repository.ClientRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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
        em.flush(); // Pour obtenir l'ID immédiatement
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

    @Override
    public List<Client> findByCriteria(String nom, String prenom, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<Client> client = cq.from(Client.class);

        List<Predicate> predicates = new ArrayList<>();

        // Ajout des critères de recherche seulement s'ils ne sont pas null ou vides
        if (nom != null && !nom.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(client.get("nom")), "%" + nom.toLowerCase() + "%"));
        }

        if (prenom != null && !prenom.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(client.get("prenom")), "%" + prenom.toLowerCase() + "%"));
        }

        if (email != null && !email.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(client.get("email")), "%" + email.toLowerCase() + "%"));
        }

        // Si aucun critère n'est spécifié, retourner tous les clients
        if (predicates.isEmpty()) {
            return findAll();
        }

        // Application des prédicats et tri par ID
        cq.where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.asc(client.get("id")));

        TypedQuery<Client> query = em.createQuery(cq);
        return query.getResultList();
    }
}