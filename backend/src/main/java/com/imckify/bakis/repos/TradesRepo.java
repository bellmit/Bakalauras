package com.imckify.bakis.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.imckify.bakis.models.Trades;

import java.util.List;
import java.util.Optional;

public interface TradesRepo extends JpaRepository<Trades, Integer> {
    Optional<List<Trades>> findByInvestorsID(Integer id);
}
