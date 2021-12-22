package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Notifications;
import com.imckify.bakis.repos.NotificationsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Notifications")
public class NotificationsControl {

    @Autowired
    private NotificationsRepo NotificationsRepo;

    public static final Logger logger = LoggerFactory.getLogger(NotificationsControl.class);

    @GetMapping("")
    public ResponseEntity<List<Notifications>> getNotifications() {
        return ResponseEntity.ok(
                this.NotificationsRepo.findAll()
        );
    }

    @GetMapping("/investor/{id}")
    public ResponseEntity<List<Notifications>> getInvestorNotifications(@PathVariable(value = "id") int id){
        Optional<List<Notifications>> Notifications = this.NotificationsRepo.findByInvestorsID(id);

        return Notifications.map(notifications -> ResponseEntity.ok().body(notifications)).orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notifications> getNotifications(@PathVariable(value = "id") int id){
        Notifications Notification = this.NotificationsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Notification not found")
        );

        return  ResponseEntity.ok().body(Notification);
    }

    @PostMapping("/create")
    public Notifications createNotification(@RequestBody Notifications Notification){
        return this.NotificationsRepo.save(Notification);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable(value = "id") int id){
        Notifications Notification =this.NotificationsRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Notification not found "+id)
        );

        this.NotificationsRepo.delete(Notification);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update/{id}")
    public Notifications updateNotification(@RequestBody Notifications newNotification, @PathVariable(value = "id") int id){
        return this.NotificationsRepo.findById(id)
                .map(Notification -> {
                    Notification.setName(newNotification.getName());
                    Notification.setType(newNotification.getType());
                    Notification.setPeriod(newNotification.getPeriod());

                    return this.NotificationsRepo.save(Notification);
                })
                .orElseGet(()->{
                    newNotification.setID(id);
                    return this.NotificationsRepo.save(newNotification);
                });
    }

}

