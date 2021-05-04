package com.imckify.bakis.controllers;

import com.imckify.bakis.exceptions.ResourceNotFoundException;
import com.imckify.bakis.models.Trades;
import com.imckify.bakis.repos.TradesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Trades")
public class TradesControl {

    @Autowired
    private TradesRepo TradesRepo;

//    @GetMapping("")       // NEREIKALINGAS
//    public ResponseEntity<List<Trades>> getTrades() {
//        return ResponseEntity.ok(
//                this.TradesRepo.findAll()
//        );
//    }

    @GetMapping("/investor/{id}")
    public ResponseEntity<List<Trades>> getInvestorTrades(@PathVariable(value = "id") int id){
        Optional<List<Trades>> Trades = this.TradesRepo.findByInvestorsID(id);

        if(Trades.isPresent()){
            return  ResponseEntity.ok().body(Trades.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trades> getTrades(@PathVariable(value = "id") int id){
        Trades Trade = this.TradesRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Trade not found")
        );

        return  ResponseEntity.ok().body(Trade);
    }

    @PostMapping("/create")
    public Trades createTrade(@RequestBody Trades Trade){
        return this.TradesRepo.save(Trade);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTrade(@PathVariable(value = "id") int id){
        Trades Trade =this.TradesRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Trade not found "+id)
        );

        this.TradesRepo.delete(Trade);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Trades updateTrade(@RequestBody Trades newTrade, @PathVariable(value = "id") int id){
        return this.TradesRepo.findById(id)
                .map(Trade -> {
                    Trade.setTicker(newTrade.getTicker());
                    Trade.setType(newTrade.getType());
                    Trade.setPrice(newTrade.getPrice());
                    Trade.setCurrentPrice(newTrade.getCurrentPrice());
                    Trade.setQuantity(newTrade.getQuantity());

                    Trade.setCurrency(newTrade.getCurrency());
                    Trade.setDateValid(newTrade.getDateValid());
                    Trade.setDatePlaced(newTrade.getDatePlaced());

                    return this.TradesRepo.save(Trade);
                })
                .orElseGet(()->{
                    newTrade.setID(id);
                    return this.TradesRepo.save(newTrade);
                });
    }
}

