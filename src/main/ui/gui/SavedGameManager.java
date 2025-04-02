package ui.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SavedGameManager {

    private static final String SAVED_FILES_DIR = "./data/savedgames/";
    private static final String LAST_GAME_KEY = "last_saved_game";

    public static void saveLastGamePath(String fileName) {
        Preferences prefs = Preferences.userNodeForPackage(SavedGameManager.class);
        prefs.put(LAST_GAME_KEY, SAVED_FILES_DIR + fileName);
    }

    public static String getLastGamePath() {
        Preferences prefs = Preferences.userNodeForPackage(SavedGameManager.class);
        String lastPath = prefs.get(LAST_GAME_KEY, null);
        if (lastPath != null) {
            return lastPath;
        }
        return getMostRecentSave();
    }

    private static String getMostRecentSave() {
        File saveDir = new File(SAVED_FILES_DIR);
        if (!saveDir.exists() || !saveDir.isDirectory()) {
            return null;
        }
        File[] files = saveDir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File latestFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (files[i].lastModified() > latestFile.lastModified()) {
                latestFile = files[i];
            }
        }
        Path basePath = Paths.get(SAVED_FILES_DIR).toAbsolutePath().normalize();
        Path fullPath = Paths.get(latestFile.getAbsolutePath()).normalize();
        return SAVED_FILES_DIR + basePath.relativize(fullPath).toString();
    }

    public static String[] getAllSaveFiles() {
        File saveDir = new File(SAVED_FILES_DIR);
        if (!saveDir.exists() || !saveDir.isDirectory()) {
            return new String[0]; 
        }
        File[] files = saveDir.listFiles();
        if (files == null || files.length == 0) {
            return new String[0];
        }

        List<String> fileNames = new ArrayList<>();
        for (File file : files) {
            fileNames.add(file.getName());
        }

        return fileNames.toArray(new String[0]); 
    }

    public static String getSavedGamesDirectory() {
        return SAVED_FILES_DIR;
    }
}
