package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.model.Users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserEntityMappingsRepository class implements crud repository for the UserEntityMappings model
 * @see UserEntityMappings
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Repository
public interface UserEntityMappingsRepository extends CrudRepository<UserEntityMappings, Long>{
    
    /**
     * This method searches for and returns the user entity mappings record that matches the
     * userId and the entityId columns for that record.
     * @see Users
     * @see Entities
     * 
     * @param userId userId against which records' columns are matched
     * @param entityId entityId against which records' columns are matched
     * @return list of all the user entity mappings that match the
     * userId and the entityID given as the argument. The returned list should
     * only contain one entry.
     */

    public List<UserEntityMappings> findByUser_UserIdAndEntity_Id(long userId, long entityId);

    /**
     * This method searches for and returns all the user entity mappings records
     * that matches the userId column for that record.
     * @see Users
     * 
     * @param userId userId against which records' columns are matched
     * @return list of all the user entity mappings that match the
     * userId given as the argument. The returned list should
     * only contain one entry.
     */

    public List<UserEntityMappings> findAllByUser_UserId(long userId);
}
