package com.imckify.bakis.repos;

import com.imckify.bakis.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationsRepo extends JpaRepository<Notifications, Integer> {
    Optional<List<Notifications>> findByInvestorsID(Integer id);

}
