package com.semmet.personattribute.personattribute.controller;

import java.util.List;
import java.util.Map;

import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.KeyPhrases;
import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.repository.EntitiesRepository;
import com.semmet.personattribute.personattribute.repository.KeyPhrasesRepository;
import com.semmet.personattribute.personattribute.repository.UserEntityMappingsRepository;
import com.semmet.personattribute.personattribute.repository.UserKeyPhrasesMappingsRepository;
import com.semmet.personattribute.personattribute.service.TextAnalysisService;
import com.semmet.personattribute.personattribute.util.AWSComprehendUtil;
import com.semmet.personattribute.personattribute.util.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TextDataController class receives post requests on the path /text-data,
 * extracts insights on that text data using AWS Comprehend SDK,
 * then store the key phrases, entities, and their mappings with the
 * provided user
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

@RestController
@RequestMapping("/text-data")
public class TextDataController {
    
    @Autowired
    private EntitiesRepository entitiesRepository;
    @Autowired
    private KeyPhrasesRepository keyPhrasesRepository;
    @Autowired
    private UserEntityMappingsRepository userEntityMappingsRepository;
    @Autowired
    private UserKeyPhrasesMappingsRepository userKeyPhrasesMappingsRepository;
    @Autowired
    private TextAnalysisService textAnalysisService;

    /**
     * This method is used to receive the posted body on the path /text-data,
     * then forward it to TextAnalysisService to extract insights and get
     * all the entities, key phrases, and mappings objects; and then store them all
     * in the database.
     * @see TextAnalysisService
     * 
     * @param body This is the body sent with the post request.
     * It should be of type {@code application/x-www-form-urlencoded;charset=UTF-8}  
     * @return void
     */
    @PostMapping(produces = "application/json", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public void addTextData(@RequestParam Map<String, String> body) {
        var textData = body.get("textData").toLowerCase().replace("\n", " ").trim();
        var userId = Long.parseLong(body.get("userId"));

        // keeping the string only alphanumeric and whitespaces
        textData = textData.replaceAll("[^a-zA-Z0-9\\s]", "");

        // use Comprehend to extract all the data first
        Map<String, Float> sentimentMap = textAnalysisService.analyzeText(textData, "en");
        Entities[] allEntities = textAnalysisService.getEntitiesObjects();
        KeyPhrases[] allKeyPhrases = textAnalysisService.getKeyPhrasesObjects();

        // update the DB with new entities and key phrases
        for(var entity: allEntities) {
            if(entitiesRepository.findByEntity(entity.getEntity()).isEmpty()) {
                entitiesRepository.save(entity);
            }
        }

        for(var keyPhrase: allKeyPhrases) {
            if(keyPhrasesRepository.findByKeyPhrase(keyPhrase.getKeyPhrase()).isEmpty()) {
                keyPhrasesRepository.save(keyPhrase);
            }
        }

        // update the DB with new mappings
        UserEntityMappings[] allUserEntityMappings = textAnalysisService.getUserEntityMappingsObjects(userId);

        for(var ueMapping: allUserEntityMappings) {
            List<UserEntityMappings> existingueMappings = userEntityMappingsRepository.findByUser_UserIdAndEntity_Id(userId, ueMapping.getEntity().getId());

            if(existingueMappings.isEmpty()) {
                // handle the case with new ueMappings
                userEntityMappingsRepository.save(ueMapping);
            } else {
                //update the existing ueMapping
                var tempueMappings = existingueMappings.get(0);
                tempueMappings.setFrequency(tempueMappings.getFrequency() + 1);
                tempueMappings.setSentimentMixed(AppUtils.tanH(tempueMappings.getSentimentMixed(), sentimentMap.get("mixed")));
                tempueMappings.setSentimentNegative(AppUtils.tanH(tempueMappings.getSentimentNegative(), sentimentMap.get("negative")));
                tempueMappings.setSentimentNeutral(AppUtils.tanH(tempueMappings.getSentimentNeutral(), sentimentMap.get("neutral")));
                tempueMappings.setSentimentPositive(AppUtils.tanH(tempueMappings.getSentimentPositive(), sentimentMap.get("positive")));
                tempueMappings.setWeight(AppUtils.tanH(tempueMappings.getWeight(), ueMapping.getWeight()));

                userEntityMappingsRepository.save(tempueMappings);
            }
        }

        UserKeyPhraseMappings[] allUserKeyPhraseMappings = textAnalysisService.getUserKeyPhraseMappingsObjects(userId);

        for(var ukpMapping: allUserKeyPhraseMappings) {
            List<UserKeyPhraseMappings> existingukpMappings = userKeyPhrasesMappingsRepository.findByUser_UserIdAndKeyPhrase_Id(userId, ukpMapping.getKeyPhrase().getId());

            if(existingukpMappings.isEmpty()) {
                // handle the case with new ukpMappings
                userKeyPhrasesMappingsRepository.save(ukpMapping);
            } else {
                //update the existing ukpMapping
                var tempukpMappings = existingukpMappings.get(0);
                tempukpMappings.setFrequency(tempukpMappings.getFrequency() + 1);
                tempukpMappings.setSentimentMixed(AppUtils.tanH(tempukpMappings.getSentimentMixed(), sentimentMap.get("mixed")));
                tempukpMappings.setSentimentNegative(AppUtils.tanH(tempukpMappings.getSentimentNegative(), sentimentMap.get("negative")));
                tempukpMappings.setSentimentNeutral(AppUtils.tanH(tempukpMappings.getSentimentNeutral(), sentimentMap.get("neutral")));
                tempukpMappings.setSentimentPositive(AppUtils.tanH(tempukpMappings.getSentimentPositive(), sentimentMap.get("positive")));
                tempukpMappings.setWeight(AppUtils.tanH(tempukpMappings.getWeight(), ukpMapping.getWeight()));

                userKeyPhrasesMappingsRepository.save(tempukpMappings);
            }
        }
    }
}
