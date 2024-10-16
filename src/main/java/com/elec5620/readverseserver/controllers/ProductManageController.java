package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.ChapterIdDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.GetBookDto;
import com.elec5620.readverseserver.dto.PostBookDto;
import com.elec5620.readverseserver.services.PublisherManageBookService;
import com.elec5620.readverseserver.utils.FileHandler;
import com.google.gson.Gson;
import nl.siegmann.epublib.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductManageController {
    private final Gson gson = new Gson();
    private final PublisherManageBookService service;
    @Autowired
    public ProductManageController(PublisherManageBookService service) {
        this.service = service;
    }

    @PostMapping("upload_new_ebook")
    public String uploadEBook(@RequestParam("id") Long id, @RequestParam("author") String author, @RequestParam("price") Double price, @RequestParam("title") String title, @RequestParam("data")MultipartFile file){
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        } else {
            PostBookDto book = PostBookDto.builder()
                    .publisherId(id)
                    .author(author)
                    .price(price)
                    .title(title)
                    .file(file)
                    .build();
            FormalDto respond = service.uploadNewBook(book);
            return gson.toJson(respond);
        }
    }

    @GetMapping("book_chapters")
    public String showBookInfo(@RequestBody GetBookDto data){
        Book book = FileHandler.epubFileReader(data.getPublisherId(), data.getBookId());
        List<ChapterIdDto> chapters = FileHandler.getChapters(book);
        return gson.toJson(chapters);
    }

    @GetMapping("get_cover_image")
    public ResponseEntity<byte[]> testGetImage(@RequestBody GetBookDto data){
        HttpHeaders headers = new HttpHeaders();
        Book book = FileHandler.epubFileReader(data.getPublisherId(), data.getBookId());
        String imageType = FileHandler.coverImageType(book);
        byte[] imageBytes = FileHandler.coverImageBytes(book);
        headers.add("Content-Type", imageType);
        return new ResponseEntity<>(imageBytes, headers, 200);
    }
}
