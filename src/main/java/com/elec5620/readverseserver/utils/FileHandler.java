package com.elec5620.readverseserver.utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    // This function will automatically create a new empty file called "ebook" in your computer right next to ReadVerseServer file.
    public static void initEBookDir(){
        String rootPath = System.getProperty("user.dir");
        File rootFile = new File(rootPath);
        String parentDir = rootFile.getParent();

        Path directoryPath = Paths.get(parentDir+"/ebooks");
        try {
            // if no such directory exist, create a new one
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("E-Book Directory created successfully!");
            } else {
                System.out.println("E-Book Directory already exists.");
            }
        } catch (IOException e) {
            System.out.println("Failed to create [E-Book] directory! " + e.getMessage());
        }
    }

    public static void createPublisherDir(Long publisherId){
        String rootPath = System.getProperty("user.dir");
        Path directoryPath = Paths.get(rootPath+"/ebooks/"+publisherId);
        try {
            // if no such directory exist, create a new one
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("Publisher Directory created successfully!");
            } else {
                System.out.println("Publisher Directory already exists.");
            }
        } catch (IOException e) {
            System.out.println("Failed to create [Publisher] directory! " + e.getMessage());
        }
    }
}
