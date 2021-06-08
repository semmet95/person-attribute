package com.semmet.personattribute.personattribute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Entities {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long entityId;
    private float weight;
    private long frequency;
    private String entity;
    private float sentimentNeutral;
    private float sentimentNegative;
    private float sentimentPositive;
    private float sentimentMixed;

    public Entities() {
    }

    public float getSentimentMixed() {
        return sentimentMixed;
    }

    public void setSentimentMixed(float sentimentMixed) {
        this.sentimentMixed = sentimentMixed;
    }

    public float getSentimentPositive() {
        return sentimentPositive;
    }

    public void setSentimentPositive(float sentimentPositive) {
        this.sentimentPositive = sentimentPositive;
    }

    public float getSentimentNegative() {
        return sentimentNegative;
    }

    public void setSentimentNegative(float sentimentNegative) {
        this.sentimentNegative = sentimentNegative;
    }

    public float getSentimentNeutral() {
        return sentimentNeutral;
    }

    public void setSentimentNeutral(float sentimentNeutral) {
        this.sentimentNeutral = sentimentNeutral;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEntityId() {
        return this.entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getFrequency() {
        return this.frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

}
