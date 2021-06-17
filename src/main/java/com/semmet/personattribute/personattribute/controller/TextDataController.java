package com.semmet.personattribute.personattribute.controller;

import java.util.HashMap;
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
import com.semmet.personattribute.personattribute.util.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(produces = "application/json", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public void addTextData(@RequestParam Map<String, String> body) {
        var textData = body.get("textData").toLowerCase();
        var userId = Long.parseLong(body.get("userId"));

        // use Comprehend to extract all the data first
        Map<String, Float> sentimentMap = textAnalysisService.analyzeText(textData, "en");
        Entities[] allEntities = textAnalysisService.getEntitiesObjects();
        KeyPhrases[] allKeyPhrases = textAnalysisService.getKeyPhrasesObjects();

        // update the DB with new entities and key phrases
        //Map<String, Long> entityIdMapping = new HashMap<>();
        //Map<String, Long> keyPhraseIdMapping = new HashMap<>();

        for(var entity: allEntities) {
            List<Entities> existingEntity = entitiesRepository.findByEntity(entity.getEntity());

            if(existingEntity.isEmpty()) {

                var savedEntity = entitiesRepository.save(entity);
                //entityIdMapping.put(entity.getEntity(), savedEntity.getId());
            } else {
                //entityIdMapping.put(entity.getEntity(), existingEntity.get(0).getId());
            }
        }

        for(var keyPhrase: allKeyPhrases) {
            List<KeyPhrases> existingKeyPhrases = keyPhrasesRepository.findByKeyPhrase(keyPhrase.getKeyPhrase());

            if(existingKeyPhrases.isEmpty()) {

                var savedKeyPhrase = keyPhrasesRepository.save(keyPhrase);
                //keyPhraseIdMapping.put(keyPhrase.getKeyPhrase(), savedKeyPhrase.getId());
            } else {
                //keyPhraseIdMapping.put(keyPhrase.getKeyPhrase(), existingKeyPhrases.get(0).getId());
            }
        }

        // update the DB with new mappings
        //UserEntityMappings[] allUserEntityMappings = textAnalysisService.getUserEntityMappingsObjects(userId, entityIdMapping);
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
