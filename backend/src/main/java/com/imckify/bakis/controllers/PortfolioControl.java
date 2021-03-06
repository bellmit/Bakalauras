package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Portfolios;
import com.imckify.bakis.repos.PortfoliosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Portfolio")
public class PortfolioControl {

    @Autowired
    private PortfoliosRepo PortfoliosRepo;

    @GetMapping("")
    public List<Portfolios> getPortfolios() {
        return this.PortfoliosRepo.findAll();
    }

    @GetMapping("/{id}")
    public Portfolios getPortfolios(@PathVariable(value = "id") int id){
        return this.PortfoliosRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Portfolio not found"));
    }

    @GetMapping("/investor/{id}")
    public List<Portfolios> getInvestorPortfolios(@PathVariable(value = "id") int id){
        return this.PortfoliosRepo.findByInvestorsID(id).orElseThrow(()-> new ResourceNotFoundException("Portfolio not found"));
    }

    @PostMapping("/create")
    public Portfolios createPortfolio(@RequestBody (required=false) Portfolios Portfolio){
        return this.PortfoliosRepo.save(Portfolio);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable(value = "id") int id){
        Portfolios Portfolio = this.PortfoliosRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Portfolio not found "+id));

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

