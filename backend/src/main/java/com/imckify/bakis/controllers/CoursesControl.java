package com.imckify.bakis.controllers;

import com.imckify.bakis.exceptions.ResourceNotFoundException;
import com.imckify.bakis.models.Courses;
import com.imckify.bakis.repos.CoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Courses")
public class CoursesControl {

    @Autowired
    private CoursesRepo CoursesRepo;

    @GetMapping("")
    public ResponseEntity<List<Courses>> getCourses() {
        return ResponseEntity.ok(
                this.CoursesRepo.findAll()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Courses> getCourses(@PathVariable(value = "id") String id){
        Courses Course = this.CoursesRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Course not found")
        );

        return  ResponseEntity.ok().body(Course);
    }

    @PostMapping("/create")
    public Courses createCourse(@RequestBody Courses Course){
        return this.CoursesRepo.save(Course);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(value = "id") String id){
        Courses Course =this.CoursesRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Course not found "+id)
        );

        this.CoursesRepo.delete(Course);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Courses updateCourse(@RequestBody Courses newCourse, @PathVariable(value = "id") String id){
        return this.CoursesRepo.findById(id)
                .map(Course -> {
                    Course.setTitle(newCourse.getTitle());
                    Course.setLength(newCourse.getLength());
                    Course.setCategory(newCourse.getCategory());
                    Course.setWatchHref(newCourse.getWatchHref());
                    Course.setAuthorId(newCourse.getAuthorId());

                    return this.CoursesRepo.save(Course);
                })
                .orElseGet(()->{
                    newCourse.setID(id);
                    return this.CoursesRepo.save(newCourse);
                });
    }
}

