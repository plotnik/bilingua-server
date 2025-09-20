package io.github.plotnik.bilingua_server.service;

import io.github.plotnik.bilingua_server.dto.ParagraphPair;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
public class BilinguaService {

    private Path bilinguaDir;

    private String leftName;
    private String rightName;

    private int ptr;
    private List<String> leftPars;
    private List<String> rightPars;

    private Path ptrFile;

    @PostConstruct
    public void initialize() throws IOException {
        reload();
    }

    private void loadConfig() throws IOException {
        String userHome = System.getProperty("user.home");
        this.bilinguaDir = Paths.get(userHome, "Documents", "pi", "bilingua");

        Path propertiesFile = bilinguaDir.resolve("bi.properties");

        Properties properties = new Properties();
        try (FileReader reader = new FileReader(propertiesFile.toFile())) {
            properties.load(reader);
            this.leftName = properties.getProperty("left_name");
            this.rightName = properties.getProperty("right_name");
        }
    }

    // --- Public API Methods ---

    public synchronized void reload() throws IOException {
        loadConfig();
        this.ptrFile = bilinguaDir.resolve("ptr.txt");
        loadPtr();
        loadBooks();
    }

    public synchronized int getPtr() {
        return ptr;
    }

    public synchronized void setPtr(int newPtr) throws IOException {
        if (newPtr < 0) {
            throw new IllegalArgumentException("Pointer value cannot be negative.");
        }
        this.ptr = newPtr;
        Files.writeString(ptrFile, String.valueOf(this.ptr));
    }

    public synchronized ParagraphPair getPars(int shift) {
        int index = this.ptr + shift;
        String left = getParagraphSafe(leftPars, index);
        String right = getParagraphSafe(rightPars, index);
        return new ParagraphPair(left, right);
    }

    public synchronized void save(ParagraphPair pair) throws IOException {
        boolean leftChanged = updateParagraph(leftPars, ptr, pair.left());
        boolean rightChanged = updateParagraph(rightPars, ptr, pair.right());

        if (leftChanged) {
            writeBookFile(leftName, leftPars);
        }
        if (rightChanged) {
            writeBookFile(rightName, rightPars);
        }

        // If any file was changed, reload everything to ensure consistency
        if (leftChanged || rightChanged) {
            loadBooks();
        }
    }

    // --- Private Helper Methods ---

    private void loadPtr() {
        try {
            String content = Files.readString(ptrFile);
            this.ptr = Integer.parseInt(content.trim());
        } catch (IOException | NumberFormatException e) {
            // If file is missing, empty, or corrupt, default to 0
            this.ptr = 0;
        }
    }

    public void loadBooks() throws IOException {
        this.leftPars = readBookFile(leftName);
        this.rightPars = readBookFile(rightName);
    }

    private List<String> readBookFile(String fileName) throws IOException {
        // Combine config.getBilinguaDir() with fileName
        Path filePath = bilinguaDir.resolve(fileName);
        if (!Files.exists(filePath)) {
            return Collections.emptyList();
        }
        String content = Files.readString(filePath);
        // Split by one or more blank lines to get paragraphs
        return Arrays.asList(content.split("\\n\\s*\\n"));
    }

    private void writeBookFile(String fileName, List<String> paragraphs) throws IOException {
        Path filePath = bilinguaDir.resolve(fileName);
        // Join paragraphs with two newlines
        String content = String.join("\n\n", paragraphs);
        Files.writeString(filePath, content);
    }

    private String getParagraphSafe(List<String> list, int index) {
        return (index >= 0 && index < list.size()) ? list.get(index) : "";
    }

    private boolean updateParagraph(List<String> list, int index, String newText) {
        if (index >= 0 && index < list.size()) {
            String oldText = list.get(index);
            if (!oldText.equals(newText)) {
                list.set(index, newText);
                return true; // Text was changed
            }
        }
        return false; // No change
    }
}

