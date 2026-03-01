package com.phd.llm.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.phd.llm.model.others.JsonResponse;

public interface FileService {

	public void addFlag(MultipartFile multiPartFile, String outputFilePath) throws IOException;

	public void anonymizeJavaScriptFile(String inputFilePath, String outputFilePath) throws IOException;

	public void anonymizeJavaScriptFile(MultipartFile multiPartFile, String outputFilePath) throws IOException;

	public void obfuscateAdvancedJavaScriptFile(String inputFilePath, String outputFilePath) throws IOException;

	public void obfuscateAdvancedJavaScriptFile(MultipartFile multiPartFile, String outputFilePath) throws IOException;

	public void addDecryptAES(MultipartFile multipartFile, String outFilePath) throws IOException;

	public void addDecryptAES(String inputFilePath, String outFilePath) throws IOException;

	public void exportAnalysisResults(Map<String, List<String>> analysisResults, String fileName) throws IOException;

	public void generateExtraObfuscatedJavaScript(String inputFilePath, String outputFilePath) throws IOException;

	public void encodeAndReplaceServerIP(String inputFilePath, String outputFilePath) throws IOException;

	public void completeProcessOfusLLMResult(MultipartFile file, String llm) throws IOException;

	public void fileModificationProcess(MultipartFile file) throws IOException;

	public void openAIServiceAllConsults(String fileName, List<String> llmResult) throws IOException;
	
	public Map<String, JsonResponse> openAIServiceAllConsultsToJson(String fileName, Map<String, JsonResponse> fileResults) throws IOException;

	public void cohereServiceAllConsults(String fileName, List<String> llmResult) throws IOException;

	public void anthropicServiceAllConsults(String fileName, List<String> llmResult) throws IOException;
	
	public Map<String, JsonResponse> anthropicServiceAllConsultsToJson(String fileName, Map<String, JsonResponse> fileResults1) throws IOException;

	public void geminiServiceAllConsults(String fileName, List<String> llmResult) throws IOException;
	
	public void openAIServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException;

	public void cohereServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException;

	public void anthropicServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException;
	
	public void anthropicServiceAllConsultsv37(String fileName, List<String> llmResult, boolean ia) throws IOException;

	public void geminiServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException;
	
	public Map<String, JsonResponse> geminiServiceAllConsultsToJson(String originalFilename, Map<String, JsonResponse> fileResults) throws IOException;
	
	public void deepSeekServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException;
	
	public void grokServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException;
	
	public Map<String, JsonResponse> grokServiceAllConsultsToJson(String originalFilename, Map<String, JsonResponse> fileResults) throws IOException;

	public Map<String, Object[]> extractedPercentageByLLM(String filePath) throws FileNotFoundException, IOException;

	public void writeResultsToNewSheet(String filePath, Map<String, Object[]> analysisResults) throws IOException;
//	public void completeProcessOfusCohereResult(MultipartFile file, String llm) throws IOException;

	void addDecryptXOR(String inputFilePath, String outFilePath) throws IOException;
	
	public void writeMapToFile(Map<String, List<String>> map, String fileName);

}
