package com.imckify.bakis.repos;

import com.imckify.bakis.models.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoursesRepo extends JpaRepository<Courses, String> {
    Optional<List<Courses>> findByAuthorId(String id);
}
