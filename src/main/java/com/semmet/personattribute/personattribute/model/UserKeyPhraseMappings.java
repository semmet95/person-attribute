package com.semmet.personattribute.personattribute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserKeyPhraseMappings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private long keyPhraseId;

    public UserKeyPhraseMappings() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getKeyPhraseId() {
        return this.keyPhraseId;
    }

    public void setKeyPhraseId(long keyPhraseId) {
        this.keyPhraseId = keyPhraseId;
    }

}
