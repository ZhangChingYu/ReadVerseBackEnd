package com.elec5620.readverseserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elec5620.readverseserver.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByPublisherId(Long publisherId);
    @Query("SELECT b FROM Book b WHERE b.publisherId = :publisherId AND " +
        "(b.title ILIKE CONCAT('%', :keyword, '%') OR " +
        "b.author ILIKE CONCAT('%', :keyword, '%')")
    List<Book> findBooksByPublisherIdAndKeyword(Long publisherId, String keyword);
}
