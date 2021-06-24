package com.semmet.personattribute.personattribute.service;

import java.util.Map;

import com.semmet.personattribute.personattribute.exception.InvalidBodyExceptionHandler;
import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.KeyPhrases;
import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.repository.EntitiesRepository;
import com.semmet.personattribute.personattribute.repository.KeyPhrasesRepository;
import com.semmet.personattribute.personattribute.repository.UserRepository;
import com.semmet.personattribute.personattribute.util.AWSComprehendUtil;
import com.semmet.personattribute.personattribute.util.AppLogger;
import com.semmet.personattribute.personattribute.util.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TextAnalysisService class provides methods and properties that
 * let you sends requests to, and get reponse from, AWS Comprehend
 * service. It also creates objects and their mappings in terms of
 * user objects and entity/key phrase objects.
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@Service
public class TextAnalysisService {

    @Autowired
    private EntitiesRepository entitiesRepository;
    @Autowired
    private KeyPhrasesRepository keyPhrasesRepository;
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

    /**
     * This method sends text data to Comprehend and saves the returned mappings
     * of detected sentiments/key phrases/entities and their scores
     * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-entities.html">Comprehend Entities</a>
     * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-key-phrases.html">Comprehend Key Phrases</a>
     * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-sentiment.html">Comprehend Sentiments</a>
     * 
     * @param text the text data to be analyzed by Comprehend
     * @param langCode ISO language code (currently hardcoded in every function call)
     * @return mapping of the detected sentiments
     */

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

    /**
     * This method creates Entities objects from the entities mapping
     * returned by Comprehend
     * @see Entities
     * 
     * @return array of the Entities objects
     */

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

    /**
     * This method creates KeyPhrases objects from the key phrases mapping
     * returned by Comprehend
     * @see KeyPhrases
     * 
     * @return array of the KeyPhrases objects
     */

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

    /**
     * This method creates new user entity mapping objects between the
     * user provided as an argument and the entities deteted by Comprehend
     * @see UserEntityMappings
     * 
     * @param userId the user with which the analyzed text data was associated with
     * @return array of the UserEntityMappings objects
     */

    public UserEntityMappings[] getUserEntityMappingsObjects(long userId) {
        
        UserEntityMappings[] allUserEntityMappings = new UserEntityMappings[entityScoreMap.size()];
        var index = 0;

        for(var entity: entityScoreMap.keySet()) {

            var storedUser = userRepository.findByUserId(userId);
            var tempueMappings = new UserEntityMappings();

            if(storedUser.isEmpty()) {
                AppLogger.LOGGER.error(String.format("user with userId:%d not found", userId));
                throw new InvalidBodyExceptionHandler();
            }
            if(entitiesRepository.findByEntity(entity).isEmpty()) {
                AppLogger.LOGGER.warn(String.format("entity:%s not found in the database, skipping mapping", entity));
                continue;
            }

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

    /**
     * This method creates new user key phrase mapping objects between the
     * user provided as an argument and the key phrases deteted by Comprehend
     * @see UserKeyPhraseMappings
     * 
     * @param userId the user with which the analyzed text data was associated with
     * @return array of the UserKeyPhraseMappings objects
     */

    public UserKeyPhraseMappings[] getUserKeyPhraseMappingsObjects(long userId) {
        
        UserKeyPhraseMappings[] allUserKeyPhraseMappings = new UserKeyPhraseMappings[keyPhraseConfidenceMap.size()];
        var index = 0;

        for(var keyPhrase: keyPhraseConfidenceMap.keySet()) {

            var storedUser = userRepository.findByUserId(userId);
            var tempukpMappings = new UserKeyPhraseMappings();

            if(storedUser.isEmpty()) {
                AppLogger.LOGGER.error(String.format("user with userId:%d not found", userId));
                throw new InvalidBodyExceptionHandler();
            }
            if(keyPhrasesRepository.findByKeyPhrase(keyPhrase).isEmpty()) {
                AppLogger.LOGGER.warn(String.format("keyphrase:%s not found in the database, skipping mapping", keyPhrase));
                continue;
            }
            
            tempukpMappings.setFrequency(1);
            tempukpMappings.setKeyPhrase(keyPhrasesRepository.findByKeyPhrase(keyPhrase).get(0));
            tempukpMappings.setUser(storedUser.get(0));
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
