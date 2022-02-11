package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Requests;
import com.imckify.bakis.repos.RequestsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Requests")
public class RequestsControl {

    @Autowired
    private RequestsRepo RequestsRepo;

    @GetMapping("")
    public List<Requests> getRequests() {
        return this.RequestsRepo.findAll();
    }

    @GetMapping("/unapproved")
    public List<Requests> getUnapprovedRequests() {
        Optional<List<Requests>> Requests = this.RequestsRepo.findByAnalystsIDIsNull();

        return Requests.orElseGet(ArrayList::new);
    }

    @GetMapping("/investor/{id}")
    public List<Requests> getInvestorRequests(@PathVariable(value = "id") int id){
        Optional<List<Requests>> Requests = this.RequestsRepo.findByInvestorsID(id);

        return Requests.orElseGet(ArrayList::new);
    }

    @GetMapping("/{id}")
    public Requests getRequests(@PathVariable(value = "id") int id){
        return this.RequestsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Request not found"));
    }

    @PostMapping("/create")
    public Requests createRequest(@RequestBody Requests Request){
        return this.RequestsRepo.save(Request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable(value = "id") int id){
        Requests Request = this.RequestsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Request not found "+id));

        this.RequestsRepo.delete(Request);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Requests updateRequest(@RequestBody Requests newRequest, @PathVariable(value = "id") int id){
        return this.RequestsRepo.findById(id)
                .map(Request -> {
                    Request.setTicker(newRequest.getTicker());
                    Request.setForm(newRequest.getForm());
                    Request.setYear(newRequest.getYear());
                    Request.setQuarter(newRequest.getQuarter());
                    Request.setAnalystsID(newRequest.getAnalystsID());/////////////////////////////////////////////////

                    return this.RequestsRepo.save(Request);
                })
                .orElseGet(()->{
                    newRequest.setID(id);
                    return this.RequestsRepo.save(newRequest);
                });
    }
}

