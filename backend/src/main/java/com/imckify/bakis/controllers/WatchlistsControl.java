package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Watchlists;
import com.imckify.bakis.repos.WatchlistsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Watchlists")
public class WatchlistsControl {

    @Autowired
    private WatchlistsRepo WatchlistsRepo;

    @GetMapping("")
    public List<Watchlists> getWatchlists() {
        return this.WatchlistsRepo.findAll();
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


    @PostMapping("/update/{id}")
    public Watchlists updateWatchlist(@RequestBody Watchlists newWatchlist, @PathVariable(value = "id") int id){
        return this.WatchlistsRepo.findById(id)
                .map(Watchlist -> {
                    Watchlist.setName(newWatchlist.getName());
                    Watchlist.setTicker(newWatchlist.getTicker());
                    Watchlist.setCompaniesID(newWatchlist.getCompaniesID());///////////////////////////////////////////

                    return this.WatchlistsRepo.save(Watchlist);
                })
                .orElseGet(()->{
                    newWatchlist.setID(id);
                    return this.WatchlistsRepo.save(newWatchlist);
                });
    }
}

