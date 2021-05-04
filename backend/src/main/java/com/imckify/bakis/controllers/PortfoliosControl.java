package com.imckify.bakis.controllers;

import com.imckify.bakis.exceptions.ResourceNotFoundException;
import com.imckify.bakis.models.Portfolios;
import com.imckify.bakis.repos.PortfoliosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Portfolios")
public class PortfoliosControl {

    @Autowired
    private PortfoliosRepo PortfoliosRepo;

    @GetMapping("")
    public ResponseEntity<List<Portfolios>> getPortfolios() {
        return ResponseEntity.ok(
                this.PortfoliosRepo.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolios> getPortfolios(@PathVariable(value = "id") int id){
        Portfolios Portfolio = this.PortfoliosRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Portfolio not found")
        );

        return  ResponseEntity.ok().body(Portfolio);
    }

    @PostMapping("/create")
    public Portfolios createPortfolio(@RequestBody (required=false) Portfolios Portfolio){
        return this.PortfoliosRepo.save(Portfolio);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable(value = "id") int id){
        Portfolios Portfolio =this.PortfoliosRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Portfolio not found "+id)
        );

        this.PortfoliosRepo.delete(Portfolio);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Portfolios updatePortfolio(@RequestBody Portfolios newPortfolio, @PathVariable(value = "id") int id){
        return this.PortfoliosRepo.findById(id)
                .map(Portfolio -> {
                    Portfolio.setValue(newPortfolio.getValue());
                    Portfolio.setChangeValue(newPortfolio.getChangeValue());
                    Portfolio.setDate(newPortfolio.getDate());

                    return this.PortfoliosRepo.save(Portfolio);
                })
                .orElseGet(()->{
                    newPortfolio.setID(id);
                    return this.PortfoliosRepo.save(newPortfolio);
                });
    }
}

