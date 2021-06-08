package com.semmet.personattribute.personattribute.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DetectEntitiesRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.Entity;

public class AWSComprehendUtil {
    
    private static final Region REGION;
    private static final ComprehendClient COM_CLIENT;
    
    static {
        REGION = Region.US_EAST_2;
        COM_CLIENT = ComprehendClient.builder().region(REGION).build();
    }

    private AWSComprehendUtil() {
    }

    public static Map<String, Float> detectSentiment(String text, String langCode) {
        try {
            AppLogger.LOGGER.info("Calling Comprehend DetectSentiment API");

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

    public static void detectAllEntities(String text, String langCode) {

        try {
            var detectEntitiesRequest = DetectEntitiesRequest.builder()
                                                            .text(text)
                                                            .languageCode(langCode)
                                                            .build();

            var detectEntitiesResult = COM_CLIENT.detectEntities(detectEntitiesRequest);
            List<Entity> entList = detectEntitiesResult.entities();


            if(!entList.isEmpty()) {
                for(Entity entity: entList) {
                    AppLogger.LOGGER.info(String.format("Entity detected %s with score %f", entity.text(), entity.score()));
                }
            }
            else {
                AppLogger.LOGGER.info("no entities detected");
            }

        } catch (ComprehendException e) {
            AppLogger.LOGGER.error(e.awsErrorDetails().errorMessage());
            //return null;
       }
    }
}
