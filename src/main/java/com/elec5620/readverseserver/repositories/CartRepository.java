package com.elec5620.readverseserver.repositories;

import com.elec5620.readverseserver.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findCartsByCustomerId(Long customerId);
    Optional<Cart> findCartById(Long id);
}
