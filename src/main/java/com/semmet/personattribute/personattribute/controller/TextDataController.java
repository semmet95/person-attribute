package com.semmet.personattribute.personattribute.controller;

import java.util.HashMap;
import java.util.Map;

import com.semmet.personattribute.personattribute.model.Entities;
import com.semmet.personattribute.personattribute.model.KeyPhrases;
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
        var textData = body.get("text-data").toLowerCase();
        var userId = body.get("userId").toLowerCase();

        // use Comprehend to extract all the data first
        textAnalysisService.analyzeText(userId, textData, "en");
        Entities[] allEntities = textAnalysisService.getEntitiesObjects();
        KeyPhrases[] allKeyPhrases = textAnalysisService.getKeyPhrasesObjects();

        // update the DB
        Map<String, Long> entityIdMapping = new HashMap<>();
        Map<String, Long> keyPhraseIdMapping = new HashMap<>();

        for(var entity: allEntities) {
            var savedEntity = entitiesRepository.save(entity);
            entityIdMapping.put(entity.getEntity(), savedEntity.getId());
        }

        for(var keyPhrase: allKeyPhrases) {
            var savedKeyPhrase = keyPhrasesRepository.save(keyPhrase);
            keyPhraseIdMapping.put(keyPhrase.getKeyPhrase(), savedKeyPhrase.getId());
        }

    }
}
