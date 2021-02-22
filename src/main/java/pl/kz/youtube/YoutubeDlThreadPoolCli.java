package pl.kz.youtube;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class YoutubeDlThreadPoolCli {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final YoutubeInputProperties input = new YoutubeInputProperties(args).getProperties();
        final Set<String> youtubeVideos = YoutubeFileTransformer.createYoutubeVideoUrlsSetFromFile(input.getFilePath());

        ForkJoinPool customThreadPool = new ForkJoinPool(input.getNumberOfInstances());
        customThreadPool.submit(() -> youtubeVideos.parallelStream()
            .forEach(videoUrl -> {
                YoutubeProcessHandler processHandler = new YoutubeProcessHandler();
                processHandler.downloadVideoFromUrl(videoUrl);
            })
        ).get();
    }
}
