package com.semmet.personattribute.personattribute.service;

import java.util.List;
import java.util.Map;

import com.semmet.personattribute.personattribute.exception.InvalidBodyExceptionHandler;
import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.KeyPhrases;
import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.repository.EntitiesRepository;
import com.semmet.personattribute.personattribute.repository.KeyPhrasesRepository;
import com.semmet.personattribute.personattribute.repository.UserEntityMappingsRepository;
import com.semmet.personattribute.personattribute.repository.UserKeyPhrasesMappingsRepository;
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
 * It extracts insights on text data then store the key phrases,
 * entities, and their mappings with the provided user
 * To check how the mappings are stored:
 * @see UserEntityMappings
 * @see UserKeyPhraseMappings
 * AWS Comprehend requests are sent using the utility class:
 * @see AWSComprehendUtil
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
    @Autowired
    private UserEntityMappingsRepository userEntityMappingsRepository;
    @Autowired
    private UserKeyPhrasesMappingsRepository userKeyPhrasesMappingsRepository;

    /**
     * This method saves a new entity and returns its id or returns the id
     * of the entity if its already saved
     * @see Entities
     * 
     * @param entity entity to save/return id of
     * @return id of the entity
     */
    private long saveDetectedEntity(String entity) {
        List<Entities> existingEntity = entitiesRepository.findByEntity(entity);

        if(!existingEntity.isEmpty()) {
            return existingEntity.get(0).getId();
        }

        var tempEntity = new Entities();
        tempEntity.setEntity(entity);
        tempEntity = entitiesRepository.save(tempEntity);

        return tempEntity.getId();
    }

    /**
     * This method saves a new key phrase and returns its id or returns the id
     * of the key phrase if its already saved
     * @see KeyPhrases
     * 
     * @param keyPhrase key phrase to save/return id of
     * @return id of the key phrase
     */
    private long saveDetectedKeyPhrase(String keyPhrase) {
        List<KeyPhrases> existingEntity = keyPhrasesRepository.findByKeyPhrase(keyPhrase);

        if(!existingEntity.isEmpty()) {
            return existingEntity.get(0).getId();
        }

        var tempEntity = new Entities();
        tempEntity.setEntity(keyPhrase);
        tempEntity = entitiesRepository.save(tempEntity);

        return tempEntity.getId();
    }

    /**
     * This method creates a new mapping between a key phrase and a user
     * @see UserKeyPhraseMappings
     * 
     * @param userId id of the user the key phrase is associated with
     * @param keyPhraseId id of the key phrase that is to be associated with the user
     * @param confidence key phrase score of the detected key phrase
     * @param sentimentMap map of different detected sentiments and their corresponding scores
     */
    private void saveUserKeyPhraseMapping(long userId, long keyPhraseId, float confidence, Map<String, Float> sentimentMap) {
        var storedUser = userRepository.findByUserId(userId);

        if(storedUser.isEmpty()) {
            AppLogger.LOGGER.error(String.format("user with userId:%d not found", userId));
            throw new InvalidBodyExceptionHandler();
        }
        if(keyPhrasesRepository.findById(keyPhraseId).isEmpty()) {
            AppLogger.LOGGER.warn(String.format("key phrase id:%d not found in the database, skipping mapping", keyPhraseId));
        }
        else {
            List<UserKeyPhraseMappings> existingukpMappings = userKeyPhrasesMappingsRepository.findByUser_UserIdAndKeyPhrase_Id(userId, keyPhraseId);
            if(existingukpMappings.isEmpty()) {

                var tempukpMappings = new UserKeyPhraseMappings();
                tempukpMappings.setKeyPhrase(keyPhrasesRepository.findById(keyPhraseId).get());
                tempukpMappings.setUser(storedUser.get(0));
                tempukpMappings.setFrequency(1);
                tempukpMappings.setSentimentMixed(AppUtils.tanH(0, 2 * sentimentMap.get("mixed")));
                tempukpMappings.setSentimentNegative(AppUtils.tanH(0, 2 * sentimentMap.get("negative")));
                tempukpMappings.setSentimentNeutral(AppUtils.tanH(0, 2 * sentimentMap.get("neutral")));
                tempukpMappings.setSentimentPositive(AppUtils.tanH(0, 2 * sentimentMap.get("positive")));
                tempukpMappings.setWeight(AppUtils.tanH(0, 2 * confidence));
            } else {
                //update the existing ueMapping
                var tempukpMappings = existingukpMappings.get(0);
                tempukpMappings.setFrequency(tempukpMappings.getFrequency() + 1);
                tempukpMappings.setSentimentMixed(AppUtils.tanH(tempukpMappings.getSentimentMixed(), sentimentMap.get("mixed")));
                tempukpMappings.setSentimentNegative(AppUtils.tanH(tempukpMappings.getSentimentNegative(), sentimentMap.get("negative")));
                tempukpMappings.setSentimentNeutral(AppUtils.tanH(tempukpMappings.getSentimentNeutral(), sentimentMap.get("neutral")));
                tempukpMappings.setSentimentPositive(AppUtils.tanH(tempukpMappings.getSentimentPositive(), sentimentMap.get("positive")));
                tempukpMappings.setWeight(AppUtils.tanH(tempukpMappings.getWeight(), confidence));

                userKeyPhrasesMappingsRepository.save(tempukpMappings);
            }
        }
    }

    /**
     * This method creates a new mapping between an entity and a user
     * @see UserEntityMappings
     * 
     * @param userId id of the user the entity is associated with
     * @param entityId id of the entity that is to be associated with the user
     * @param weight score weight of the detected entity
     * @param sentimentMap map of different detected sentiments and their corresponding scores
     */
    private void saveUserEntityMapping(long userId, long entityId, float weight, Map<String, Float> sentimentMap) {
        var storedUser = userRepository.findByUserId(userId);

        if(storedUser.isEmpty()) {
            AppLogger.LOGGER.error(String.format("user with userId:%d not found", userId));
            throw new InvalidBodyExceptionHandler();
        }
        if(entitiesRepository.findById(entityId).isEmpty()) {
            AppLogger.LOGGER.warn(String.format("entityId:%d not found in the database, skipping mapping", entityId));
        }
        else {
            List<UserEntityMappings> existingueMappings = userEntityMappingsRepository.findByUser_UserIdAndEntity_Id(userId, entityId);
            if(existingueMappings.isEmpty()) {

                var tempueMappings = new UserEntityMappings();
                tempueMappings.setEntity(entitiesRepository.findById(entityId).get());
                tempueMappings.setUser(storedUser.get(0));
                tempueMappings.setFrequency(1);
                tempueMappings.setSentimentMixed(AppUtils.tanH(0, 2 * sentimentMap.get("mixed")));
                tempueMappings.setSentimentNegative(AppUtils.tanH(0, 2 * sentimentMap.get("negative")));
                tempueMappings.setSentimentNeutral(AppUtils.tanH(0, 2 * sentimentMap.get("neutral")));
                tempueMappings.setSentimentPositive(AppUtils.tanH(0, 2 * sentimentMap.get("positive")));
                tempueMappings.setWeight(AppUtils.tanH(0, 2 * weight));
            } else {
                //update the existing ueMapping
                var tempueMappings = existingueMappings.get(0);
                tempueMappings.setFrequency(tempueMappings.getFrequency() + 1);
                tempueMappings.setSentimentMixed(AppUtils.tanH(tempueMappings.getSentimentMixed(), sentimentMap.get("mixed")));
                tempueMappings.setSentimentNegative(AppUtils.tanH(tempueMappings.getSentimentNegative(), sentimentMap.get("negative")));
                tempueMappings.setSentimentNeutral(AppUtils.tanH(tempueMappings.getSentimentNeutral(), sentimentMap.get("neutral")));
                tempueMappings.setSentimentPositive(AppUtils.tanH(tempueMappings.getSentimentPositive(), sentimentMap.get("positive")));
                tempueMappings.setWeight(AppUtils.tanH(tempueMappings.getWeight(), weight));

                userEntityMappingsRepository.save(tempueMappings);
            }
        }
    }

    public void addUserTextData(String text, String langCode, long userId) {

        Map<String, Float> sentimentMap = AWSComprehendUtil.detectSentiment(text, langCode);
        Map<String, Float> entityScoreMap = AWSComprehendUtil.detectAllEntities(text, langCode);
        Map<String, Float> keyPhraseConfidenceMap = AWSComprehendUtil.detectAllKeyPhrases(text, langCode);

        entityScoreMap.entrySet().forEach(entry->{
            var entity = entry.getKey();
            var score = entry.getValue();

            var entityId = saveDetectedEntity(entity);
            saveUserEntityMapping(userId, entityId, score, sentimentMap);
        });

        keyPhraseConfidenceMap.entrySet().forEach(entry->{
            var keyPhrase = entry.getKey();
            var confidence = entry.getValue();

            var keyPhraseId = saveDetectedKeyPhrase(keyPhrase);
            saveUserKeyPhraseMapping(userId, keyPhraseId, confidence, sentimentMap);
        });
    }
}
