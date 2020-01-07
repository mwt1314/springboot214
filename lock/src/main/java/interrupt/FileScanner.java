package interrupt;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class FileScanner {

    //在扫描文件的过程中，对于中断的检测这里采用的策略是，如果碰到的是文件就不检测中断，是目录才检测中断，
    // 因为文件可能是非常多的，每次遇到文件都检测一次会降低程序执行效率。此外，在fileIteratorThread线程中，
    // 仅是捕获了InterruptedException，没有重设中断状态也没有继续抛出异常
    private static void listFile(File file) throws InterruptedException {
        if (file != null) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else {
                System.out.println(Thread.currentThread().isInterrupted());
                if (Thread.interrupted()) {
                    throw new InterruptedException("文件扫描任务被中断");
                }
                File[] files = file.listFiles();
                if (files != null) {
                    for (File listFile : files) {
                        listFile(listFile);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread fileIteratorThread = new Thread(() -> {
            try {
                listFile(new File("C:\\"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        fileIteratorThread.start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (fileIteratorThread.isAlive()) {
                fileIteratorThread.interrupt();
                return;
            }
        }).start();
    }
}