package com.elec5620.readverseserver.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elec5620.readverseserver.models.BookCategory;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    
    // 根据 categoryId 查找所有的书籍分类
    List<BookCategory> findByCategoryId(Long categoryId);


}