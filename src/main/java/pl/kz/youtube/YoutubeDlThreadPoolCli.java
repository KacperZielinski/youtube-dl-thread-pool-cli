package pl.kz.youtube;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

public class YoutubeDlThreadPoolCli {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final YoutubeInputProperties input = new YoutubeInputProperties(args).getProperties();
        final Set<String> youtubeVideos = YoutubeFileTransformer.createYoutubeVideoUrlsSetFromFile(input.getFilePath());

        ForkJoinPool customThreadPool = new ForkJoinPool(input.getNumberOfInstances());

        AtomicInteger taskLeft = new AtomicInteger(youtubeVideos.size());
        customThreadPool.submit(() -> youtubeVideos.parallelStream()
            .forEach(videoUrl -> {
                YoutubeProcessHandler processHandler = new YoutubeProcessHandler();
                processHandler.downloadVideoFromUrl(videoUrl);
                taskLeft.getAndDecrement();
                System.out.println("Overall status: " + taskLeft + "/" + youtubeVideos.size());
            })
        ).get();
    }
}
