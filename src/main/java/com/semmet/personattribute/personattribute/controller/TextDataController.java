package com.semmet.personattribute.personattribute.controller;

import java.util.Map;

import com.semmet.personattribute.personattribute.service.TextAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TextDataController class receives post requests on the path /text-data,
 * then forwards it to the TextAnalysisService class to analyze it using
 * AWS Comprehend API and saves all the mappings between the user and the
 * detected entities and key phrases
 * @see TextAnalysisService
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@RestController
@RequestMapping("/text-data")
public class TextDataController {

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

        textAnalysisService.addUserTextData(textData, "en", userId);
    }
}
