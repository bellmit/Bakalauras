package com.imckify.bakis.repos;

import com.imckify.bakis.models.WatchlistCompanies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistCompaniesRepo extends JpaRepository<WatchlistCompanies, Integer> {
    Optional<WatchlistCompanies> findByCompaniesIDAndWatchlistsID(Integer companiesID, Integer watchlistsID);
    Optional<List<WatchlistCompanies>> findAllByWatchlistsID(Integer id);
}
