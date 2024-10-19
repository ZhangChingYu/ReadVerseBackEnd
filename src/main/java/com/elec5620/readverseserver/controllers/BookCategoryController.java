package com.elec5620.readverseserver.controllers;


import com.elec5620.readverseserver.models.BookCategory;
import com.elec5620.readverseserver.services.BookCategoryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookCategoryController {

    private final Gson gson = new Gson();
    private final BookCategoryService bookCategoryService;

    @Autowired
    public BookCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    // 测试接口
    @GetMapping("/test-book-category")
    public String sayHello() {
        return "hello from BookCategoryController";
    }

    // 获取所有的 BookCategory
    @GetMapping("/bookcategories")
    public String findAllBookCategories() {
        List<BookCategory> bookCategories = bookCategoryService.findAllBookCategories();
        return gson.toJson(bookCategories);
    }
}

