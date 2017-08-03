package com.tps;

import java.io.IOException;
import java.nio.file.*;
import java.text.NumberFormat;

public class Main {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        Path root;
        if (OS.indexOf("win") >= 0) {
            root= Paths.get("C:\\");
        } else {
            root= Paths.get("/");
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        /*for (Path root : FileSystems.getDefault().getRootDirectories()) {
            if (root.startsWith(comparePath) ) {
                System.out.print(root + " --> "); */
        try {
            FileStore store = Files.getFileStore(root);
            System.out.println("available=" + nf.format(store.getUsableSpace()) + ", total=" + nf.format(store.getTotalSpace()));
        } catch (IOException e) {
            System.out.println("error querying space: " + e.toString());
        }
    }
}
