package com.imckify.bakis.controllers;

import com.imckify.bakis.exceptions.ResourceNotFoundException;
import com.imckify.bakis.models.Authors;
import com.imckify.bakis.repos.AuthorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Authors")
public class AuthorsControl {

    @Autowired
    private AuthorsRepo AuthorsRepo;

    @GetMapping("")
    public ResponseEntity<List<Authors>> getAuthors() {
        return ResponseEntity.ok(
                this.AuthorsRepo.findAll()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Authors> getAuthors(@PathVariable(value = "id") String id){
        Authors Author = this.AuthorsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Author not found")
        );

        return  ResponseEntity.ok().body(Author);
    }

    @PostMapping("/create")
    public Authors createAuthor(@RequestBody Authors Author){
        return this.AuthorsRepo.save(Author);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable(value = "id") String id){
        Authors Author =this.AuthorsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Author not found "+id)
        );

        this.AuthorsRepo.delete(Author);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Authors updateAuthor(@RequestBody Authors newAuthor, @PathVariable(value = "id") String id){
        return this.AuthorsRepo.findById(id)
                .map(Author -> {
                    Author.setFirstName(newAuthor.getFirstName());
                    Author.setLastName(newAuthor.getLastName());

                    return this.AuthorsRepo.save(Author);
                })
                .orElseGet(()->{
                    newAuthor.setID(id);
                    return this.AuthorsRepo.save(newAuthor);
                });
    }
}

