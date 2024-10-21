package com.elec5620.readverseserver.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.models.Category;
import com.elec5620.readverseserver.repositories.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

	public FormalDto getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
        FormalDto result = FormalDto.builder().status(200).build();
        if(categories.isEmpty()) {
            result.setMessage("No categories in database");
            result.setData(null);
        } else {
            result.setMessage("Load categoryies successfully");
            result.setData(categories);
        }
        return result;
	}


}