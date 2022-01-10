package com.imckify.bakis.repos;

import com.imckify.bakis.models.Filings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilingsRepo extends JpaRepository<Filings, Integer> {
    Optional<List<Filings>> findByCompaniesIDInAndFormIn(List<Integer> companyIDs, List<String> typeList);
}
