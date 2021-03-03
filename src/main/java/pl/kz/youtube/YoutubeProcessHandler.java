package pl.kz.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class YoutubeProcessHandler {
    private static final String YOUTUBE_DL_APP_NAME = "youtube-dl";
    private static final String VIDEO_QUALITY_FIX = "-f 22";
    private static final String PROGRESS_INFO_PREFIX = "[download] ";
    private static final String VIDEO_NAME_OUTPUT_LINE_HOTSPOT = "Destination: ";
    private static final String PERCENT = "%";
    private static final int PROGRESS_BAR_BEGIN_INDEX = 11;
    private static final int PROGRESS_BAR_MAX_END_INDEX = 16;
    private static final int MINIMUM_PROGRESS_LINE_LENGTH = 17;
    private static final double MAXIMUM_PROGRESS = 100.0;
    private static final int DISPLAY_THRESHOLD = 0;

    private double downloadProgress;
    private double lastDisplayedProgress;

    void downloadVideoFromUrl(String videoUrl) {
        String videoName = "still unknown..";
        String command = YOUTUBE_DL_APP_NAME + " " + VIDEO_QUALITY_FIX + " " + videoUrl;
        printCurrentProgress(videoUrl, videoName);

        try {
            Process process = Runtime.getRuntime().exec(command);

            try(BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String processOutputLine;

                while ((processOutputLine = processOutput.readLine()) != null) {
                    if(processOutputLine.contains(VIDEO_NAME_OUTPUT_LINE_HOTSPOT)) {
                        videoName = processOutputLine.split(VIDEO_NAME_OUTPUT_LINE_HOTSPOT)[1];
                    }

                    if(lineContainsProgressInfo(processOutputLine)) {
                        downloadProgress = updateProgress(processOutputLine);

                        if(downloadProgress > lastDisplayedProgress + DISPLAY_THRESHOLD) {
                            lastDisplayedProgress = downloadProgress;
                            printCurrentProgress(videoUrl, videoName);
                        }
                    }
                }
            }
            System.out.println(videoName + " has been downloaded!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getVideoName(String videoUrl) {
        String videoName = ".part";
        String checkVideoNameCommand = YOUTUBE_DL_APP_NAME + " --get-title " + videoUrl;

        try {
            Process process = Runtime.getRuntime().exec(checkVideoNameCommand);

            try(BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream(), "CP1250"))) {
                String processOutputLine;

                while ((processOutputLine = processOutput.readLine()) != null) {
                    videoName = processOutputLine;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return videoName;
    }

    private void printCurrentProgress(String videoUrl, String videoName) {
        System.out.println(LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + " | "
                + Thread.currentThread().getName() + " | "
                + videoUrl + " | "
                + videoName + " -> "
                + downloadProgress);
    }

    private boolean lineContainsProgressInfo(String s) {
        return s.startsWith(PROGRESS_INFO_PREFIX) && s.contains(PERCENT) && s.length() >= MINIMUM_PROGRESS_LINE_LENGTH;
    }

    private Double updateProgress(String processOutputLine) {
        String progressValueLine = processOutputLine.substring(PROGRESS_BAR_BEGIN_INDEX, PROGRESS_BAR_MAX_END_INDEX);
        if(progressValueLine.contains(PERCENT)) {
            return MAXIMUM_PROGRESS;
        }

        try {
            return Double.parseDouble(progressValueLine);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid progress line during parsing to double: " + progressValueLine);
        }

        return downloadProgress;
    }
}
