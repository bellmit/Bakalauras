package com.imckify.bakis.repos;

import com.imckify.bakis.models.Watchlists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistsRepo extends JpaRepository<Watchlists, Integer> {
}
