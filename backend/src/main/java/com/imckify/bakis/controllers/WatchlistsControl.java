package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.CompaniesResponse;
import com.imckify.bakis.models.Watchlists;
import com.imckify.bakis.models.WatchlistsResponse;
import com.imckify.bakis.repos.WatchlistsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Watchlists")
public class WatchlistsControl {

    @Autowired
    private WatchlistsRepo WatchlistsRepo;

    @GetMapping("/investor/{id}")
    public List<WatchlistsResponse> getInvestorWatchlists(@PathVariable(value = "id") int investorID){
        return this.WatchlistsRepo.findByInvestorsID(investorID).orElseGet(ArrayList::new).stream().map(dto -> {
            WatchlistsResponse r = new WatchlistsResponse();
            r.setID(dto.getID());
            r.setName(dto.getName());
            r.setInvestorsID(dto.getInvestorsID());
            r.setCompanies(dto.getCompanies().stream().map(c -> {
                CompaniesResponse cr = new CompaniesResponse();
                cr.setID(c.getID());
                cr.setTicker(c.getTicker());
                cr.setName(c.getName());
                return cr;
            }).collect(Collectors.toList()));
            return r;
        }).collect(Collectors.toList());
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
}

