package pl.kz.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class YoutubeFileTransformer {
    private static final int DEFAULT_YOUTUBE_URL_LINK_LENGTH = 43;

    static Set<String> createYoutubeVideoUrlsSetFromFile(String filePath) {
        Set<String> videoUrls = ConcurrentHashMap.newKeySet();
        Path path = Paths.get(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)){
            int currentLine = 0;
            String line;

            while((line = reader.readLine()) != null) {
                currentLine++;

                if(!line.isEmpty() && hasStandardYoutubeLinkLength(line)) {
                    videoUrls.add(line);
                } else {
                    System.out.println("Error on line: " + currentLine + " " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: Cannot parse youtube url file: " + path);
            return ConcurrentHashMap.newKeySet();
        }
        return videoUrls;
    }

    private static boolean hasStandardYoutubeLinkLength(String line) {
        return line.length() == DEFAULT_YOUTUBE_URL_LINK_LENGTH;
    }
}
