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
            return targetPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Book epubFileReader(Long publisherId, Long bookId){
        String rootPath = System.getProperty("user.dir");
        File rootFile = new File(rootPath);
        String parentDir = rootFile.getParent();
        String filepath = parentDir + "/ebooks/" + publisherId + "/"+bookId + ".epub";
        InputStream input = null;
        try{
            input = new FileInputStream(filepath);
            EpubReader reader = new EpubReader();
            Book ebook = reader.readEpub(input);
            return ebook;
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

    public static byte[] coverImageBytes(Book book){
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

    public static String coverImageType(Book book){
        Resource coverImage = book.getCoverImage();
        return coverImage.getMediaType().getName();
    }

    public static List<ChapterIdDto> getChapters(Book book){
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

    public static void testEpubReader(Long publisherId, Long bookId){
        String rootPath = System.getProperty("user.dir");
        File rootFile = new File(rootPath);
        String parentDir = rootFile.getParent();
        String filepath = parentDir + "/ebooks/" + publisherId + "/"+bookId + ".epub";
        InputStream input = null;
        try{
            input = new FileInputStream(filepath);
            EpubReader reader = new EpubReader();
            Book ebook = reader.readEpub(input);
            //获取到书本的头部信息
            Metadata metadata = ebook.getMetadata();
            System.out.println("FirstTitle为："+metadata.getFirstTitle());
            //获取到书本的全部资源
            Resources resources = ebook.getResources();
            System.out.println("所有资源数量为："+resources.size());
            //获取所有的资源数据
            Collection<String> allHrefs = resources.getAllHrefs();
            for (String href : allHrefs) {
                Resource resource = resources.getByHref(href);
                //data就是资源的内容数据，可能是css,html,图片等等
                byte[] data = resource.getData();
                // 获取到内容的类型  css,html,还是图片
                MediaType mediaType = resource.getMediaType();
            }
            //获取到书本的内容资源
            List<Resource> contents = ebook.getContents();
            System.out.println("内容资源数量为："+contents.size());
            //获取到书本的spine资源 线性排序
            Spine spine = ebook.getSpine();
            System.out.println("spine资源数量为："+spine.size());
            //通过spine获取所有的数据
            List<SpineReference> spineReferences = spine.getSpineReferences();
            for (SpineReference spineReference : spineReferences) {
                Resource resource = spineReference.getResource();
                //data就是资源的内容数据，可能是css,html,图片等等
                byte[] data = resource.getData();
                // 获取到内容的类型  css,html,还是图片
                MediaType mediaType = resource.getMediaType();
                //System.out.println(mediaType.getName());
            }
            //获取到书本的目录资源
            TableOfContents tableOfContents = ebook.getTableOfContents();
            System.out.println("目录资源数量为："+tableOfContents.size());
            //获取到目录对应的资源数据
            List<TOCReference> tocReferences = tableOfContents.getTocReferences();
            for (TOCReference tocReference : tocReferences) {
                Resource resource = tocReference.getResource();
                //data就是资源的内容数据，可能是css,html,图片等等
                byte[] data = resource.getData();
                // 获取到内容的类型  css,html,还是图片
                MediaType mediaType = resource.getMediaType();
                //System.out.println(mediaType.getName());
                if(tocReference.getChildren().size()>0){
                    //获取子目录的内容
                    System.out.println("Children Exists.");
                }
            }
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
    }
}
