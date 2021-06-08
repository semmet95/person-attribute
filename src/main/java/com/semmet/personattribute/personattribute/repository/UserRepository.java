package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.Users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    
    public List<Users> findByUserId(long userId);
}
