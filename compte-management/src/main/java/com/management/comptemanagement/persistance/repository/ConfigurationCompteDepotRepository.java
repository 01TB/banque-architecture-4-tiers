package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.ConfigurationCompteDepot;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ConfigurationCompteDepotRepository {
    List<ConfigurationCompteDepot> findAll();
    ConfigurationCompteDepot findById(int id);
    ConfigurationCompteDepot save(ConfigurationCompteDepot configurationCompteDepot);
    ConfigurationCompteDepot update(ConfigurationCompteDepot configurationCompteDepot);
    void delete(int id);
}
