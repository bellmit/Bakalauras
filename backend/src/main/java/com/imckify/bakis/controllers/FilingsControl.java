package com.imckify.bakis.controllers;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.imckify.bakis.models.Filings;
import com.imckify.bakis.repos.FilingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Filings")
public class FilingsControl {

    @Autowired
    private FilingsRepo FilingsRepo;

    @GetMapping("")
    public ResponseEntity<List<Filings>> getFilings() {
        return ResponseEntity.ok(
                this.FilingsRepo.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filings> getFilings(@PathVariable(value = "id") int id){
        Filings Filing = this.FilingsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Filing not found")
        );

        return  ResponseEntity.ok().body(Filing);
    }

    @PostMapping("/create")
    public Filings createFiling(@RequestBody Filings Filing){
        return this.FilingsRepo.save(Filing);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFiling(@PathVariable(value = "id") int id){
        Filings Filing =this.FilingsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Filing not found "+id)
        );

        this.FilingsRepo.delete(Filing);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Filings updateFiling(@RequestBody Filings newFiling, @PathVariable(value = "id") int id){
        return this.FilingsRepo.findById(id)
                .map(Filing -> {
                    Filing.setName(newFiling.getName());
                    Filing.setRef(newFiling.getRef());
                    Filing.setDate(newFiling.getDate());
                    Filing.setForm(newFiling.getForm());

                    Filing.setRef(newFiling.getRef());
                    Filing.setCik(newFiling.getCik());
                    Filing.setAccno(newFiling.getAccno());

                    return this.FilingsRepo.save(Filing);
                })
                .orElseGet(()->{
                    newFiling.setID(id);
                    return this.FilingsRepo.save(newFiling);
                });
    }
}

