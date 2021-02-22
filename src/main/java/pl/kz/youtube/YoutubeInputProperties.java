package pl.kz.youtube;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class YoutubeInputProperties {
    private static final int DEFAULT_POOL_SIZE = 3;
    private final String[] consoleArgs;

    private int numberOfInstances = DEFAULT_POOL_SIZE;
    private String filePath;

    public YoutubeInputProperties(String[] consoleArgs) {
        this.consoleArgs = consoleArgs;
    }

    int getNumberOfInstances() {
        return numberOfInstances;
    }

    String getFilePath() {
        return filePath;
    }

    YoutubeInputProperties getProperties() {
        if (consoleArgs.length == 2) {
            numberOfInstances = parseNumberOfInstances(consoleArgs[0]);
            filePath = consoleArgs[1];
        } else {
            waitForInput();
        }

        if(!Files.exists(Paths.get(filePath))) {
            throw new IllegalArgumentException("Filepath is incorrect!");
        }

        return this;
    }

    private int parseNumberOfInstances(String consoleArg) {
        try {
            return Integer.parseInt(consoleArg);
        } catch (NumberFormatException ex) {
            System.out.println("Improper number of instances, using default: " + DEFAULT_POOL_SIZE);
            return DEFAULT_POOL_SIZE;
        }
    }

    private void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Provide number of threads: ");
            numberOfInstances = parseNumberOfInstances(scanner.next());

            System.out.print("Provide path to file with Youtube urls: ");
            filePath = scanner.next();

            System.out.println();
        }
    }
}
