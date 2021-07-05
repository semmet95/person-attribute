package com.semmet.personattribute.personattribute.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entities class models a table that stores entities returned by AWS Comprehend
 * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-entities.html">Comprehend Entities</a>
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Entity
public class Entities {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false, unique = true)
    private String entity;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
