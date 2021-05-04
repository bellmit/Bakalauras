package com.imckify.bakis.repos;

import com.imckify.bakis.models.Companies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompaniesRepo extends JpaRepository<Companies, Integer> {
}
