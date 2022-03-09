package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Companies;
import com.imckify.bakis.models.CompaniesVM;
import com.imckify.bakis.repos.CompaniesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Companies")
public class CompaniesControl {

    @Autowired
    private CompaniesRepo CompaniesRepo;

    @GetMapping("")
    public List<CompaniesVM> getCompanies() {
        return this.CompaniesRepo.findAll().stream().map(Companies::toViewModel).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CompaniesVM getCompanies(@PathVariable(value = "id") int id){
        return this.CompaniesRepo.findById(id).map(Companies::toViewModel).orElseThrow(()-> new ResourceNotFoundException("Company not found"));
    }

//    @PostMapping("/create")
//    public CompaniesVM createCompany(@RequestBody Companies Company){
//        return this.CompaniesRepo.save(Company);
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable(value = "id") int id){
        Companies Company = this.CompaniesRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Company not found "+id));

        this.CompaniesRepo.delete(Company);
        return ResponseEntity.ok().build();
    }


//    @PostMapping("/update/{id}")
//    public CompaniesVM updateCompany(@RequestBody Companies newCompany, @PathVariable(value = "id") int id){
//        return this.CompaniesRepo.findById(id)
//                .map(Company -> {
//                    Company.setName(newCompany.getName());
//                    Company.setTicker(newCompany.getTicker());
//
//                    return this.CompaniesRepo.save(Company);
//                })
//                .orElseGet(()->{
//                    newCompany.setID(id);
//                    return this.CompaniesRepo.save(newCompany);
//                });
//    }
}

