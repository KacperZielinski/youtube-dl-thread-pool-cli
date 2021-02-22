package pl.kz.youtube;

import java.util.Scanner;

class YoutubeInputProperties {
    private static final int DEFAULT_POOL_SIZE = 3;
    private static final int FORK_JOIN_POOL_SIZE_FIX = 1;

    private int numberOfInstances = DEFAULT_POOL_SIZE;
    private String filePath;

    int getNumberOfInstances() {
        return numberOfInstances + FORK_JOIN_POOL_SIZE_FIX;
    }

    String getFilePath() {
        return filePath;
    }

    void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Provide number of threads: ");
            String threadPoolSize = scanner.next();

            try {
                this.numberOfInstances = Integer.parseInt(threadPoolSize);
            } catch (NumberFormatException ex) {
                System.out.println("Improper threadPool size, using default: " + DEFAULT_POOL_SIZE);
            }

            System.out.print("Provide path to file with Youtube urls: ");
            filePath = scanner.next();

            System.out.println();
        }
    }
}
