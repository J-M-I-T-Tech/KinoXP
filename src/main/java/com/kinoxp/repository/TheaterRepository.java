package com.kinoxp.repository;

import com.kinoxp.model.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
