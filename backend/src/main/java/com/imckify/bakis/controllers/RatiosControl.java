package com.imckify.bakis.controllers;

import com.imckify.bakis.exceptions.ResourceNotFoundException;
import com.imckify.bakis.models.Ratios;
import com.imckify.bakis.repos.RatiosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Ratios")
public class RatiosControl {

    @Autowired
    private RatiosRepo RatiosRepo;

    @GetMapping("")
    public ResponseEntity<List<Ratios>> getRatios() {
        return ResponseEntity.ok(
                this.RatiosRepo.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ratios> getRatios(@PathVariable(value = "id") int id){
        Ratios Ratio = this.RatiosRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Ratio not found")
        );

        return  ResponseEntity.ok().body(Ratio);
    }

    @PostMapping("/create")
    public Ratios createRatio(@RequestBody Ratios Ratio){
        return this.RatiosRepo.save(Ratio);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRatio(@PathVariable(value = "id") int id){
        Ratios Ratio =this.RatiosRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Ratio not found "+id)
        );

        this.RatiosRepo.delete(Ratio);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Ratios updateRatio(@RequestBody Ratios newRatio, @PathVariable(value = "id") int id){
        return this.RatiosRepo.findById(id)
                .map(Ratio -> {
                    Ratio.setFormula(newRatio.getFormula());
                    Ratio.setType(newRatio.getType());

                    return this.RatiosRepo.save(Ratio);
                })
                .orElseGet(()->{
                    newRatio.setID(id);
                    return this.RatiosRepo.save(newRatio);
                });
    }
}

