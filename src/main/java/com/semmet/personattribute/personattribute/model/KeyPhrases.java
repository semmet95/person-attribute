package com.semmet.personattribute.personattribute.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class KeyPhrases {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true)
    private String keyPhrase;
    

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyPhrase() {
        return this.keyPhrase;
    }

    public void setKeyPhrase(String keyPhrase) {
        this.keyPhrase = keyPhrase;
    }
}
