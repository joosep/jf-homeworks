package ee.ut.jf2014;

import ee.ut.jf2014.homework1.FileReverser;

import java.util.Arrays;

/*Write a Java program which reverses binary file content.
    1. Take the filename as an input argument (main
    method args[0]).
    2. Use a single java.io.RandomAccessFile instance
    for reading and writing the file.
    3. No temporary files may be used.
    4. Support large files (200MB) while using little
    memory (-Xmx32M).
    5. Buffer data for better performance.
    6. On start print out the filename and size.
    7. On finish print out duration and speed (kB/s) of
    transforming the file.
    8. Close the RandomAccessFile properly. */
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Programme must have file location as input argument.");
            System.out.println("Current input arguments: " + Arrays.toString(args) + ".");
            System.exit(1);
        }
        String filename = args[0];
        FileReverser fileReverser = new FileReverser();
        try {
            fileReverser.reverse(filename);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
