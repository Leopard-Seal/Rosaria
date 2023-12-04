package rosaria.main;

import rosaria.io.FileUtil;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final AtomicInteger successCnt = new AtomicInteger();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int start = 0;
        int end   = 35050;

        System.out.println("Max Threads : ");
        int nThreads = scanner.nextInt();
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        for (int i = start; i <= end; i++) {
            String url = "https://business-mail.jp/example/" + i;
            String clazz = "mailtxt"; // Assuming this is the class you want to scrape
            Rosaria rosaria = new Rosaria(url, clazz, i);
            executor.execute(rosaria);
        }

        executor.shutdown();

        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.println("Waiting for tasks to finish...");
            }
        } catch (InterruptedException e) {
            System.err.println("Tasks interrupted");
        }

        // ここで FileUtil のエグゼキュータをシャットダウン
        FileUtil.shutdownFileWriteExecutor();

        System.out.println("All tasks completed. Get Files : " + successCnt.get());
    }
}
