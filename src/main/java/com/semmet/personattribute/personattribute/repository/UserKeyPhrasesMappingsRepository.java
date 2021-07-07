package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.KeyPhrases;
import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.model.Users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserKeyPhrasesMappingsRepository class implements crud repository for the
 * UserKeyPhrasesMappings model
 * @see UserKeyPhraseMappings
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Repository
public interface UserKeyPhrasesMappingsRepository extends CrudRepository<UserKeyPhraseMappings, Long> {

    /**
     * This method searches for and returns the user key phrase mappings record that matches the
     * userId and the keyPhraseId columns for that record.
     * @see Users
     * @see KeyPhrases
     * 
     * @param userId userId against which records' columns are matched
     * @param keyPhraseId keyPhraseId against which records' columns are matched
     * @return list of all the user key phrase mappings that match the
     * userId and the keyPhraseID given as the argument. The returned list should
     * only contain one entry.
     */

    public List<UserKeyPhraseMappings> findByUser_UserIdAndKeyPhrase_Id(long userId, long keyPhraseId);

    /**
     * This method searches for and returns all the user key phrase mappings records
     * that matches the userId column for that record.
     * @see Users
     * 
     * @param userId userId against which records' columns are matched
     * @return list of all the user key phrase mappings that match the
     * userId given as the argument. The returned list should
     * only contain one entry.
     */

    public List<UserKeyPhraseMappings> findAllByUser_UserId(long userId);
}
