package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<UserDetails> findByEmail(String email);

    boolean existsByEmail(String username);
}
