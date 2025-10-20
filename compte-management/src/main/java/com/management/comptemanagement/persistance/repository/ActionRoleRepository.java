package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.ActionRole;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ActionRoleRepository {
    List<ActionRole> findAll();
    ActionRole findById(int id);
    ActionRole save(ActionRole actionRole);
    ActionRole update(ActionRole actionRole);
    void delete(int id);
}