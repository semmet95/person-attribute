package com.semmet.personattribute.personattribute.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * KeyPhrases class models a table that stores key phrases returned by AWS Comprehend
 * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-key-phrases.html">Comprehend Key Phrases</a>
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Entity
public class KeyPhrases {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
