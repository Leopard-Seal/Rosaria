package rosaria.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileUtil {
    public static final String DIR = "./output/data/";
    private static final ExecutorService fileWriteExecutor = Executors.newCachedThreadPool();

    public static void saveTextToFileAsync(String text, String filePath) {
        fileWriteExecutor.execute(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIR + filePath))) {
                writer.write(text);
            } catch (IOException e) {
                System.err.println("Error writing file: " + filePath);
            }
        });
    }

    public static void shutdownFileWriteExecutor() {
        fileWriteExecutor.shutdown();
        try {
            if (!fileWriteExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("File write executor did not terminate.");
            }
        } catch (InterruptedException e) {
            System.err.println("File write executor interrupted.");
        }
    }

    public static String sanitizeFilename(String filename) {
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}

