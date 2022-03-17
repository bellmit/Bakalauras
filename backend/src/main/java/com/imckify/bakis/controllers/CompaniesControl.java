package com.imckify.bakis.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Companies;
import com.imckify.bakis.models.CompaniesVM;
import com.imckify.bakis.repos.CompaniesRepo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    @GetMapping("/yahoo/spark/quotes")
    public Object getLastPrices(){

        String[] tickers = this.getCompanies().stream().map(CompaniesVM::getTicker).toArray(String[]::new);
        List<List<String>> chunks = new ArrayList<>();

        int chunkSize = 20; // chunk size to divide
        for(int i = 0; i < tickers.length; i += chunkSize){
            chunks.add(Arrays.stream(Arrays.copyOfRange(tickers, i, Math.min(tickers.length, i + chunkSize))).collect(Collectors.toList()));
        }

        String url = "https://query1.finance.yahoo.com/v8/finance/spark?symbols=";
        Map<String, Double> prices = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        for(List<String> subList : chunks) {
            String tickersStr = String.join(",", subList);
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                try (CloseableHttpResponse response = client.execute(new HttpGet(url + tickersStr))) {
                    String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    JsonNode node = mapper.readValue(json, JsonNode.class);
                    Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
                    while (iter.hasNext()) {
                        Map.Entry<String, JsonNode> entry = iter.next();
                        if (entry.getValue().hasNonNull("previousClose")) {
                            prices.put(entry.getKey(), entry.getValue().get("previousClose").asDouble());
                        }
                    }
                }
            } catch (IOException ignored) {}
        }

        return prices;
    }
}

