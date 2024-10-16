package com.elec5620.readverseserver.repositories;

import com.elec5620.readverseserver.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByPublisherId(Long publisherId);
}
