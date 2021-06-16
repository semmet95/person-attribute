package com.semmet.personattribute.personattribute.service;

import java.util.Map;

import com.semmet.personattribute.personattribute.exception.InvalidBodyExceptionHandler;
import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.KeyPhrases;
import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.repository.EntitiesRepository;
import com.semmet.personattribute.personattribute.repository.UserRepository;
import com.semmet.personattribute.personattribute.util.AWSComprehendUtil;
import com.semmet.personattribute.personattribute.util.AppLogger;
import com.semmet.personattribute.personattribute.util.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextAnalysisService {

    @Autowired
    private EntitiesRepository entitiesRepository;
    @Autowired
    private UserRepository userRepository;

    private Map<String, Float> sentimentMap;
    private Map<String, Float> entityScoreMap;
    private Map<String, Float> keyPhraseConfidenceMap;

    public Map<String,Float> getSentimentMap() {
        return this.sentimentMap;
    }

    public void setSentimentMap(Map<String,Float> sentimentMap) {
        this.sentimentMap = sentimentMap;
    }

    public Map<String,Float> getEntityScoreMap() {
        return this.entityScoreMap;
    }

    public void setEntityScoreMap(Map<String,Float> entityScoreMap) {
        this.entityScoreMap = entityScoreMap;
    }

    public Map<String,Float> getKeyPhraseConfidenceMap() {
        return this.keyPhraseConfidenceMap;
    }

    public void setKeyPhraseConfidenceMap(Map<String,Float> keyPhraseConfidenceMap) {
        this.keyPhraseConfidenceMap = keyPhraseConfidenceMap;
    }


    public Map<String, Float> analyzeText(String text, String langCode) {
        
        this.setSentimentMap(AWSComprehendUtil.detectSentiment(text, langCode));
        this.setEntityScoreMap(AWSComprehendUtil.detectAllEntities(text, langCode));
        this.setKeyPhraseConfidenceMap(AWSComprehendUtil.detectAllKeyPhrases(text, langCode));

        if(null == this.sentimentMap) {
            AppLogger.LOGGER.error(String.format("received null sentiments for the text::%s", text));
        } else if(null == this.entityScoreMap) {
            AppLogger.LOGGER.error(String.format("received null entities for the text::%s", text));
        } else if(null == this.keyPhraseConfidenceMap) {
            AppLogger.LOGGER.error(String.format("received null key phrases for the text::%s", text));
        }

        return this.sentimentMap;
    }

    public Entities[] getEntitiesObjects() {
        
        Entities[] allEntities = new Entities[this.entityScoreMap.size()];
        var index = 0;

        for(var entities: this.entityScoreMap.keySet()) {

            var tempEntities = new Entities();
            tempEntities.setEntity(entities);

            allEntities[index++] = tempEntities;
        }

        return allEntities;
    }

    public KeyPhrases[] getKeyPhrasesObjects() {
        
        KeyPhrases[] allKeyPhrases = new KeyPhrases[this.keyPhraseConfidenceMap.size()];
        var index = 0;

        for(var keyPhrases: this.keyPhraseConfidenceMap.keySet()) {

            var tempKeyPhrases = new KeyPhrases();
            tempKeyPhrases.setKeyPhrase(keyPhrases);

            allKeyPhrases[index++] = tempKeyPhrases;
        }

        return allKeyPhrases;
    }

    public UserEntityMappings[] getUserEntityMappingsObjects(long userId) {
        
        UserEntityMappings[] allUserEntityMappings = new UserEntityMappings[entityScoreMap.size()];
        var index = 0;

        for(var entity: entityScoreMap.keySet()) {

            var storedUser = userRepository.findByUserId(userId);
            if(storedUser.isEmpty()) {
                AppLogger.LOGGER.error(String.format("user with userId:%d not found", userId));
                throw new InvalidBodyExceptionHandler();
            }

            var tempueMappings = new UserEntityMappings();
            tempueMappings.setEntity(entitiesRepository.findByEntity(entity).get(0));
            tempueMappings.setUser(storedUser.get(0));
            tempueMappings.setFrequency(1);
            tempueMappings.setSentimentMixed(AppUtils.tanH(0, 2 * sentimentMap.get("mixed")));
            tempueMappings.setSentimentNegative(AppUtils.tanH(0, 2 * sentimentMap.get("negative")));
            tempueMappings.setSentimentNeutral(AppUtils.tanH(0, 2 * sentimentMap.get("neutral")));
            tempueMappings.setSentimentPositive(AppUtils.tanH(0, 2 * sentimentMap.get("positive")));
            tempueMappings.setWeight(AppUtils.tanH(0, 2 * entityScoreMap.get(entity)));

            allUserEntityMappings[index++] = tempueMappings;
        }

        return allUserEntityMappings;
    }

    public UserKeyPhraseMappings[] getUserKeyPhraseMappingsObjects(long userId, Map<String, Long> keyPhraseIdMapping) {
        
        UserKeyPhraseMappings[] allUserKeyPhraseMappings = new UserKeyPhraseMappings[keyPhraseConfidenceMap.size()];
        var index = 0;

        for(var keyPhrase: keyPhraseConfidenceMap.keySet()) {
            
            var tempukpMappings = new UserKeyPhraseMappings();
            tempukpMappings.setFrequency(1);
            tempukpMappings.setKeyPhraseId(keyPhraseIdMapping.get(keyPhrase));
            tempukpMappings.setUserId(userId);
            tempukpMappings.setWeight(AppUtils.tanH(0, 2 * keyPhraseConfidenceMap.get(keyPhrase)));
            tempukpMappings.setSentimentMixed(AppUtils.tanH(0, 2 * sentimentMap.get("mixed")));
            tempukpMappings.setSentimentNegative(AppUtils.tanH(0, 2 * sentimentMap.get("negative")));
            tempukpMappings.setSentimentNeutral(AppUtils.tanH(0, 2 * sentimentMap.get("neutral")));
            tempukpMappings.setSentimentPositive(AppUtils.tanH(0, 2 * sentimentMap.get("positive")));

            allUserKeyPhraseMappings[index++] = tempukpMappings;
        }

        return allUserKeyPhraseMappings;
    }
}
