package com.hospital.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Icd10Service {

    private final RestTemplate restTemplate;

    public Icd10Service(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the first ICD-10 code for a given diagnosis.
     * @param diagnosis The diagnosis name
     * @return The first ICD-10 code found, or "ICD Code Not Found".
     */
    public String getIcd10Code(String diagnosis) {
        // ✅ Use "sf=code,name" for better results
        String apiUrl = "https://clinicaltables.nlm.nih.gov/api/icd10cm/v3/search?sf=code,name&terms=" + diagnosis;

        try {
            // ✅ Fetch raw JSON response
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            // ✅ Convert JSON to a List and extract the most relevant ICD-10 code
            return parseBestICD10Code(jsonResponse, diagnosis);
        } catch (Exception e) {
            System.err.println("Error fetching ICD-10 data: " + e.getMessage());
            return "ICD Code Not Found";
        }
    }


    /**
     * Fetches a list of ICD-10 codes for a given disease.
     * @param query The disease name
     * @return A list of ICD-10 codes and descriptions.
     */
    public List<Map<String, String>> searchICD10(String query) {
        String apiUrl = "https://clinicaltables.nlm.nih.gov/api/icd10cm/v3/search?sf=code,name&terms=" + query;

        try {
            // ✅ Fetch raw JSON response as a String
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            // ✅ Convert JSON response into List format
            return parseICD10Response(jsonResponse);
        } catch (Exception e) {
            System.err.println("Error fetching ICD-10 data: " + e.getMessage());
            return List.of();  // ✅ Return an empty list if the API call fails
        }
    }

    /**
     * Parses the API response and extracts the first ICD-10 code.
     * @param jsonResponse Raw JSON response from API
     * @return The first ICD-10 code or "ICD Code Not Found"
     */
    private String parseBestICD10Code(String jsonResponse, String originalDiagnosis) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> responseList = objectMapper.readValue(jsonResponse, List.class);

            if (responseList.size() >= 4) {
                List<List<String>> results = (List<List<String>>) responseList.get(3);

                // ✅ Prioritize exact or relevant matches
                for (List<String> result : results) {
                    if (result.size() >= 2) {
                        String code = result.get(0);
                        String description = result.get(1);

                        // ✅ If the description contains the exact term, return it first
                        if (description.toLowerCase().contains(originalDiagnosis.toLowerCase())) {
                            return code;
                        }
                    }
                }

                // ✅ If no exact match is found, return the first available code
                if (!results.isEmpty()) {
                    return results.get(0).get(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing ICD-10 response: " + e.getMessage());
        }
        return "ICD Code Not Found";
    }

    /**
     * Parses the API response and extracts a list of ICD-10 codes.
     * @param jsonResponse Raw JSON response from API
     * @return A list of ICD-10 codes with descriptions.
     */
    private List<Map<String, String>> parseICD10Response(String jsonResponse) {
        List<Map<String, String>> icd10Codes = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> responseList = objectMapper.readValue(jsonResponse, List.class);

            if (responseList.size() >= 4) {
                List<List<String>> results = (List<List<String>>) responseList.get(3);

                for (List<String> result : results) {
                    if (result.size() >= 2) {
                        icd10Codes.add(Map.of("code", result.get(0), "description", result.get(1)));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing ICD-10 response: " + e.getMessage());
        }

        return icd10Codes;
    }

}
