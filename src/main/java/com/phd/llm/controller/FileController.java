package com.phd.llm.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phd.llm.model.others.Categoria;
import com.phd.llm.service.DirectoryService;
import com.phd.llm.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/v1/api/files")
public class FileController {

	@Autowired
	private FileService fileService;

	@Autowired 
	private DirectoryService directoryService;

	private static final String UPLOAD_DIR = "uploads/";

//	private static final String FILE_PATH = "uploads1/analysis_results.xlsx";

	private static final Set<String> ALLOWED_TYPES = Set.of("chatgpt", "cohere", "anthropic", "gemini", "deepseek", "grok", "none", "all");

	@Operation(summary = "Upload File", description = "It lets upload a file to serve")
	@PostMapping(value = "/uploadFile", consumes = { "multipart/form-data" })
	public ResponseEntity<String> handleFileUpload(
			@Parameter(description = "Selecciona una categoría") @RequestParam(required = false, defaultValue = "") Categoria llmType,
			@Parameter(description = "Archivo a subir", required = true) @RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please, select a file to upload it.");
		}
		if (!ALLOWED_TYPES.contains(llmType.toString().toLowerCase())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"File type does not allow. Allowed values: chatgpt, gemini, cohere, anthropic or empty to do with all");
		}

		try {
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			fileService.completeProcessOfusLLMResult(file, llmType.toString());

			return ResponseEntity.ok("Finished process: " + file.getOriginalFilename());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error to upload file: " + e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/updaloadFolder")
	public ResponseEntity<String> handleFolderUpload(String directory,
			@Parameter(description = "Select a category") @RequestParam(required = false, defaultValue = "") Categoria llmType, boolean ia) {

		if (!ALLOWED_TYPES.contains(llmType.toString().toLowerCase())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"File type does not allow. Allowed values: chatgpt, gemini, cohere, anthropic, deepseek or grok or empty to do with all");
		}

		try {
			directoryService.explore(directory, llmType.toString(), ia);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok("Finished process:");
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/updaloadFolderToJson")
	public ResponseEntity<String> handleFolderUploadtoJson(String directory,
			@Parameter(description = "Select a category") @RequestParam(required = false, defaultValue = "") Categoria llmType, boolean ia) {

		if (!ALLOWED_TYPES.contains(llmType.toString().toLowerCase())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"File type does not allow. Allowed values: chatgpt, gemini, cohere, anthropic or empty to do with all");
		}

		try {
			directoryService.exploreJSON(directory, llmType.toString(), ia);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok("Finished process:");
	}
}
