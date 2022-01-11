package com.imckify.bakis.repos;

import com.imckify.bakis.models.Authors;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorsRepo extends JpaRepository<Authors, String> {
}
