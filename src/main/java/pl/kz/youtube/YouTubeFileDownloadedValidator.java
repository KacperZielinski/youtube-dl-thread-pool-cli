package pl.kz.youtube;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class YouTubeFileDownloadedValidator {

    public static void main(String[] args) throws IOException {
        final YoutubeInputProperties input = new YoutubeInputProperties(args).getProperties();
        final Set<String> youtubeVideos = YoutubeFileTransformer.createYoutubeVideoUrlsSetFromFile(input.getFilePath());

        long start = System.currentTimeMillis();
        final List<Path> videoPaths = Files.walk(Paths.get("P:\\youtube-dl-download\\"))
                .filter(Files::isRegularFile)
                .filter(path -> !path.endsWith(".part"))
                .collect(Collectors.toList());

        long end = System.currentTimeMillis();
        System.out.println("File walks takes: " + (end - start) + " ms");

        videoPaths.forEach(System.out::println);
        System.out.println(videoPaths.size());

        youtubeVideos.forEach(videoUrl -> {
            YoutubeProcessHandler processHandler = new YoutubeProcessHandler();
            String videoName = processHandler.getVideoName(videoUrl);
            videoName = videoName.replace(":", " -")
                                 .replace("/", "_")
                                 .replace("\"", "'")
                                 .replace("?", "");

            boolean isVideoDownloaded = findDownloadedVideo(videoPaths, videoName);

            if(!isVideoDownloaded) {
                System.out.println(videoUrl);
                processHandler.downloadVideoFromUrl(videoUrl);
            }
        });
    }

    private static boolean findDownloadedVideo(List<Path> videoPaths, String videoName) {
        return videoPaths.stream().anyMatch(path -> path.toString().contains(videoName));
    }
}
