package com.phd.llm.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phd.llm.model.others.JsonResponse;
import com.phd.llm.model.others.PathMultipartFile;
import com.phd.llm.service.DirectoryService;
import com.phd.llm.service.FileService;
import com.phd.llm.test.others.LogParser;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Autowired
    private FileService fileService;

    private static final String FILE_PATH = "2.resultsRootedCON/analysis_results_v2_rootedCON.xlsx";

    private List<String> prefixes = Arrays.asList(
        "1 flag",
        "2. base64",
        "3. anonymize",
        "4. obfus",
        "5. superobfus",
        "6. decryptedXOR",
        "7. decrypted",
        "8. decryptedXORVarOnly",
        "9. decryptedVarOnly",
        "10. decryptedXORDeadCode",
        "11. decryptedDeadCode",
        "12. decryptedXORSuperOfusDeadCode",
        "13. decryptedSuperOfusDeadCode"
    );

    @Override
    public void explore(String directoryPath, String llm, boolean ia) throws IOException {

        File directory = new File(directoryPath);
        // THIS IS THE CORRECT LINE
        listFilesInDirectory(directory, llm, ia);
        // CHECK IF IT IS THE CORRECT FOLDER

        // OLD VERSION
        // if (directory.exists() && directory.isDirectory()) {
        //     // List first-level items inside the main folder
        //     File[] subDirectories = directory.listFiles(File::isDirectory);
        //
        //     if (subDirectories != null) {
        //         for (File subDirectory : subDirectories) {
        //             System.out.println("Files in folder: " + subDirectory.getName());
        //             listFilesInDirectory(subDirectory, llm, ia);
        //         }
        //     } else {
        //         System.out.println("There are no subdirectories in the specified directory.");
        //     }
        // } else {
        //     System.out.println("The specified path is not a valid directory.");
        // }
    }

    @Override
    public void exploreJSON(String directoryPath, String llm, boolean ia) throws IOException {

        File directory = new File(directoryPath);
        listFilesInDirectoryToJson(directory, llm, ia);

    }

    public static String removePrefix(String fileName, List<String> prefixes) {
        for (String prefix : prefixes) {
            if (fileName.startsWith(prefix)) {
                return fileName.replaceFirst(prefix, "").trim();
            }
        }
        return fileName;
    }

    private void listFilesInDirectory(File directory, String llm, boolean ia) throws IOException {
        File[] files = directory.listFiles(File::isFile);

        HashMap<String, List<String>> previousAnalysisResults = new HashMap<>();
        try {
            if (files != null && files.length > 0) {
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File f1, File f2) {
                        // Extract test number
                        int num1 = extractNumber(f1.getName());
                        int num2 = extractNumber(f2.getName());
                        return Integer.compare(num1, num2);
                    }

                    // Method to extract the number from a file name
                    private int extractNumber(String filename) {
                        // Match numbers at the beginning of the file name
                        Pattern pattern = Pattern.compile("^\\d+");
                        Matcher matcher = pattern.matcher(filename);
                        if (matcher.find()) {
                            return Integer.parseInt(matcher.group()); // Return the number found
                        }
                        // If there is no number, put the file at the end
                        return Integer.MAX_VALUE;
                    }
                });

                for (File file : files) {

                    // Create MultipartFile
                    MultipartFile multipartFile = new PathMultipartFile(file.getPath());

                    // Full obfuscation process
                    String modifiedName = removePrefix(multipartFile.getOriginalFilename(), prefixes);
                    List<String> llmResult = new ArrayList<>();

                    // LLMs management --> Parameters (LLM, file, prompt and hashMap)
                    // If ia == false, just generate the files to process them later
                    if (!ia) {
                        fileService.fileModificationProcess(multipartFile);
                    } else {
                        if (llm.equals("ChatGPT")) {
                            fileService.openAIServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            // Export combined results to an Excel file
                            // previousAnalysisResults.put(multipartFile.getOriginalFilename(), llmResult);
                            addToMap(previousAnalysisResults, modifiedName + "ChatGPT", llmResult);
                            llmResult = new ArrayList<>();
                        } else if (llm.equals("Cohere")) {
                            fileService.cohereServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            // Export combined results to an Excel file
                            // previousAnalysisResults.put(multipartFile.getOriginalFilename(), llmResult);
                            addToMap(previousAnalysisResults, modifiedName + "Cohere", llmResult);
                            llmResult = new ArrayList<>();
                        } else if (llm.equals("Anthropic")) {
                            fileService.anthropicServiceAllConsultsv37(multipartFile.getOriginalFilename(), llmResult, ia);
                            // Export combined results to an Excel file
                            // previousAnalysisResults.put(multipartFile.getOriginalFilename(), llmResult);
                            addToMap(previousAnalysisResults, modifiedName + "Anthropic", llmResult);
                            llmResult = new ArrayList<>();
                        } else if (llm.equals("Gemini")) {
                            fileService.geminiServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            // Export combined results to an Excel file
                            // previousAnalysisResults.put(multipartFile.getOriginalFilename(), llmResult);
                            addToMap(previousAnalysisResults, modifiedName + "Gemini", llmResult);
                            llmResult = new ArrayList<>();
                        } else if (llm.equals("DeepSeek")) {
                            fileService.deepSeekServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            // Export combined results to an Excel file
                            // previousAnalysisResults.put(multipartFile.getOriginalFilename(), llmResult);
                            addToMap(previousAnalysisResults, modifiedName + "DeepSeek", llmResult);
                            llmResult = new ArrayList<>();
                        } else {

                            if (previousAnalysisResults.get(modifiedName + "ChatGPT") == null) {
                                llmResult.add("ChatGPT");
                            }
                            fileService.openAIServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            addToMap(previousAnalysisResults, modifiedName + "ChatGPT", llmResult);
                            llmResult = new ArrayList<>();

//                            if (previousAnalysisResults.get(modifiedName + "Cohere") == null) {
//                                llmResult.add("Cohere");
//                            }
//                            fileService.cohereServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
//                            addToMap(previousAnalysisResults, modifiedName + "Cohere", llmResult);
//                            llmResult = new ArrayList<>();

                            try {
                                // Pause execution for 1 second
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(previousAnalysisResults.size());

                            if (previousAnalysisResults.get(modifiedName + "Anthropic") == null) {
                                llmResult.add("Anthropic");
                            }
                            fileService.anthropicServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            addToMap(previousAnalysisResults, modifiedName + "Anthropic", llmResult);
                            llmResult = new ArrayList<>();

                            if (previousAnalysisResults.get(modifiedName + "Gemini") == null) {
                                llmResult.add("Gemini");
                            }
                            fileService.geminiServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            addToMap(previousAnalysisResults, modifiedName + "Gemini", llmResult);
                            llmResult = new ArrayList<>();

                            if (previousAnalysisResults.get(modifiedName + "Grok") == null) {
                                llmResult.add("Grok");
                            }
                            fileService.grokServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
                            addToMap(previousAnalysisResults, modifiedName + "Grok", llmResult);
                            llmResult = new ArrayList<>();

//                            if (previousAnalysisResults.get(modifiedName + "DeepSeek") == null) {
//                                llmResult.add("DeepSeek");
//                            }
//                            fileService.deepSeekServiceAllConsults(multipartFile.getOriginalFilename(), llmResult, ia);
//                            addToMap(previousAnalysisResults, modifiedName + "DeepSeek", llmResult);
//                            llmResult = new ArrayList<>();
                        }
                    }
                }

            } else {
                System.out.println("(No files in this folder)");
            }
        } catch (Exception e) {
            System.out.println("Error in " + e.getMessage());
        }

        fileService.exportAnalysisResults(previousAnalysisResults, FILE_PATH);
        Map<String, Object[]> analysisResults = fileService.extractedPercentageByLLM(FILE_PATH);

        fileService.writeMapToFile(previousAnalysisResults, "2.resultsRootedCON/analysis_results_rootedCON.txt");
        fileService.writeResultsToNewSheet(FILE_PATH, analysisResults);
        System.out.println("Data successfully exported to " + FILE_PATH);
    }

    private void listFilesInDirectoryToJson(File directory, String llm, boolean ia) throws IOException {
        File[] files = directory.listFiles(File::isFile);

        Map<String, JsonResponse> fileResults = new HashMap<>();

        if (files != null && files.length > 0) {

            for (File file : files) {

                // Create MultipartFile
                MultipartFile multipartFile = new PathMultipartFile(file.getPath());

                if (!ia) {
                    fileService.fileModificationProcess(multipartFile);
                } else {
                    // For OpenAI
                    fileResults = fileService.openAIServiceAllConsultsToJson(
                        multipartFile.getOriginalFilename(),
                        fileResults
                    );

                    try {
                        // Pause execution for 500 milliseconds
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // For Anthropic
                    fileResults = fileService.anthropicServiceAllConsultsToJson(
                        multipartFile.getOriginalFilename(),
                        fileResults
                    );
                    // For Gemini
                    fileResults = fileService.geminiServiceAllConsultsToJson(
                        multipartFile.getOriginalFilename(),
                        fileResults
                    );
                    // For Grok
                    fileResults = fileService.grokServiceAllConsultsToJson(
                        multipartFile.getOriginalFilename(),
                        fileResults
                    );
                }
            }

            // Define the output file path
            String filePath = "2.resultsRootedCON/outputFinal.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Loop through the Map and write each entry to the file
                for (Map.Entry<String, JsonResponse> entry : fileResults.entrySet()) {
                    String line = entry.getKey()
                        + " => Contains IP: " + entry.getValue().getContains_ip()
                        + ", IP: " + (entry.getValue().getIp() == null ? "null" : entry.getValue().getIp());

                    writer.write(line);
                    writer.newLine();
                }

                System.out.println("File written successfully: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String basePath = "C:\\\\Users\\\\Tecdata\\\\Documents\\\\workspace\\\\llm\\\\2.resultsRootedCON\\\\";
            filePath = basePath + "outputFinal.txt";
            String excelFilePath = basePath + "output.xlsx";

            LogParser.baseRootedExtracted(filePath, excelFilePath);
        }
    }

    public static void addToMap(HashMap<String, List<String>> map, String key, List<String> newValues) {
        // Check if the key already exists
        if (map.containsKey(key)) {
            // If it exists, add the new values to the existing list
            map.get(key).addAll(newValues);
        } else {
            // If it does not exist, create a new list and insert it
            map.put(key, new ArrayList<>(newValues));
        }
    }
}
