package com.semmet.personattribute.personattribute.repository;

import java.util.List;

import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKeyPhrasesMappingsRepository extends CrudRepository<UserKeyPhraseMappings, Long> {

    public List<UserKeyPhraseMappings> findByUserIdAndKeyPhraseId(long userId, long keyPhraseId);
}
