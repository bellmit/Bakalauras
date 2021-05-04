package com.imckify.bakis.controllers;

import com.imckify.bakis.exceptions.ResourceNotFoundException;
import com.imckify.bakis.models.Modifications;
import com.imckify.bakis.repos.ModificationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Modifications")
public class ModificationsControl {

    @Autowired
    private ModificationsRepo ModificationsRepo;

    @GetMapping("")
    public ResponseEntity<List<Modifications>> getModifications() {
        return ResponseEntity.ok(
                this.ModificationsRepo.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modifications> getModifications(@PathVariable(value = "id") int id){
        Modifications Modification = this.ModificationsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Modification not found")
        );

        return  ResponseEntity.ok().body(Modification);
    }

    @PostMapping("/create")
    public Modifications createModification(@RequestBody Modifications Modification){
        return this.ModificationsRepo.save(Modification);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteModification(@PathVariable(value = "id") int id){
        Modifications Modification =this.ModificationsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Modification not found "+id)
        );

        this.ModificationsRepo.delete(Modification);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Modifications updateModification(@RequestBody Modifications newModification, @PathVariable(value = "id") int id){
        return this.ModificationsRepo.findById(id)
                .map(Modification -> {
                    Modification.setHtml(newModification.getHtml());
                    Modification.setDate(newModification.getDate());

                    return this.ModificationsRepo.save(Modification);
                })
                .orElseGet(()->{
                    newModification.setID(id);
                    return this.ModificationsRepo.save(newModification);
                });
    }
}

