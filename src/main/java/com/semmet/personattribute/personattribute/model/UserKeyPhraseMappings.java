package com.semmet.personattribute.personattribute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserKeyPhraseMappings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name =  "user_id", referencedColumnName = "userId")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "key_phrase_id", referencedColumnName = "id")
    private KeyPhrases keyPhrase;
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

    public Users getUser() {
        return this.user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public KeyPhrases getKeyPhrase() {
        return this.keyPhrase;
    }

    public void setKeyPhrase(KeyPhrases keyPhrase) {
        this.keyPhrase = keyPhrase;
    }


}
