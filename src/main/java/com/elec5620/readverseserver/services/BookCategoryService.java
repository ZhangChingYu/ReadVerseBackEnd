package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.models.BookCategory;
import com.elec5620.readverseserver.repositories.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;

    @Autowired
    public BookCategoryService(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }

    // 查找所有 BookCategory
    public List<BookCategory> findAllBookCategories() {
        return bookCategoryRepository.findAll();
    }

}