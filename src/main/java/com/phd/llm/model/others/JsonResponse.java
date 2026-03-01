package com.phd.llm.model.others;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse {

	private String contains_ip;
    private String ip;
    private String llm;

    // ✅ Default constructor (required for Jackson)
    public JsonResponse() {}

    public JsonResponse(String contains_ip, String ip) {
        this.contains_ip = contains_ip;
        this.ip = ip;
    }

    // ✅ Getters and Setters (required for JSON serialization)
    public String getContains_ip() {
        return contains_ip;
    }

    public void setContains_ip(String contains_ip) {
        this.contains_ip = contains_ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "FileResult{" +
                "contains_ip='" + contains_ip + '\'' +
                ", ip='" + ip + '\'' +
                ", llm='" + llm + '\'' +
                '}';
    }
}
