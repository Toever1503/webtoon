package webtoon.utils;

import java.io.File;

public class FileUtils {
    public static void deleteFolderContent(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolderContent(file.getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
