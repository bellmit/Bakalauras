package com.imckify.bakis.repos;

import com.imckify.bakis.models.Companies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompaniesRepo extends JpaRepository<Companies, Integer> {
    Optional<List<Companies>> findByName(String name);
    Optional<List<Companies>> findByTickerIn(List<String> tickerList);
}
