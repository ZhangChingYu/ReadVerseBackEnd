package com.elec5620.readverseserver.utils;


import org.springframework.web.multipart.MultipartFile;

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

    public static String createPublisherDir(Long publisherId){
        String rootPath = System.getProperty("user.dir");
        File rootFile = new File(rootPath);
        String parentDir = rootFile.getParent();
        Path directoryPath = Paths.get(parentDir+"/ebooks/"+publisherId);

        try {
            // if no such directory exist, create a new one
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("Publisher Directory created successfully!");
            } else {
                System.out.println("Publisher Directory already exists.");
            }
            return parentDir+"/ebooks";
        } catch (IOException e) {
            System.out.println("Failed to create [Publisher] directory! " + e.getMessage());
        }
        return null;
    }

    public static String uploadNewBook(Long publisherId, Long bookId, MultipartFile book){
        // create the publisher directory if not exists.
        String parentDir = createPublisherDir(publisherId);
        // rename book file name as it's id
        String newFileName = bookId + ".epub";
        try {
            // target file path is: .../ebooks/publisherId/bookId.epub
            String targetPath = parentDir + "/" + publisherId + "/" + newFileName;
            Path filePath = Paths.get(targetPath);
            // write the file to the target file path, if same file name already exist, this will override the old file.
            Files.createDirectories(filePath.getParent());  // 确保目录存在
            Files.write(filePath, book.getBytes());
            return targetPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean checkFileAlreadyExists(Long publisherId, Long bookId, MultipartFile book){
        String rootPath = System.getProperty("user.dir");
        String filename = bookId + ".epub";
        String targetPath = rootPath + "/ebook/" + publisherId + "/" + filename;
        Path filePath = Paths.get(targetPath);
        return Files.exists(filePath);
    }

    public static Boolean dirExist(Long publisherId){
        String rootPath = System.getProperty("user.dir");
        Path directoryPath = Paths.get(rootPath+"/ebooks/"+publisherId);
        return !Files.exists(directoryPath);
    }
}
