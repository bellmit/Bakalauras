package com.imckify.bakis.repos;

import com.imckify.bakis.models.Watchlists;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistsRepo extends JpaRepository<Watchlists, Integer> {
    Optional<Watchlists> findByName(String name);
    Optional<List<Watchlists>> findByInvestorsID(Integer id);
}
