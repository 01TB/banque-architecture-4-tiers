package com.management.comptemanagement.persistance.repository;

import com.management.comptemanagement.entity.Direction;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface DirectionRepository {
    List<Direction> findAll();
    Direction findById(int id);
    Direction save(Direction direction);
    Direction update(Direction direction);
    void delete(int id);
}