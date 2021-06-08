package com.semmet.personattribute.personattribute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntityMappings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private long entitiesId;

    public UserEntityMappings() {
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

    public long getEntitiesId() {
        return this.entitiesId;
    }

    public void setEntitiesId(long entitiesId) {
        this.entitiesId = entitiesId;
    }

}
