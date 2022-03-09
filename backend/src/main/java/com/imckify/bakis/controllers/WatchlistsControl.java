package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Watchlists;
import com.imckify.bakis.models.WatchlistsVM;
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
    public List<WatchlistsVM> getInvestorWatchlists(@PathVariable(value = "id") int investorID){
        return this.WatchlistsRepo.findByInvestorsID(investorID).orElseGet(ArrayList::new).stream().map(Watchlists::toViewModel).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WatchlistsVM getWatchlists(@PathVariable(value = "id") int id){
        return this.WatchlistsRepo.findById(id).map(Watchlists::toViewModel).orElseThrow(()-> new ResourceNotFoundException("Watchlist not found"));
    }

    // TODO below GUI & BE

//    @PostMapping("/create")
//    public Watchlists createWatchlist(@RequestBody Watchlists Watchlist){
//        return this.WatchlistsRepo.save(Watchlist);
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable(value = "id") int id){
        Watchlists Watchlist = this.WatchlistsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Watchlist not found "+id));

        this.WatchlistsRepo.delete(Watchlist);
        return ResponseEntity.ok().build();
    }


//    @PostMapping("/update/{name}")
//    public Watchlists updateWatchlist(@RequestBody Watchlists newWatchlist, @PathVariable(value = "name") String oldName){
//        return this.WatchlistsRepo.findByName(oldName)
//                .map(Watchlist -> {
//                    Watchlist.setName(newWatchlist.getName());
//                    return this.WatchlistsRepo.save(Watchlist);
//                })
//                .orElseThrow(()-> new ResourceNotFoundException("Watchlist not found"));
//    }
}

