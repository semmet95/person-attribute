package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.Entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntitiesRepository extends CrudRepository<Entities, Long> {
    
    public List<Entities> findByEntity(String entity);
}
