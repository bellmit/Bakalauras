package com.imckify.bakis.repos;

import com.imckify.bakis.models.Filings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilingsRepo extends JpaRepository<Filings, Integer> {
}
