package com.imckify.bakis.repos;

import com.imckify.bakis.models.Portfolios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PortfoliosRepo extends JpaRepository<Portfolios, Integer> {
    @Query(value= "SELECT p.* FROM Portfolios p INNER JOIN Users u ON p.ID = u.portfoliosID WHERE u.ID=:id", nativeQuery=true)
    Optional<List<Portfolios>> findByInvestorsID(Integer id);
}
