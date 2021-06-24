package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.Entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * EntitiesRepository class implements crud repository for the Entities model
 * @see Entities
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Repository
public interface EntitiesRepository extends CrudRepository<Entities, Long> {
    
    /**
     * This method searches for and returns the entity record that matches the
     * entity string column for that record.
     * 
     * @param entity entity string that is searched for in the entities table
     * @return list of all the entities that match the string given as the argument.
     * The returned list should only contain one entry.
     */

    public List<Entities> findByEntity(String entity);
}
