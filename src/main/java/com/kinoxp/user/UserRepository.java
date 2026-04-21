package com.kinoxp.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByNameAndPassword(String name, String password);
    Optional<User> findFirstByNameIgnoreCaseOrderByUserIdAsc(String name);
    List<User> findAllByNameIgnoreCaseOrderByUserIdAsc(String name);
}
