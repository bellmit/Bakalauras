package com.imckify.bakis.repos;

import com.imckify.bakis.models.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestsRepo extends JpaRepository<Requests, Integer> {
    @Query(value= "SELECT * FROM Requests r WHERE r.analystsID IS NULL", nativeQuery=true)
    Optional<List<Requests>> findByAnalystsIDIsNull();
    Optional<List<Requests>> findByInvestorsID(Integer id);

}
