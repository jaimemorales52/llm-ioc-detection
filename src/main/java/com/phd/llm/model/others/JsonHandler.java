package com.phd.llm.model.others;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JsonHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ✅ Parse JSON String into Map<String, FileResult>
    public static Map<String, JsonResponse> parseJsonResponse(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, JsonResponse>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Convert Map<String, FileResult> back to JSON String
    public static String generateJsonResponse(Map<String, JsonResponse> fileResults) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileResults);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
