package com.management.comptemanagement.service.repository;

import com.management.comptemanagement.entity.Client;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ClientRepository {
    List<Client> findAll();
    Client findById(int id);
    Client save(Client client);
    Client update(Client client);
    void delete(int id);
}
