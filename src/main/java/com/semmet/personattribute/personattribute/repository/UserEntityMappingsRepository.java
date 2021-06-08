package com.semmet.personattribute.personattribute.repository;

import com.semmet.personattribute.personattribute.model.UserEntityMappings;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityMappingsRepository extends CrudRepository<UserEntityMappings, Long>{
    
}
