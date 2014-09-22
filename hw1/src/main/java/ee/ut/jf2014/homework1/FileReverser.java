package ee.ut.jf2014.homework1;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by joosep on 20.09.14.
 */

public class FileReverser {

    //Tested that with buffer size 1024 or 256 took more time to reverse.
    private final int BUFFER_SIZE = 512;

    public void reverse(String filename) throws Exception {
        File file = new File(filename);
        checkIfValid(file);
        printFileInfo(file);
        long startTime = System.nanoTime();
        doReversing(file);
        long timing = System.nanoTime() - startTime;
        printWorkingStats(timing, file.length());

    }


    private void doReversing(File file) throws Exception {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");) {
            byte[] beginningBuffer = new byte[BUFFER_SIZE];
            byte[] endingBuffer = new byte[BUFFER_SIZE];
            long endingPosition = file.length() - BUFFER_SIZE;
            long beginningPosition = 0;
            FileChannel fileChannel = randomAccessFile.getChannel();
            while (beginningPosition < endingPosition - BUFFER_SIZE) {
                randomAccessFile.read(beginningBuffer);
                randomAccessFile.seek(endingPosition);
                randomAccessFile.read(endingBuffer);
                ArrayUtils.reverse(beginningBuffer);
                ArrayUtils.reverse(endingBuffer);
                randomAccessFile.seek(endingPosition);
                randomAccessFile.write(beginningBuffer);
                randomAccessFile.seek(beginningPosition);
                randomAccessFile.write(endingBuffer);
                beginningPosition = fileChannel.position();
                endingPosition -= BUFFER_SIZE;

            }
            long stillToReverse = endingPosition - beginningPosition + BUFFER_SIZE;
            if (stillToReverse > 0) {
                byte[] finalBuffer = new byte[(int) stillToReverse];
                randomAccessFile.read(finalBuffer);
                ArrayUtils.reverse(finalBuffer);
                randomAccessFile.seek(beginningPosition);
                randomAccessFile.write(finalBuffer);
            }
        }
    }


    private void checkIfValid(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception(file.getAbsolutePath() + " does not exists.");
        }
        if (!file.canRead()) {
            throw new Exception(file.getAbsolutePath() + " can not be read.");
        }
        if (!file.canWrite()) {
            throw new Exception(file.getAbsolutePath() + " can not be written.");
        }
    }

    private void printFileInfo(File file) {
        System.out.println("filename: " + file.getName() + ".");
        System.out.println("file size: " + getFormattedFileSize(file.length()) + ".");
    }

    private String getFormattedFileSize(long fileSizeInBytes) {
        StringBuilder formattedFileSize = new StringBuilder();
        formattedFileSize.append(fileSizeInBytes + "B");
        if (fileSizeInBytes >= 1024) {
            formattedFileSize.append(" (~");
            long fileSizeInKB = fileSizeInBytes / 1024;
            if (fileSizeInKB >= 1024) {
                long fileSizeInMB = fileSizeInKB / 1024;
                formattedFileSize.append(fileSizeInMB + "MB)");
            } else {
                formattedFileSize.append(fileSizeInKB + "kB)");
            }
        }
        return formattedFileSize.toString();
    }

    private void printWorkingStats(long timing, long fileSize) {
        String formattedTiming = getFormattedTiming(timing);
        System.out.print("reversing took time: " + formattedTiming);
        System.out.println(", " + getKbPerSec(timing, fileSize) + "Kb/s");
    }

    private String getKbPerSec(long timingInNano, long fileSize) {
        double seconds = timingInNano / 1000000000.0;
        double sizeInKb = fileSize / 1024;
        double kbPerSec = sizeInKb / seconds;
        return String.format("%.2f", kbPerSec);
    }

    private String getFormattedTiming(long timing) {

        double millisecond = timing / 1000000.0;
        String formattedTime = millisecond % 1000 + "ms";
        double second = Math.floor(millisecond / 1000);
        if (second > 0) {
            formattedTime = (int) (second % 60) + "s " + formattedTime;
            double minute = Math.floor(second / 60);
            if (minute > 0) {
                formattedTime = (int) minute + "m " + formattedTime;
            }
        }
        return formattedTime;
    }
}
