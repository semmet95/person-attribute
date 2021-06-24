package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.Users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository class implements crud repository for the
 * Users model
 * @see Users
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    
    /**
     * This method searches for and return the user
     * that matches the userId column for that record.
     * 
     * @param userId userId against which records' columns are matched
     * @return list of all the user records that match the
     * userId given as the argument. The returned list should
     * only contain one entry.
     */

    public List<Users> findByUserId(long userId);
}
