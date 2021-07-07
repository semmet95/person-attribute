package com.semmet.personattribute.personattribute.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DetectEntitiesRequest;
import software.amazon.awssdk.services.comprehend.model.DetectKeyPhrasesRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.Entity;
import software.amazon.awssdk.services.comprehend.model.KeyPhrase;

/**
 * AWSComprehendUtil class provides methods that directly use AWS SDK to
 * communicate with Comprehend service.
 * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/functionality.html">Comprehend text analysis</a>
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

public class AWSComprehendUtil {
    
    private static final Region REGION;
    private static final ComprehendClient COM_CLIENT;
    
    static {
        REGION = Region.US_EAST_2;
        COM_CLIENT = ComprehendClient.builder().region(REGION).build();
    }

    private AWSComprehendUtil() {
    }

    /**
     * This method uses the Comprehend service to get the detected sentiments
     * and process the returned response to create a sentiment-score mapping.
     * @see <a href="https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/comprehend/model/DetectSentimentResponse.html">DetectSentimentResponse</a>
     * 
     * @param text the text data to be analyzed by Comprehend
     * @param langCode ISO language code (currently hardcoded in every function call)
     * @return mapping of the detected sentiments
     */

    public static Map<String, Float> detectSentiment(String text, String langCode) {
        try {
            var detectSentimentRequest = DetectSentimentRequest.builder()
                                                            .text(text)
                                                            .languageCode(langCode)
                                                            .build();

            var detectSentimentResponse = COM_CLIENT.detectSentiment(detectSentimentRequest);

            AppLogger.LOGGER.info(String.format("neutral score: %s", detectSentimentResponse.sentimentScore().neutral()));
            AppLogger.LOGGER.info(String.format("negative score: %s", detectSentimentResponse.sentimentScore().negative()));
            AppLogger.LOGGER.info(String.format("positive score: %s", detectSentimentResponse.sentimentScore().positive()));
            AppLogger.LOGGER.info(String.format("mixed score: %s", detectSentimentResponse.sentimentScore().mixed()));

            Map<String, Float> sentimentMap = new HashMap<>();
            sentimentMap.put("neutral", detectSentimentResponse.sentimentScore().neutral());
            sentimentMap.put("negative", detectSentimentResponse.sentimentScore().negative());
            sentimentMap.put("positive", detectSentimentResponse.sentimentScore().positive());
            sentimentMap.put("mixed", detectSentimentResponse.sentimentScore().mixed());

            return sentimentMap;

        } catch (ComprehendException e) {
            AppLogger.LOGGER.error(e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    /**
     * This method uses the Comprehend service to get the detected entities
     * and process the returned response to create an entity-score mapping.
     * @see <a href="https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/comprehend/model/DetectEntitiesResponse.html">DetectEntitiesResponse</a>
     * 
     * @param text the text data to be analyzed by Comprehend
     * @param langCode ISO language code (currently hardcoded in every function call)
     * @return mapping of the detected entities
     */

    public static Map<String, Float> detectAllEntities(String text, String langCode) {

        try {
            var detectEntitiesRequest = DetectEntitiesRequest.builder()
                                                            .text(text)
                                                            .languageCode(langCode)
                                                            .build();

            var detectEntitiesResult = COM_CLIENT.detectEntities(detectEntitiesRequest);
            List<Entity> entList = detectEntitiesResult.entities();

            Map<String, Float> entityScoreMap = new HashMap<>();

            if(!entList.isEmpty()) {

                for(Entity entity: entList) {
                    AppLogger.LOGGER.info(String.format("Entity detected %s with score %f", entity.text(), entity.score()));
                    entityScoreMap.put(entity.text(), entity.score());
                }
            }
            else {
                AppLogger.LOGGER.info("no entities detected");
            }

            return entityScoreMap;

        } catch (ComprehendException e) {
            AppLogger.LOGGER.error(e.awsErrorDetails().errorMessage());
            return null;
       }
    }

    /**
     * This method uses the Comprehend service to get the detected key phrases
     * and process the returned response to create an key phrase-confidence mapping.
     * @see <a href="https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/comprehend/model/DetectKeyPhrasesResponse.html">DetectKeyPhrasesResponse</a>
     * 
     * @param text the text data to be analyzed by Comprehend
     * @param langCode ISO language code (currently hardcoded in every function call)
     * @return mapping of the detected key phrases
     */

    public static Map<String, Float> detectAllKeyPhrases(String text, String langCode) {

        try {
            var detectKeyPhrasesRequest = DetectKeyPhrasesRequest.builder()
                                                                    .text(text)
                                                                    .languageCode(langCode)
                                                                    .build();

            var detectKeyPhrasesResult = COM_CLIENT.detectKeyPhrases(detectKeyPhrasesRequest);

            List<KeyPhrase> phraseList = detectKeyPhrasesResult.keyPhrases();

            Map<String, Float> keyPhraseConfidenceMap = new HashMap<>();

            if(!phraseList.isEmpty()) {

                for(var keyPhrase: phraseList) {
                    AppLogger.LOGGER.info(String.format("detected key phrase:%s with confidence:%f", keyPhrase.text(), keyPhrase.score()));
                    keyPhraseConfidenceMap.put(keyPhrase.text(), keyPhrase.score());
                }

            } else {
                AppLogger.LOGGER.info("no key phrases detected");
            }

            return keyPhraseConfidenceMap;

        } catch (ComprehendException e) {
            AppLogger.LOGGER.error(e.awsErrorDetails().errorMessage());
            return null;
        }
    }
}
