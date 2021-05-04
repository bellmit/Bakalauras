package com.imckify.bakis.repos;

import com.imckify.bakis.models.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestsRepo extends JpaRepository<Requests, Integer> {
    @Query(value= "SELECT * FROM Requests r WHERE r.analystsID IS NULL", nativeQuery=true)
    List<Requests> findByAnalystsIDIsNull();
}
