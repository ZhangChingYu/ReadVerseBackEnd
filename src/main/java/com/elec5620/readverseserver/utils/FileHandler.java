package com.elec5620.readverseserver.utils;


import com.elec5620.readverseserver.dto.ChapterIdDto;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
            return "/" + publisherId + "/" + newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) { 
                Files.delete(path);  
                return true;
            } else {
                System.out.println("File not found: " + filePath);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } 
    }
  
    private static Book epubFileReader(Long publisherId, Long bookId){
        String rootPath = System.getProperty("user.dir");
        File rootFile = new File(rootPath);
        String parentDir = rootFile.getParent();
        String filepath = parentDir + "/ebooks/" + publisherId + "/"+bookId + ".epub";
        InputStream input = null;
        try{
            input = new FileInputStream(filepath);
            EpubReader reader = new EpubReader();
            return reader.readEpub(input);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (input != null){
                    input.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] coverImageBytes(Long publisherId, Long bookId){
        Book book = epubFileReader(publisherId, bookId);
        Resource coverImage = book.getCoverImage();
        if(coverImage != null){
            byte[] cover = new byte[0];
            try {
                cover = coverImage.getData();
                return cover;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String coverImageType(Long publisherId, Long bookId){
        Book book = epubFileReader(publisherId, bookId);
        Resource coverImage = book.getCoverImage();
        return coverImage.getMediaType().getName();
    }

    public static String coverImageBase64(Long publisherId, Long bookId){
        Book book = epubFileReader(publisherId, bookId);
        Resource coverImage = book.getCoverImage();
        if(coverImage != null){
            byte[] cover = new byte[0];
            String type = null;
            try {
                cover = coverImage.getData();
                type = coverImage.getMediaType().getName();
                String base64 = "data:"+type+";base64,"+Base64.getEncoder().encodeToString(cover);
                return base64;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static List<ChapterIdDto> getChapters(Long publisherId, Long bookId){
        Book book = epubFileReader(publisherId, bookId);
        List<ChapterIdDto> chapters = new ArrayList<>();
        if(book != null){
            List<TOCReference> references = book.getTableOfContents().getTocReferences();
            for(int i = 0 ; i < references.size() ; i++){
                ChapterIdDto chapter = ChapterIdDto.builder()
                        .id(i)
                        .chapter(references.get(i).getTitle())
                        .build();
                chapters.add(chapter);
            }
        }
        return chapters;
    }

    public static HashMap<String, String> getBookUploadInfo(Long publisherId, Long bookId){
        Book book = epubFileReader(publisherId, bookId);
        if(book == null){
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("authors", book.getMetadata().getAuthors().toString());
        map.put("title", book.getTitle());
        return map;
    }

    public static String getBookSummary(Long publisherId, Long bookId){
        Book book = epubFileReader(publisherId, bookId);
        if(book == null){
            return null;
        }
        return book.getMetadata().getDescriptions().toString();
    }
}
