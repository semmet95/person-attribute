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
    private long entityId;
    private float weight;
    private long frequency;
    private float sentimentNeutral;
    private float sentimentNegative;
    private float sentimentPositive;
    private float sentimentMixed;

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

    public float getSentimentNeutral() {
        return this.sentimentNeutral;
    }

    public void setSentimentNeutral(float sentimentNeutral) {
        this.sentimentNeutral = sentimentNeutral;
    }

    public float getSentimentNegative() {
        return this.sentimentNegative;
    }

    public void setSentimentNegative(float sentimentNegative) {
        this.sentimentNegative = sentimentNegative;
    }

    public float getSentimentPositive() {
        return this.sentimentPositive;
    }

    public void setSentimentPositive(float sentimentPositive) {
        this.sentimentPositive = sentimentPositive;
    }

    public float getSentimentMixed() {
        return this.sentimentMixed;
    }

    public void setSentimentMixed(float sentimentMixed) {
        this.sentimentMixed = sentimentMixed;
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

    public long getEntityId() {
        return this.entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
