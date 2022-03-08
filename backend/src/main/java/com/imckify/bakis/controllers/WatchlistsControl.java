package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.WatchlistCompanies;
import com.imckify.bakis.models.Watchlists;
import com.imckify.bakis.repos.CompaniesRepo;
import com.imckify.bakis.repos.WatchlistCompaniesRepo;
import com.imckify.bakis.repos.WatchlistsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Watchlists")
public class WatchlistsControl {

    @Autowired
    private WatchlistsRepo WatchlistsRepo;

    @Autowired
    private WatchlistCompaniesRepo wcRepo;

    @Autowired
    private CompaniesRepo CompaniesRepo;

    @GetMapping("/investor/{id}")
    public List<Watchlists> getInvestorWatchlist(@PathVariable(value = "id") int id){
        return this.WatchlistsRepo.getWatchlistsByInvestorsID(id).orElseGet(ArrayList::new);
    }

    @GetMapping("/{id}")
    public Watchlists getWatchlists(@PathVariable(value = "id") int id){
        return this.WatchlistsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Watchlist not found"));
    }

    @PostMapping("/create")
    public Watchlists createWatchlist(@RequestBody Watchlists Watchlist){
        return this.WatchlistsRepo.save(Watchlist);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable(value = "id") int id){
        Watchlists Watchlist = this.WatchlistsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Watchlist not found "+id));

        this.WatchlistsRepo.delete(Watchlist);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{name}")
    public Watchlists updateWatchlist(@RequestBody Watchlists newWatchlist, @PathVariable(value = "name") String oldName){
        return this.WatchlistsRepo.findByName(oldName)
                .map(Watchlist -> {
                    Watchlist.setName(newWatchlist.getName());
                    return this.WatchlistsRepo.save(Watchlist);
                })
                .orElseThrow(()-> new ResourceNotFoundException("Watchlist not found"));
    }

    @GetMapping("/investor/{id}")
    public List<WatchlistCompanies> getInvestorWatchlist(@PathVariable(value = "id") int id){
        return this.wcRepo.getWatchlistsByInvestorsID(id).orElseGet(ArrayList::new);
    }

    @PostMapping("/add/{name}")
    public WatchlistCompanies addWatchlistCompany(@RequestBody WatchlistCompanies newWC, @PathVariable(value = "name") String name){
        return this.WatchlistsRepo.findByName(name)
                .map(Watchlist -> {
                    newWC.setWatchlistsID(Watchlist.getID());
                    this.CompaniesRepo.findById(newWC.getCompaniesID()).orElseThrow(()-> new ResourceNotFoundException("Company not found. Bad ID"));
                    return this.wcRepo.save(newWC);
                })
                .orElseThrow(()-> new ResourceNotFoundException("Watchlist not found. Bad ID"));
    }
}

