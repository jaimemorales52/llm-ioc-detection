package com.phd.llm.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phd.llm.model.others.JsonHandler;
import com.phd.llm.model.others.JsonResponse;
import com.phd.llm.service.AnthropicService;
import com.phd.llm.service.CohereService;
import com.phd.llm.service.DeepSeekService;
import com.phd.llm.service.FileService;
import com.phd.llm.service.GeminiService;
import com.phd.llm.service.GrokService;
import com.phd.llm.service.OpenAIService;

@Service
public class FileServiceImpl implements FileService {

    private static final String UPLOAD_DIR = "1.filesRootedCON/";

    private static final String FILE_PATH = "2.resultsRootedCON/analysis_results.xlsx";

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private CohereService cohereService;

    @Autowired
    private AnthropicService anthropicService;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private GrokService grokService;

    // Generates a random name of the given length
    private static String generateRandomName(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < length; i++) {
            name.append(characters.charAt(random.nextInt(characters.length())));
        }
        return name.toString();
    }

    public static String removeComments(String code) {
        // Regex to find single-line and multi-line comments
        String regex = "(//.*?$)|(/\\*.*?\\*/)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(code);

        // Remove comments using the regex
        return matcher.replaceAll("");
    }

    public void anonymizeJavaScriptFile(MultipartFile multiPartFile, String outputFilePath) throws IOException {

        // Map to store original names and their anonymized replacements
        Map<String, String> anonymizedNames = new HashMap<>();

        String content = new String(multiPartFile.getBytes(), StandardCharsets.UTF_8);

        content = removeComments(content);
        // Regex to capture variable and function names
        Pattern pattern = Pattern.compile("\\b(var|let|const|function)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(content);

        // Find all variable and function names
        while (matcher.find()) {
            String originalName = matcher.group(2);
            anonymizedNames.putIfAbsent(originalName, generateRandomName(5));
        }
        content = removeComments(content);

        // Replace all original names with their anonymized equivalents
        for (Map.Entry<String, String> entry : anonymizedNames.entrySet()) {
            content = content.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        // Save the anonymized content to a new file
        Files.write(Paths.get(outputFilePath), content.getBytes());
        System.out.println("Anonymized file saved to: " + outputFilePath);

    }

    // Main method to anonymize based on file paths
    public void anonymizeJavaScriptFile(String inputFilePath, String outputFilePath) throws IOException {

        // Map to store original names and their anonymized replacements
        Map<String, String> anonymizedNames = new HashMap<>();
        // Read the content of the JavaScript file
        String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
        content = removeComments(content);

        // Regex to capture variable and function names
        Pattern pattern = Pattern.compile("\\b(var|let|const|function)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(content);

        // Find all variable and function names
        while (matcher.find()) {
            String originalName = matcher.group(2);
            anonymizedNames.putIfAbsent(originalName, generateRandomName(5));
        }

        // Replace all original names with their anonymized equivalents
        for (Map.Entry<String, String> entry : anonymizedNames.entrySet()) {
            content = content.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        // Save the anonymized content to a new file
        Files.write(Paths.get(outputFilePath), content.getBytes());
        System.out.println("Anonymized file saved to: " + outputFilePath);
    }

    // Main method to obfuscate the file
    public void obfuscateAdvancedJavaScriptFile(String inputFilePath, String outputFilePath) throws IOException {

        Map<String, String> obfuscatedNames = new HashMap<>();
        // Read the content of the JavaScript file
        String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));

        // Regex to capture variable and function names
        Pattern pattern = Pattern.compile("\\b(var|let|const|function)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(content);

        // Find all variable and function names and replace them
        while (matcher.find()) {
            String originalName = matcher.group(2);
            obfuscatedNames.putIfAbsent(originalName, generateRandomName(8));
        }

        // Replace all original names with their obfuscated equivalents
        for (Map.Entry<String, String> entry : obfuscatedNames.entrySet()) {
            content = content.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        // Obfuscate strings in Base64 and reversed
        Pattern stringPattern = Pattern.compile("\"(.*?)\"");
        Matcher stringMatcher = stringPattern.matcher(content);
        while (stringMatcher.find()) {
            String originalString = stringMatcher.group(1);
            String base64String = encodeBase64(reverseString(originalString));
            content = content.replace("\"" + originalString + "\"",
                    "decodeString(atob(\"" + base64String + "\".split('').reverse().join('')))"); 
        }

        // Add helper functions and dummy code
        content = injectDummyCode() + "\n" + "function decodeString(str) { return str.split('').reverse().join(''); }\n"
                + content;

        // Save the obfuscated content to a new file
        Files.write(Paths.get(outputFilePath), content.getBytes());
        System.out.println("Obfuscated JavaScript file saved to: " + outputFilePath);
    }

    // Injects dummy code and conditionals
    private static String injectDummyCode() {
        StringBuilder dummyCode = new StringBuilder();
        dummyCode.append("var _dummy = function() { for (var i = 0; i < 10; i++) { if (i % 2 == 0) { continue; } } };");
        dummyCode.append("function _noise() { return Math.random() > 0.5 ? _dummy() : _dummy(); }");
        return dummyCode.toString();
    }

    // Encodes a string to Base64
    private static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    // Converts a string to hexadecimal representation
    private static String toHexString(String str) {
        StringBuilder hex = new StringBuilder();
        for (char ch : str.toCharArray()) {
            hex.append(String.format("\\x%02X", (int) ch));
        }
        return hex.toString();
    }

    // Reverses a string
    private static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    @Override
    public void obfuscateAdvancedJavaScriptFile(MultipartFile multiPartFile, String outputFilePath) throws IOException {

        Map<String, String> obfuscatedNames = new HashMap<>();
        // Read the content of the JavaScript file
        String content = new String(multiPartFile.getBytes(), StandardCharsets.UTF_8);
        content = removeComments(content);

        // Regex to capture variable and function names
        Pattern pattern = Pattern.compile("\\b(var|let|const|function)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(content);

        // Find all variable and function names and replace them
        while (matcher.find()) {
            String originalName = matcher.group(2);
            obfuscatedNames.putIfAbsent(originalName, generateRandomName(8));
        }

        // Replace all original names with their obfuscated equivalents
        for (Map.Entry<String, String> entry : obfuscatedNames.entrySet()) {
            content = content.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        // Obfuscate strings in Base64 and reversed
        Pattern stringPattern = Pattern.compile("\"(.*?)\"");
        Matcher stringMatcher = stringPattern.matcher(content);
        while (stringMatcher.find()) {
            String originalString = stringMatcher.group(1);
            String base64String = encodeBase64(reverseString(originalString));
            content = content.replace("\"" + originalString + "\"",
                    "decodeString(atob(\"" + base64String + "\".split('').reverse().join('')))"); 
        }

        // Add helper functions and dummy code
        content = injectDummyCode() + "\n" + "function decodeString(str) { return str.split('').reverse().join(''); }\n"
                + content;

        // Save the obfuscated content to a new file
        Files.write(Paths.get(outputFilePath), content.getBytes());
        System.out.println("Obfuscated JavaScript file saved to: " + outputFilePath);
    }

    @Override
    public void addDecryptAES(MultipartFile multipartFile, String outFilePath) throws IOException {

        // Import line for Node.js
        String importCryptoJs = "// Import 'crypto-js' library if you are in Node.js\n"
                + "const CryptoJS = require(\"crypto-js\");\n";

        // Decryption and execution function code
        String decryptFunction = "\n// Function to decrypt using AES\n"
                + "function decryptAES(encryptedData, secretKey) {\n"
                + "    const bytes = CryptoJS.AES.decrypt(encryptedData, secretKey);\n"
                + "    return bytes.toString(CryptoJS.enc.Utf8);\n" + "}\n"
                + "let secretKey = \"mySecretKey123\";\n\n"
                + "// Encrypted message\n"
                + "let encryptedMessage = \"U2FsdGVkX1/8bCMGYEcBjKH7hZfkXraXs7WyYg5EaQ4=\";\n\n"
                + "// Decrypt message\n"
                + "let decryptedMessage = decryptAES(encryptedMessage, secretKey);\n"
                + "console.log(decryptedMessage);\n";

        try {
            // Read existing JavaScript file content
            // String content = new String(Files.readAllBytes(Paths.get(outFilePath)));
            String content = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
            // Add import at the beginning and decryption code at the end
            content = importCryptoJs + "\n" + content + "\n\n" + decryptFunction;

            // Write updated content to the output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilePath))) {
                writer.write(content);
                System.out.println("JavaScript code successfully added to file.");
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        }

    }

    @Override
    public void addFlag(MultipartFile multiPartFile, String outputFilePath) throws IOException {
        String ipVariable = "192.168.17.65"; // Change this to the IP you want to add

        // Line to be appended
        String lineToAdd = "var serverIP = '" + ipVariable + "';";
        String lineToLog = "console.log(serverIP);"; // Line to log the value to console

        // Use a StringBuilder to construct the new file content
        StringBuilder contentBuilder = new StringBuilder();

        // Read existing content from MultipartFile
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multiPartFile.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Append new lines at the end
        contentBuilder.append(lineToAdd).append(System.lineSeparator());
        contentBuilder.append(lineToLog).append(System.lineSeparator());

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath)))) {
            writer.write(contentBuilder.toString());
            System.out.println("JS file updated and saved to: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to remove lines containing a specific keyword
    public static String removeLinesContaining(String inputText, String lineToRemove) {
        StringBuilder result = new StringBuilder();
        String[] lines = inputText.split("\n"); // Split text into lines

        for (String line : lines) {
            // If the line does not contain the keyword, add it to the result
            if (!line.contains(lineToRemove)) {
                result.append(line).append("\n");
            }
        }
        return result.toString().trim(); // Return text without removed lines
    }

    @Override
    public void addDecryptAES(String inputFilePath, String outFilePath) throws IOException {

        // Import line for Node.js
        String importCryptoJs = "// Import 'crypto-js' library if you are in Node.js\n"
                + "const CryptoJS = require(\"crypto-js\");\n";

        // Decryption and execution function code
        String decryptFunction = "\n// Function to decrypt using AES\n"
                + "function decryptAES(encryptedData, secretKey) {\n"
                + "    const bytes = CryptoJS.AES.decrypt(encryptedData, secretKey);\n"
                + "    return bytes.toString(CryptoJS.enc.Utf8);\n" + "}\n"
                + "let secretKey = \"mySecretKey123\";\n\n"
                + "// Encrypted message\n"
                + "let encryptedMessage = \"U2FsdGVkX1/A/6wHmBj8+Fry+QrYVv97+87j7QLl5ZY=\";\n\n"
                + "// Decrypt message\n"
                + "let decryptedMessage = decryptAES(encryptedMessage, secretKey);\n"
                + "console.log(decryptedMessage);\n";

        try {
            // Read existing JavaScript file content
            String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            // Remove IP-related lines and add import and decrypt code
            content = removeLinesContaining(content, "var serverIP =");
            content = removeLinesContaining(content, "console.log(serverIP);");
            content = importCryptoJs + "\n" + content + "\n\n" + decryptFunction;

            // Write updated content to the output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilePath))) {
                writer.write(content);
                System.out.println("JavaScript code successfully added to file.");
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        }
    }

    @Override
    public void addDecryptXOR(String inputFilePath, String outFilePath) throws IOException {

        // Import line for Node.js
        String importCryptoJs = "// Import 'crypto-js' library if you are in Node.js\n"
                + "const CryptoJS = require(\"crypto-js\");\n";

        String decryptFunction = "function xorDecrypt(key, encryptedText) {\n"
                + "  let decryptedText = '';\n"
                + "  let encryptedTextBytes = Buffer.from(encryptedText, 'base64');\n"
                + "  for (let i = 0; i < encryptedTextBytes.length; i++) {\n"
                + "    decryptedText += String.fromCharCode(encryptedTextBytes[i] ^ key.charCodeAt(i % key.length));\n"
                + "  }\n" + "  return decryptedText;\n" + "}\n "
                + "let encryptedText = \"31544b5d54554a4b4545534c\"\n"
                + "let key = \"mysecretkey\"";

        try {
            // Read existing JavaScript file content
            String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            // Remove IP-related lines and add import and decrypt code
            content = removeLinesContaining(content, "var serverIP =");
            content = removeLinesContaining(content, "console.log(serverIP);");
            content = importCryptoJs + "\n" + content + "\n\n" + decryptFunction;

            // Write updated content to the output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilePath))) {
                writer.write(content);
                System.out.println("JavaScript code successfully added to file.");
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        }
    }

    // Generates extra obfuscated JavaScript wrapper code
    public void generateExtraObfuscatedJavaScript(String inputFilePath, String outputFilePath) throws IOException {

        String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
        // Convert original script to Base64
        String base64Script = Base64.getEncoder().encodeToString(content.getBytes());

        // Build obfuscated JavaScript code with Base64 decoding wrapper
        String obfuscatedScript = "(function(){\n"
                + "    var decodedScript = atob('" + base64Script + "');\n"
                + "    eval(decodedScript);\n"
                + "})();";

        Files.write(Paths.get(outputFilePath), obfuscatedScript.getBytes());
        System.out.println("Obfuscated JavaScript file saved to: " + outputFilePath);

    }

    public void encodeAndReplaceServerIP(String inputFilePath, String outputFilePath) throws IOException {
        // Read JavaScript file content
        Path path = Path.of(inputFilePath);
        String content = Files.readString(path);

        // Regex to find serverIP variable using single or double quotes
        Pattern pattern = Pattern.compile("var\\s+serverIP\\s*=\\s*['\\\"]([^'\\\"]+)['\\\"];");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            // Extract serverIP value
            String serverIP = matcher.group(1);

            // Encode value in Base64
            String encodedServerIP = Base64.getEncoder().encodeToString(serverIP.getBytes());

            // Create replacement in original format
            String replacement = "var serverIP = \"" + encodedServerIP + "\";";

            // Replace content
            String newContent = matcher.replaceFirst(replacement);

            // Write modified content back to file
            Files.write(Paths.get(outputFilePath), newContent.getBytes());

            System.out.println("serverIP value successfully replaced with Base64 encoding: " + encodedServerIP);
        } else {
            System.out.println("serverIP variable not found in file.");
        }
    }

    public void writeMapToFile(Map<String, List<String>> map, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Iterate over the map
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();

                // Write the key
                writer.write(key + ":");
                writer.newLine();

                // Write the values associated with the key
                for (String value : values) {
                    writer.write("  - " + value);
                    writer.newLine();
                }

                writer.newLine(); // Blank line between entries
            }
            System.out.println("File written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportAnalysisResults(Map<String, List<String>> analysisResults, String fileName) throws IOException {
        Workbook workbook;
        Sheet sheet;

        String fileResultAux = "";
        List<String> suffixes = Arrays.asList("Gemini", "Cohere", "ChatGPT", "Anthropic", "Grok");

        // Load file if it exists, or create a new one
        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            workbook = new XSSFWorkbook(fileIn);
            sheet = workbook.getSheetAt(0);

        } catch (IOException e) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Analysis Results");

            // Create header row if the file is new
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("File Name");
            header.createCell(1).setCellValue("LLM");
            header.createCell(2).setCellValue("1. Flag");
            header.createCell(3).setCellValue("2. Flag Base 64");
            header.createCell(4).setCellValue("3. Rename Var & Functions");
            header.createCell(5).setCellValue("4. Obfuscated Dead Code");
            header.createCell(6).setCellValue("5. Super Obfuscated Dead Code");
            header.createCell(7).setCellValue("6. Encrypted Flag XOR");
            header.createCell(8).setCellValue("7. Encrypted Flag");
            header.createCell(9).setCellValue("8. Encrypted Flag XOR & Rename");
            header.createCell(10).setCellValue("9. Encrypted Flag & Rename");
            header.createCell(11).setCellValue("10. Encrypted Flag XOR & Obfuscated");
            header.createCell(12).setCellValue("11. Encrypted Flag & Obfuscated");
            header.createCell(13).setCellValue("12. Encrypted Flag XOR & Advanced Obfuscated");
            header.createCell(14).setCellValue("13. Encrypted Flag & Advanced Obfuscated");
        }

        // Detect last row with data
        int lastRowNum = sheet.getLastRowNum();

        CellStyle yesStyle = workbook.createCellStyle();
        yesStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        yesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle noStyle = workbook.createCellStyle();
        noStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        noStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle doubtStyle = workbook.createCellStyle();
        doubtStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        doubtStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Add data from HashMap to file
        for (Map.Entry<String, List<String>> entry : analysisResults.entrySet()) {
            Row row = sheet.createRow(++lastRowNum);

            fileResultAux = entry.getKey();
            // Remove any suffix from the list if present
            for (String suffix : suffixes) {
                if (entry.getKey().endsWith(suffix)) {
                    fileResultAux = entry.getKey().substring(0, entry.getKey().length() - suffix.length());
                    break; // Exit once suffix is found and removed
                }
            }

            row.createCell(0).setCellValue(fileResultAux);
            List<String> results = entry.getValue();
            for (int j = 0; j < results.size(); j++) {

                Cell cell = row.createCell(j + 1);

                cell.setCellValue(results.get(j));
                if (results.get(j).toLowerCase().contains("no sé")) {
                    cell.setCellValue("Don't know");
                    cell.setCellStyle(doubtStyle);
                } else if (results.get(j).toLowerCase().contains("no")) {
                    cell.setCellValue("No");
                    cell.setCellStyle(noStyle);
                } else if (results.get(j).toLowerCase().contains("sí")) {
                    cell.setCellValue("Yes");
                    cell.setCellStyle(yesStyle);
                }
            }
        }

        // Save changes
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    // Method that runs the full file creation/modification process and calls the
    // specific LLM to generate results for each file
    public void completeProcessOfusLLMResult(MultipartFile file, String llmType) throws IOException {

        Map<String, List<String>> previousAnalysisResults = new HashMap<>();
        List<String> llmResult = new ArrayList<String>();

        // Full obfuscation process
        fileModificationProcess(file);

        if (llmType.equals("ChatGPT")) {
            openAIServiceAllConsults(file.getOriginalFilename(), llmResult);
            // Export combined results to an Excel file
            previousAnalysisResults.put(file.getOriginalFilename(), llmResult);
        } else if (llmType.equals("Cohere")) {
            cohereServiceAllConsults(file.getOriginalFilename(), llmResult);
            // Export combined results to an Excel file
            previousAnalysisResults.put(file.getOriginalFilename(), llmResult);
        } else if (llmType.equals("Anthropic")) {
            anthropicServiceAllConsults(file.getOriginalFilename(), llmResult);
            // Export combined results to an Excel file
            previousAnalysisResults.put(file.getOriginalFilename(), llmResult);
        } else if (llmType.equals("Gemini")) {
            geminiServiceAllConsults(file.getOriginalFilename(), llmResult);
            // Export combined results to an Excel file
            previousAnalysisResults.put(file.getOriginalFilename(), llmResult);
        } else if (llmType.equals("DeepSeek")) {
            deepSeekServiceAllConsults(file.getOriginalFilename(), llmResult);
            // Export combined results to an Excel file
            previousAnalysisResults.put(file.getOriginalFilename(), llmResult);
        } else {
            openAIServiceAllConsults(file.getOriginalFilename(), llmResult);
            previousAnalysisResults.put(file.getOriginalFilename() + "ChatGPT", llmResult);
            llmResult = new ArrayList<String>();
//          cohereServiceAllConsults(file.getOriginalFilename(), llmResult);
//          previousAnalysisResults.put(file.getOriginalFilename() + "Cohere", llmResult);
//          llmResult = new ArrayList<String>();
            anthropicServiceAllConsults(file.getOriginalFilename(), llmResult);
            previousAnalysisResults.put(file.getOriginalFilename() + "Anthropic", llmResult);
            llmResult = new ArrayList<String>();

//          deepSeekServiceAllConsults(file.getOriginalFilename(), llmResult);
//          previousAnalysisResults.put(file.getOriginalFilename() + "DeepSeek", llmResult);
//          llmResult = new ArrayList<String>();

            grokServiceAllConsults(file.getOriginalFilename(), llmResult);
            previousAnalysisResults.put(file.getOriginalFilename() + "DeepSeek", llmResult);
            llmResult = new ArrayList<String>();

            geminiServiceAllConsults(file.getOriginalFilename(), llmResult);
            // Export combined results to an Excel file
            previousAnalysisResults.put(file.getOriginalFilename() + "Gemini", llmResult);
//          we do not add it to keep the line below working when called separately
//          previousAnalysisResults.put(file.getOriginalFilename(), llmResult);
//          llmResult = new ArrayList<String>();
            // add logic to run all models
        }

        // Export combined results to an Excel file
//      previousAnalysisResults.put(file.getOriginalFilename(), llmResult);

        exportAnalysisResults(previousAnalysisResults, FILE_PATH);
        writeMapToFile(previousAnalysisResults, "uploadsXOR/analysis_results.txt");
        Map<String, Object[]> analysisResults = extractedPercentageByLLM(FILE_PATH);

        writeResultsToNewSheet(FILE_PATH, analysisResults);
        System.out.println("Data successfully exported to " + FILE_PATH);
    }

    // File creation and modification process to then call the LLM
    public void fileModificationProcess(MultipartFile file) throws IOException {

        Path path = Paths.get(UPLOAD_DIR + "1 flag" + file.getOriginalFilename());

        // Check if the modification process has already been done to avoid repeating it
        if (Files.exists(path)) {
            System.out.println(
                    "The file has already been through the obfuscation and encryption process. Stopping.");
            return;
        }
        // ADD FLAG
        addFlag(file, UPLOAD_DIR + "1 flag" + file.getOriginalFilename());
        System.out.println("1. Add flag");

        // ENCODE FLAG
        encodeAndReplaceServerIP(UPLOAD_DIR + "1 flag" + file.getOriginalFilename(),
                UPLOAD_DIR + "2. base64" + file.getOriginalFilename());
        System.out.println("2. Encode added flag");

        // ANONYMIZE VARIABLES AND FUNCTIONS
        anonymizeJavaScriptFile(UPLOAD_DIR + "1 flag" + file.getOriginalFilename(),
                UPLOAD_DIR + "3. anonymize" + file.getOriginalFilename());
        System.out.println("3. Anonymize variables and functions");

        // ADD SOME OBFUSCATION TO CODE
        obfuscateAdvancedJavaScriptFile(UPLOAD_DIR + "1 flag" + file.getOriginalFilename(),
                UPLOAD_DIR + "4. obfus" + file.getOriginalFilename());

        // COMPLEX OBFUSCATION
        generateExtraObfuscatedJavaScript(UPLOAD_DIR + "4. obfus" + file.getOriginalFilename(),
                UPLOAD_DIR + "5. superobfus" + file.getOriginalFilename());

        // ADD XOR VARIABLE
        addDecryptXOR(UPLOAD_DIR + "1 flag" + file.getOriginalFilename(),
                UPLOAD_DIR + "6. decryptedXOR" + file.getOriginalFilename());

        // ADD ENCRYPTED VARIABLE
        addDecryptAES(UPLOAD_DIR + "1 flag" + file.getOriginalFilename(),
                UPLOAD_DIR + "7. decrypted" + file.getOriginalFilename());

        // ANONYMIZE VARIABLES AND FUNCTIONS
        anonymizeJavaScriptFile(UPLOAD_DIR + "6. decryptedXOR" + file.getOriginalFilename(),
                UPLOAD_DIR + "8. decryptedXORVarOnly" + file.getOriginalFilename());

        anonymizeJavaScriptFile(UPLOAD_DIR + "7. decrypted" + file.getOriginalFilename(),
                UPLOAD_DIR + "9. decryptedVarOnly" + file.getOriginalFilename());

        // ADD SOME OBFUSCATION TO CODE
        obfuscateAdvancedJavaScriptFile(UPLOAD_DIR + "6. decryptedXOR" + file.getOriginalFilename(),
                UPLOAD_DIR + "10. decryptedXORDeadCode" + file.getOriginalFilename());

        obfuscateAdvancedJavaScriptFile(UPLOAD_DIR + "7. decrypted" + file.getOriginalFilename(),
                UPLOAD_DIR + "11. decryptedDeadCode" + file.getOriginalFilename());

        // COMPLEX OBFUSCATION
        generateExtraObfuscatedJavaScript(UPLOAD_DIR + "10. decryptedXORDeadCode" + file.getOriginalFilename(),
                UPLOAD_DIR + "12. decryptedXORSuperOfusDeadCode" + file.getOriginalFilename());

        generateExtraObfuscatedJavaScript(UPLOAD_DIR + "11. decryptedDeadCode" + file.getOriginalFilename(),
                UPLOAD_DIR + "13. decryptedSuperOfusDeadCode" + file.getOriginalFilename());
    }

    public void openAIServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException {

        llmResult.add(openAIService.getOpenAIResponse("¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                // fileName))))
                + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));
    }

    public Map<String, JsonResponse> openAIServiceAllConsultsToJson(String fileName,
            Map<String, JsonResponse> fileResults1) throws IOException {

        String responseOpenAI = (openAIService.getOpenAIResponse(
                "I am a security analyst and I want to check if those JS code has an IP,"
                        + " I want to know only YES, NO, Don't KNOW and in case of YES, give me the IP."
                        + "The results should be in ONLY a JSON with this format example:" + fileName + ": {\r\n"
                        + "    \"contains_ip\": \"YES, NO, Don't KNOW\",\r\n"
                        + "    \"ip\": \"x.x.x.x\"\r\n" + "  },: "
                        // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                        // fileName))))
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

        Map<String, JsonResponse> fileResults = new HashMap<String, JsonResponse>();

        try {
            // Regex pattern to extract JSON
            Pattern pattern = Pattern.compile("\\{[\\s\\S]*\\}");
            Matcher matcher = pattern.matcher(responseOpenAI);

            if (matcher.find()) {
                // Extract JSON
                String jsonString = matcher.group(0);
                if (!jsonString.contains(fileName.subSequence(4, fileName.length()))) {
                    jsonString = jsonString.replaceFirst("\\{", "{\"" + fileName + "\":{ ");
                    jsonString = jsonString.replaceAll("\\}", "}}");
                }
//              System.out.println(jsonString);
                fileResults = JsonHandler.parseJsonResponse(jsonString);

                // Step 2: Process parsed data (example: put into map)
                if (fileResults != null) {
                    fileResults.forEach((filename, fileResult) -> {
                        fileResults1.put(fileName + "ChatGPT", fileResult);
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileResults1;
    }

    public void cohereServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException {

        llmResult.add(cohereService.getCohereResponse("¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                // fileName))))
                + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

    }

    public void anthropicServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException {

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                        // fileName))))
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

    }

    public void anthropicServiceAllConsultsv37(String fileName, List<String> llmResult, boolean ia)
            throws IOException {

        llmResult.add(anthropicService.getAnthropicResponsev37(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                        // fileName))))
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

    }

    public Map<String, JsonResponse> anthropicServiceAllConsultsToJson(String fileName,
            Map<String, JsonResponse> fileResults1) throws IOException {

        String responseOpenAI = (anthropicService.getAnthropicResponse(
                "I am a security analyst and I want to check if those JS code has an IP,"
                        + " I want to know only YES, NO, Don't KNOW and in case of YES, give me the IP."
                        + "The results should be in ONLY a JSON with this format example:" + fileName + ": {\r\n"
                        + "    \"contains_ip\": \"YES, NO, Don't KNOW\",\r\n"
                        + "    \"ip\": \"x.x.x.x\"\r\n" + "  },: "
                        // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                        // fileName))))
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

        Map<String, JsonResponse> fileResults = new HashMap<String, JsonResponse>();

        try {
            // Regex pattern to extract JSON
            Pattern pattern = Pattern.compile("\\{[\\s\\S]*\\}");
            Matcher matcher = pattern.matcher(responseOpenAI);

            if (matcher.find()) {
                // Extract JSON
                String jsonString = matcher.group(0);
                if (!jsonString.contains(fileName.subSequence(4, fileName.length()))) {
                    jsonString = jsonString.replaceFirst("\\{", "{\"" + fileName + "\":{ ");
                    jsonString = jsonString.replaceAll("\\}", "}}");
                }
                fileResults = JsonHandler.parseJsonResponse(jsonString);

                // Step 2: Process parsed data (example: put into map)
                if (fileResults != null) {
                    fileResults.forEach((filename, fileResult) -> {
                        fileResults1.put(fileName + "Anthropic", fileResult);
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileResults1;
    }

    public void geminiServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException {

        llmResult.add(geminiService.getGeminiResponse("¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                // fileName))))
                + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

    }

    public void grokServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException {

        llmResult.add(grokService.getGrokResponse("¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                // fileName))))
                + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

    }

    public Map<String, JsonResponse> geminiServiceAllConsultsToJson(String fileName,
            Map<String, JsonResponse> fileResults1) throws IOException {

        String responseOpenAI = (geminiService.getGeminiResponse(
                "I am a security analyst and I want to check if those JS code has an IP,"
                        + " I want to know only YES, NO, Don't KNOW and in case of YES, give me the IP."
                        + "The results should be in ONLY a JSON with this format example:" + fileName + ": {\r\n"
                        + "    \"contains_ip\": \"YES, NO, Don't KNOW\",\r\n"
                        + "    \"ip\": \"x.x.x.x\"\r\n" + "  },: "
                        // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                        // fileName))))
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

        Map<String, JsonResponse> fileResults = new HashMap<String, JsonResponse>();

        try {
            // Regex pattern to extract JSON
            // Regex Pattern (only matches 5-character filenames)
            Pattern pattern = Pattern.compile("\\s*\"([a-z]{5})\\.js\"");

            // Matcher for first case (should match)
            Matcher matcher1 = pattern.matcher(responseOpenAI);
            String randomName = "";
            if (matcher1.find()) {
                randomName = matcher1.group(1); // Extracted filename without extension
                responseOpenAI = responseOpenAI.replace(randomName, fileName);
                System.out.println("Extracted Filename: " + randomName);
            }

            pattern = Pattern.compile("\\{[\\s\\S]*\\}");
            Matcher matcher = pattern.matcher(responseOpenAI);

            if (matcher.find()) {
                // Extract JSON
                String jsonString = matcher.group(0);

                if (!matcher1.find() && !jsonString.contains(fileName.subSequence(4, fileName.length()))) {
                    jsonString = jsonString.replaceFirst("\\{", "{\"" + fileName + "\":{ ");
                    jsonString = jsonString.replaceAll("\\}", "}}");
                    System.out.println();
                }

                fileResults = JsonHandler.parseJsonResponse(jsonString);

                // Step 2: Process parsed data (example: put into map)
                if (fileResults != null) {
                    fileResults.forEach((filename, fileResult) -> {
                        fileResults1.put(fileName + "Gemini", fileResult);
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileResults1;
    }

    public Map<String, JsonResponse> grokServiceAllConsultsToJson(String fileName,
            Map<String, JsonResponse> fileResults1) throws IOException {

        String responseOpenAI = (grokService.getGrokResponse(
                "I am a security analyst and I want to check if those JS code has an IP,"
                        + " I want to know only YES, NO, Don't KNOW and in case of YES, give me the IP."
                        + "The results should be in ONLY a JSON with this format example:" + fileName + ": {\r\n"
                        + "    \"contains_ip\": \"YES, NO, Don't KNOW\",\r\n"
                        + "    \"ip\": \"x.x.x.x\"\r\n" + "  },: "
                        // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                        // fileName))))
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

        Map<String, JsonResponse> fileResults = new HashMap<String, JsonResponse>();

        try {
            // Regex pattern to extract JSON
            // Regex Pattern (only matches 5-character filenames)
            Pattern pattern = Pattern.compile("\\s*\"([a-z]{5})\\.js\"");

            // Matcher for first case (should match)
            Matcher matcher1 = pattern.matcher(responseOpenAI);
            String randomName = "";
            if (matcher1.find()) {
                randomName = matcher1.group(1); // Extracted filename without extension
                responseOpenAI = responseOpenAI.replace(randomName, fileName);
                System.out.println("Extracted Filename: " + randomName);
            }

            pattern = Pattern.compile("\\{[\\s\\S]*\\}");
            Matcher matcher = pattern.matcher(responseOpenAI);

            if (matcher.find()) {
                // Extract JSON
                String jsonString = matcher.group(0);

                if (!matcher1.find() && !jsonString.contains(fileName.subSequence(4, fileName.length()))) {
                    jsonString = jsonString.replaceFirst("\\{", "{\"" + fileName + "\":{ ");
                    jsonString = jsonString.replaceAll("\\}", "}}");
                    System.out.println();
                }

                fileResults = JsonHandler.parseJsonResponse(jsonString);

                // Step 2: Process parsed data (example: put into map)
                if (fileResults != null) {
                    fileResults.forEach((filename, fileResult) -> {
                        fileResults1.put(fileName + "Grok", fileResult);
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileResults1;
    }

    public void deepSeekServiceAllConsults(String fileName, List<String> llmResult, boolean ia) throws IOException {

        llmResult.add(deepSeekService.getDeepSeekResponse("¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                // + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" +
                // fileName))))
                + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + fileName)))));

    }

    public Map<String, Object[]> extractedPercentageByLLM(String filePath) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));

        // Load Excel file
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0); // First sheet

        // Map to store results: type -> { [39 counters for yes/no/don't know], total
        // line count }
        Map<String, int[]> typeCounts = new HashMap<>();
        Map<String, Integer> typeLineCounts = new HashMap<>();

        // Ignore first row and start from second
        boolean isFirstRow = true;
        for (Row row : sheet) {
            if (isFirstRow) {
                isFirstRow = false;
                continue; // Skip header row
            }

            // Get type from second column
            Cell typeCell = row.getCell(1);
            if (typeCell == null || typeCell.getCellType() != CellType.STRING) {
                continue; // Skip rows without a valid type
            }
            String type = typeCell.getStringCellValue();

            // Initialize counters and line count for this type if not present
            typeCounts.putIfAbsent(type, new int[39]); // 39 elements: three per analysis column (yes/no/don't know)
            typeLineCounts.put(type, typeLineCounts.getOrDefault(type, 0) + 1); // Total lines per type

            // Count "Sí", "No" and "No sabe" in each column from 3 to 14
            for (int i = 2, index = 0; i <= 14; i++, index += 3) {
                Cell cell = row.getCell(i);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue().toLowerCase();
                    if (cellValue.length() < 4 && cellValue.contains("no sé")) {
                        typeCounts.get(type)[index + 2]++; // "Don't know" counter
                    } else if (cellValue.length() < 4 && cellValue.contains("no")) {
                        typeCounts.get(type)[index + 1]++; // "No" counter
                    } else if (cellValue.length() < 4 && cellValue.contains("sí")) {
                        typeCounts.get(type)[index]++; // "Yes" counter
                    }
                }
            }
        }

        // Calculate and print percentages
        for (Map.Entry<String, int[]> entry : typeCounts.entrySet()) {
            String type = entry.getKey();
            int[] counts = entry.getValue();
            int totalLines = typeLineCounts.get(type);

            System.out.println("Results for type: " + type);
            System.out.println(" - Total lines: " + totalLines);

            for (int i = 0; i < 13; i++) { // Iterate over each analysis column (3 to 15)
                int yesCount = counts[i * 3];
                int noCount = counts[i * 3 + 1];
                int noAnswerCount = counts[i * 3 + 2];
                int total = yesCount + noCount + noAnswerCount;

                double yesPercentage = (total > 0) ? (yesCount * 100.0) / total : 0;
                double noPercentage = (total > 0) ? (noCount * 100.0) / total : 0;
                double noAnswerPercentage = (total > 0) ? (noAnswerCount * 100.0) / total : 0;

                System.out.printf("   Column %d: Yes = %.2f%%, No = %.2f%%, Don't know = %.2f%%%n", i + 3,
                        yesPercentage, noPercentage, noAnswerPercentage);
            }
        }

        // Close resources
        workbook.close();
        fileInputStream.close();

        // Combine results in a single map
        Map<String, Object[]> analysisResults = new HashMap<>();
        for (String type : typeCounts.keySet()) {
            analysisResults.put(type, new Object[] { typeCounts.get(type), typeLineCounts.get(type) });
        }
        return analysisResults;
    }

    public void writeResultsToNewSheet(String filePath, Map<String, Object[]> analysisResults) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        // Check if sheet "Resultados" exists
        Sheet resultSheet = workbook.getSheet("Resultados");
        if (resultSheet != null) {
            // If it exists, remove it
            int sheetIndex = workbook.getSheetIndex(resultSheet);
            workbook.removeSheetAt(sheetIndex);
        }

        // Create new sheet for results
        resultSheet = workbook.createSheet("Resultados");

        // Create cell styles
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle firstTwoColsStyle = workbook.createCellStyle();
        firstTwoColsStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        firstTwoColsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle yesStyle = workbook.createCellStyle();
        yesStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        yesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle noStyle = workbook.createCellStyle();
        noStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        noStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle doubtStyle = workbook.createCellStyle();
        doubtStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        doubtStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Header row
        Row headerRow = resultSheet.createRow(0);
        headerRow.createCell(0).setCellValue("Type");
        headerRow.createCell(1).setCellValue("Total Lines");
        for (int i = 3; i <= 13; i++) {
            int colIndex = (i - 3) * 3 + 2; // Index in sheet
            headerRow.createCell(colIndex).setCellValue("Column " + i + " - % Yes");
            headerRow.createCell(colIndex + 1).setCellValue("Column " + i + " - % No");
            headerRow.createCell(colIndex + 2).setCellValue("Column " + i + " - % Don't know");
        }

        // Apply style to header row
        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        // Write data
        int rowIndex = 1;
        for (Map.Entry<String, Object[]> entry : analysisResults.entrySet()) {
            String type = entry.getKey();
            int[] counts = (int[]) entry.getValue()[0];
            int totalLines = (int) entry.getValue()[1];

            Row row = resultSheet.createRow(rowIndex++);
            Cell typeCell = row.createCell(0);
            typeCell.setCellValue(type);
            typeCell.setCellStyle(firstTwoColsStyle);

            Cell totalLinesCell = row.createCell(1);
            totalLinesCell.setCellValue(totalLines);
            totalLinesCell.setCellStyle(firstTwoColsStyle);

            for (int i = 0; i < 13; i++) { // For columns 3 to 15
                int yesCount = counts[i * 3];
                int noCount = counts[i * 3 + 1];
                int noAnswerCount = counts[i * 3 + 2];
                int totalResponses = yesCount + noCount + noAnswerCount;

                // Calculate percentages
                double yesPercentage = totalResponses == 0 ? 0 : (yesCount * 100.0 / totalResponses);
                double noPercentage = totalResponses == 0 ? 0 : (noCount * 100.0 / totalResponses);
                double noAnswerPercentage = totalResponses == 0 ? 0 : (noAnswerCount * 100.0 / totalResponses);

                // Create and style cells
                Cell yesCell = row.createCell(2 + i * 3);
                yesCell.setCellValue(String.format("%.2f%%", yesPercentage));
                yesCell.setCellStyle(yesStyle);

                Cell noCell = row.createCell(3 + i * 3);
                noCell.setCellValue(String.format("%.2f%%", noPercentage));
                noCell.setCellStyle(noStyle);

                Cell doubtCell = row.createCell(4 + i * 3);
                doubtCell.setCellValue(String.format("%.2f%%", noAnswerPercentage));
                doubtCell.setCellStyle(doubtStyle);
            }
        }

        // Save file
        fileInputStream.close();
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }

    public void openAIServiceAllConsults(String fileName, List<String> llmResult) throws IOException {

        llmResult.add("ChatGPT");
        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" + fileName)))));
        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "2. base64" + fileName)))));
        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "3. anonymize" + fileName)))));

        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "4. obfus" + fileName)))));

        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "5. superobfus" + fileName)))));

        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "6. decryptedXOR" + fileName)))));

        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "7. decryptedVarOnly" + fileName)))));

        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "8. decryptedDeadCode" + fileName)))));

        llmResult.add(openAIService.getOpenAIResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "9. decryptedSuperOfusDeadCode" + fileName)))));
    }

    public void cohereServiceAllConsults(String fileName, List<String> llmResult) throws IOException {

        llmResult.add("Cohere");

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "2. base64" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "3. anonymize" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "4. obfus" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "5. superobfus" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "6. decryptedXOR" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "7. decryptedVarOnly" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "8. decryptedDeadCode" + fileName)))));

        llmResult.add(cohereService.getCohereResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "9. decryptedSuperOfusDeadCode" + fileName)))));

    }

    public void anthropicServiceAllConsults(String fileName, List<String> llmResult) throws IOException {

        llmResult.add("Anthopic");

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "2. base64" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "3. anonymize" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "4. obfus" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "5. superobfus" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "6. decryptedXOR" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "7. decryptedVarOnly" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "8. decryptedDeadCode" + fileName)))));

        llmResult.add(anthropicService.getAnthropicResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(Files.readAllBytes(
                        Paths.get(UPLOAD_DIR + "9. decryptedSuperOfusDeadCode" + fileName)))));

    }

    public void geminiServiceAllConsults(String fileName, List<String> llmResult) throws IOException {

        llmResult.add("Gemini");
        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "2. base64" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "3. anonymize" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "4. obfus" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "5. superobfus" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "6. decryptedXOR" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "7. decryptedVarOnly" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "8. decryptedDeadCode" + fileName)))));

        llmResult.add(geminiService.getGeminiResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(Files.readAllBytes(
                        Paths.get(UPLOAD_DIR + "9. decryptedSuperOfusDeadCode" + fileName)))));

    }

    public void deepSeekServiceAllConsults(String fileName, List<String> llmResult) throws IOException {

        llmResult.add("DeepSeek");
        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "2. base64" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "3. anonymize" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "4. obfus" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "5. superobfus" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "6. decryptedXOR" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "7. decryptedVarOnly" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "8. decryptedDeadCode" + fileName)))));

        llmResult.add(deepSeekService.getDeepSeekResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(Files.readAllBytes(
                        Paths.get(UPLOAD_DIR + "9. decryptedSuperOfusDeadCode" + fileName)))));

    }

    public void grokServiceAllConsults(String fileName, List<String> llmResult) throws IOException {

        llmResult.add("DeepSeek");
        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "1 flag" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "2. base64" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "3. anonymize" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "4. obfus" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "5. superobfus" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: "
                        + new String(Files.readAllBytes(Paths.get(UPLOAD_DIR + "6. decryptedXOR" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "7. decryptedVarOnly" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(
                        Files.readAllBytes(Paths.get(UPLOAD_DIR + "8. decryptedDeadCode" + fileName)))));

        llmResult.add(grokService.getGrokResponse(
                "¿contiene este código una IP? Dime solo si es sí, no o no sé: " + new String(Files.readAllBytes(
                        Paths.get(UPLOAD_DIR + "9. decryptedSuperOfusDeadCode" + fileName)))));

    }

}
