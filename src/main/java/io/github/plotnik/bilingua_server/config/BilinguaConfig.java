package io.github.plotnik.bilingua_server.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class BilinguaConfig {

    private Path bilinguaDir;

    private String leftName;
    private String rightName;

    @PostConstruct
    public void init() throws IOException {
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

    public String getLeftName() {
        return leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public Path getBilinguaDir() {
        return bilinguaDir;
    }
}