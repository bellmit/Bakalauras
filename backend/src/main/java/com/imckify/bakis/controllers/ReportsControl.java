package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Reports;
import com.imckify.bakis.repos.ReportsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Reports")
public class ReportsControl {

    @Autowired
    private ReportsRepo ReportsRepo;

    @GetMapping("")
    public List<Reports> getReports() {
        return this.ReportsRepo.findAll();
    }

    @GetMapping("/{id}")
    public Reports getReports(@PathVariable(value = "id") int id){
        return this.ReportsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Report not found"));
    }

    @PostMapping("/create")
    public Reports createReport(@RequestBody Reports Report){
        return this.ReportsRepo.save(Report);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable(value = "id") int id){
        Reports Report = this.ReportsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Report not found "+id));

        this.ReportsRepo.delete(Report);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Reports updateReport(@RequestBody Reports newReport, @PathVariable(value = "id") int id){
        return this.ReportsRepo.findById(id)
                .map(Report -> {
                    Report.setCik(newReport.getCik());
                    Report.setTicker(newReport.getTicker());
                    Report.setForm(newReport.getForm());
                    Report.setDate(newReport.getDate());

                    Report.setLocation(newReport.getLocation());////////////////////////////////////////////////////////

                    return this.ReportsRepo.save(Report);
                })
                .orElseGet(()->{
                    newReport.setID(id);
                    return this.ReportsRepo.save(newReport);
                });
    }
}

