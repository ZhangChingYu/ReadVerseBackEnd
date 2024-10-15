package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PostBookDto;
import com.elec5620.readverseserver.services.PublisherManageBookService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

}
