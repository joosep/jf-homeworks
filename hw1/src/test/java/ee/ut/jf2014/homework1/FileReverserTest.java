package ee.ut.jf2014.homework1;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * Created by joosep on 20.09.14.
 */
public class FileReverserTest {

    @Test
    public void testExistingFile() throws IOException {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        FileReverser fileReverser = new FileReverser();
        boolean failed = false;
        try {
            fileReverser.reverse(tmpFile.getAbsolutePath());
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertFalse(failed);
    }

    @Test
    public void testMissingFile() throws IOException {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.delete();
        FileReverser fileReverser = new FileReverser();
        String exception = "Did not get exception.";
        String expected = "does not exists.";
        try {
            fileReverser.reverse(tmpFile.getAbsolutePath());
        } catch (Exception e) {
            String message = e.getMessage();
            exception = message.substring(message.length() - expected.length());
        }
        Assert.assertEquals(expected, exception);
    }

    @Test
    public void testNotReadableFile() throws IOException {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        tmpFile.setReadable(false);
        FileReverser fileReverser = new FileReverser();
        String exception = "Did not get exception.";
        String expected = "can not be read.";
        try {
            fileReverser.reverse(tmpFile.getAbsolutePath());
        } catch (Exception e) {
            String message = e.getMessage();
            exception = message.substring(message.length() - expected.length());
        }
        Assert.assertEquals(expected, exception);
    }

    @Test
    public void testNotWritableFile() throws IOException {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        tmpFile.setWritable(false);
        FileReverser fileReverser = new FileReverser();
        String exception = "Did not get exception.";
        String expected = "can not be written.";
        try {
            fileReverser.reverse(tmpFile.getAbsolutePath());
        } catch (Exception e) {
            String message = e.getMessage();
            exception = message.substring(message.length() - expected.length());
        }
        Assert.assertEquals(expected, exception);
    }


    @Test
    public void testBigFileReversing() throws Exception {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        String firstLine = null;

        StringBuilder bigText = new StringBuilder();
        FileWriter writer = new FileWriter(tmpFile);
        for (int i = 0; i < 30000000; i++) {
            bigText.append(i);
            if (i % 1000 == 0) {
                if (firstLine == null) {
                    firstLine = bigText.toString();
                }
                writer.write(bigText.toString() + System.lineSeparator());
                bigText = new StringBuilder();
            }
        }
        writer.write(bigText.toString());
        writer.close();
        FileReverser fileReverser = new FileReverser();
        fileReverser.reverse(tmpFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        String line;
        String lastLineResult = null;
        String firstLineResult = null;
        while ((line = reader.readLine()) != null) {
            if (firstLineResult == null) {
                firstLineResult = line;
            }
            lastLineResult = line;
        }
        reader.close();
        String firstLineReversed = new StringBuilder(firstLine).reverse().toString();
        String lastLineReversed = bigText.reverse().toString();
        Assert.assertEquals(firstLineReversed, lastLineResult);
        Assert.assertEquals(lastLineReversed, firstLineResult);
    }

    @Test
    public void testSimpleFileReversing() throws Exception {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        StringBuilder bigText = new StringBuilder();
        FileWriter writer = new FileWriter(tmpFile);
        for (int i = 0; i < 1000; i++) {
            bigText.append(i);
        }
        writer.write(bigText.toString());
        writer.close();
        FileReverser fileReverser = new FileReverser();
        fileReverser.reverse(tmpFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        String reversed = reader.readLine();
        reader.close();
        String fileInReverse = bigText.reverse().toString();
        Assert.assertEquals(fileInReverse, reversed);
    }

    @Test
    public void testSmallerFileThanBuffer() throws Exception {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        StringBuilder smallText = new StringBuilder();
        smallText.append("Aias sadas saia.");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(smallText.toString());
        writer.close();
        FileReverser fileReverser = new FileReverser();
        fileReverser.reverse(tmpFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        String reversed = reader.readLine();
        reader.close();
        String fileInReverse = smallText.reverse().toString();
        Assert.assertEquals(fileInReverse, reversed);
    }

    @Test
    public void testEmptyFile() throws Exception {
        File tmpFile = File.createTempFile("fileReverse", ".test");
        tmpFile.deleteOnExit();
        FileReverser fileReverser = new FileReverser();
        fileReverser.reverse(tmpFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        Assert.assertTrue(reader.readLine() == null);
    }
}
