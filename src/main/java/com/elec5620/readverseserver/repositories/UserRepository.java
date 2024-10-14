package com.elec5620.readverseserver.repositories;

import com.elec5620.readverseserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole(String role);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);
}
