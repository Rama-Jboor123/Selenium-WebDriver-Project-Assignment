package com.ramajboor.selenium.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
    }

    public static String capture(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalStateException("The active WebDriver does not support screenshots.");
        }

        String safeName = name.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path outputDir = Path.of("target", "screenshots");
        Path destination;
        try {
            Files.createDirectories(outputDir);
            destination = outputDir.resolve(safeName + "-" + LocalDateTime.now().format(TIMESTAMP) + ".png");
            Files.copy(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath(),
                    destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save screenshot", e);
        }
        return destination.toString();
    }
}
