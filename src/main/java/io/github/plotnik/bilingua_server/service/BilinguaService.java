package io.github.plotnik.bilingua_server.service;

import io.github.plotnik.bilingua_server.config.BilinguaConfig;
import io.github.plotnik.bilingua_server.dto.ParagraphPair;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BilinguaService {

    @Autowired
    private BilinguaConfig config; 

    private int ptr;
    private List<String> leftPars;
    private List<String> rightPars;

    private Path ptrFile;

    @PostConstruct
    public void initialize() throws IOException {
        reload();
    }

    // --- Public API Methods ---

    public synchronized void reload() throws IOException {
        this.ptrFile = config.getBilinguaDir().resolve("ptr.txt");
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
            writeBookFile(config.getLeftName(), leftPars);
        }
        if (rightChanged) {
            writeBookFile(config.getRightName(), rightPars);
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
        this.leftPars = readBookFile(config.getLeftName());
        this.rightPars = readBookFile(config.getRightName());
    }

    private List<String> readBookFile(String fileName) throws IOException {
        // Combine config.getBilinguaDir() with fileName
        Path filePath = config.getBilinguaDir().resolve(fileName);
        if (!Files.exists(filePath)) {
            return Collections.emptyList();
        }
        String content = Files.readString(filePath);
        // Split by one or more blank lines to get paragraphs
        return Arrays.asList(content.split("\\n\\s*\\n"));
    }

    private void writeBookFile(String fileName, List<String> paragraphs) throws IOException {
        Path filePath = config.getBilinguaDir().resolve(fileName);
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

