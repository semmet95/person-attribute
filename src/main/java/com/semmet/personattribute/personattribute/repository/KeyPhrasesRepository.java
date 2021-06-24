package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.KeyPhrases;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * KeyPhrasesRepository class implements crud repository for the KeyPhrases model
 * @see KeyPhrases
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Repository
public interface KeyPhrasesRepository extends CrudRepository<KeyPhrases, Long> {
    
    /**
     * This method searches for and returns the key phrase record that matches the
     * key phrase string column for that record.
     * 
     * @param keyPhrase key phrase string that is searched for in the key phrases table
     * @return list of all the key phrases that match the string given as the argument.
     * The returned list should only contain one entry.
     */

    public List<KeyPhrases> findByKeyPhrase(String keyPhrase);
}
