package com.kinoxp.repository;

import com.kinoxp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNameAndPassword(String name, String password);

}