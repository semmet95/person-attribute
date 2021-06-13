package com.semmet.personattribute.personattribute.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.KeyPhrases;
import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.repository.EntitiesRepository;
import com.semmet.personattribute.personattribute.repository.KeyPhrasesRepository;
import com.semmet.personattribute.personattribute.repository.UserEntityMappingsRepository;
import com.semmet.personattribute.personattribute.repository.UserKeyPhrasesMappingsRepository;
import com.semmet.personattribute.personattribute.service.TextAnalysisService;

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
        textAnalysisService.analyzeText(textData, "en");
        Entities[] allEntities = textAnalysisService.getEntitiesObjects();
        KeyPhrases[] allKeyPhrases = textAnalysisService.getKeyPhrasesObjects();

        // update the DB with new entities and key phrases
        Map<String, Long> entityIdMapping = new HashMap<>();
        Map<String, Long> keyPhraseIdMapping = new HashMap<>();

        for(var entity: allEntities) {
            List<Entities> existingEntity = entitiesRepository.findByEntity(entity.getEntity());

            if(existingEntity.isEmpty()) {

                var savedEntity = entitiesRepository.save(entity);
                entityIdMapping.put(entity.getEntity(), savedEntity.getId());
            } else {
                entityIdMapping.put(entity.getEntity(), existingEntity.get(0).getId());
            }
        }

        for(var keyPhrase: allKeyPhrases) {
            List<KeyPhrases> existingKeyPhrases = keyPhrasesRepository.findByKeyPhrase(keyPhrase.getKeyPhrase());

            if(existingKeyPhrases.isEmpty()) {

                var savedKeyPhrase = keyPhrasesRepository.save(keyPhrase);
                keyPhraseIdMapping.put(keyPhrase.getKeyPhrase(), savedKeyPhrase.getId());
            } else {
                keyPhraseIdMapping.put(keyPhrase.getKeyPhrase(), existingKeyPhrases.get(0).getId());
            }
        }

        // update the DB with new mappings
        UserEntityMappings[] allUserEntityMappings = textAnalysisService.getUserEntityMappingsObjects(userId, entityIdMapping);

        for(var ueMapping: allUserEntityMappings) {
            List<UserEntityMappings> existingueMappings = userEntityMappingsRepository.findByUserIdAndEntityId(userId, ueMapping.getEntityId());

            if(existingueMappings.isEmpty()) {
                // handle the case with new UEmappings
            } else {
                //update the existing ueMapping
            }
        }
    }
}
