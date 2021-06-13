package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.KeyPhrases;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyPhrasesRepository extends CrudRepository<KeyPhrases, Long> {
    
    public List<KeyPhrases> findByKeyPhrase(String keyPhrase);
}
