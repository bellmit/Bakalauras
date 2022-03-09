package com.imckify.bakis.repos;

import com.imckify.bakis.models.WatchlistCompanies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WatchlistCompaniesRepo extends JpaRepository<WatchlistCompanies, Integer> {
//    @Query(value= "SELECT wc.* FROM Watchlists w INNER JOIN WatchlistCompanies wc ON w.ID = wc.WatchlistsID WHERE w.InvestorsID = :id", nativeQuery=true)
//    Optional<List<WatchlistCompanies>> findByInvestorsID(@Param("id") Integer id);
    Optional<WatchlistCompanies> findByCompaniesIDAndWatchlistsID(Integer companiesID, Integer watchlistsID);
}
