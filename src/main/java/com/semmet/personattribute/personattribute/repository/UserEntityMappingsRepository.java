package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.UserEntityMappings;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityMappingsRepository extends CrudRepository<UserEntityMappings, Long>{
    
    public List<UserEntityMappings> findByUser_UserIdAndEntity_Id(long userId, long entityId);

    public List<UserEntityMappings> findAllByUser_UserId(long userId);
}
