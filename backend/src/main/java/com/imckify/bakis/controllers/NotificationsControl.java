package com.imckify.bakis.controllers;

import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Companies;
import com.imckify.bakis.models.Filings;
import com.imckify.bakis.models.Notifications;
import com.imckify.bakis.repos.CompaniesRepo;
import com.imckify.bakis.repos.FilingsRepo;
import com.imckify.bakis.repos.NotificationsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Notifications")
public class NotificationsControl {

    @Autowired
    private NotificationsRepo NotificationsRepo;

    @Autowired
    private FilingsRepo FilingsRepo;

    @Autowired
    private CompaniesRepo CompaniesRepo;

    public static final Logger logger = LoggerFactory.getLogger(NotificationsControl.class);

    @GetMapping("")
    public ResponseEntity<List<Notifications>> getNotifications() {
        return ResponseEntity.ok(
                this.NotificationsRepo.findAll()
        );
    }

    // get notifications' subscriptions (controls)
    @GetMapping("/investor/{id}")
    public ResponseEntity<List<Notifications>> getInvestorNotifications(@PathVariable(value = "id") int id){
        Optional<List<Notifications>> Notifications = this.NotificationsRepo.findByInvestorsIDAndSeenIsNull(id);

        return Notifications.map(notifications -> ResponseEntity.ok().body(notifications)).orElseGet(() -> ResponseEntity.ok().build());
    }

    // produce filings notifications for subscriptions (controls)
    @GetMapping("/produce/investor/{id}")
    public List<Notifications> produceInvestorNotifications(@PathVariable(value = "id") int id){
        // all notification controls
        List<Notifications> subscriptions = this.NotificationsRepo.findByInvestorsIDAndSeenIsNull(id).get();

        // tickers -> companyIDs
        List<String> tickerList = subscriptions.stream().map(Notifications::getName).collect(Collectors.toList());
        List<Companies> companies = this.CompaniesRepo.findByTickerIn(tickerList).get();
        List<Integer> companyIDs = companies.stream().map(Companies::getID).collect(Collectors.toList());

        // get prepare filings for mapping
        List<String> typeList = subscriptions.stream().map(Notifications::getType).distinct().collect(Collectors.toList());
        List<Filings> notificationFilings = this.FilingsRepo.findByCompaniesIDInAndFormIn(companyIDs, typeList).get();

        // map filings to notifications
        List<Notifications> mapped = notificationFilings.stream().map(f -> {
            Notifications n = new Notifications();
            n.setName(companies.stream().filter(c -> c.getID().equals(f.getCompaniesID())).findFirst().map(Companies::getTicker).get());
            n.setType(f.getForm());
            n.setPeriod(f.getDate());
            n.setSeen(false);
            n.setInvestorsID(id);
            return n;
        }).collect(Collectors.toList());

        // get subscribing control for each notification and check notification date with control date
        List<Notifications> news = mapped.stream().filter(n ->
                subscriptions.stream().filter(s ->
                        // get control
                        s.getName().equals(n.getName()) &&
                                s.getType().equals(n.getType()) &&
                                s.getInvestorsID().equals(n.getInvestorsID())
                )
                        .findFirst()
                        // validate period <= subscribed period
                        .filter(op_n -> (d(n.getPeriod()).isBefore(d(op_n.getPeriod())) || n.getPeriod().equals(op_n.getPeriod())))
                        .isPresent()
        ).collect(Collectors.toList());

        // due to Hibernate entity state, saveAll does not check if row already exists
        List<Notifications> existing = this.NotificationsRepo.findByInvestorsIDAndSeenIsNotNull(id).get();
        news = news.stream().filter(n -> !existing.contains(n)).collect(Collectors.toList());

        return this.NotificationsRepo.saveAll(news);
    }

    // get notifications which have been produced using subscriptions (controls)
    @GetMapping("/receive/investor/{id}")
    public ResponseEntity<List<Notifications>> receiveInvestorNotifications(@PathVariable(value = "id") int id){
        Optional<List<Notifications>> realNotifications = this.NotificationsRepo.findByInvestorsIDAndSeenIsNotNull(id);
        return realNotifications.map(notifications -> ResponseEntity.ok().body(notifications)).orElseGet(() -> ResponseEntity.ok().build());
    }

    // mark notification as read
    @GetMapping("/mark/{id}")
    public Notifications markNotification(@PathVariable(value = "id") int id){
        return this.NotificationsRepo.findById(id)
                .map(Notification -> {
                    Notification.setSeen(true);
                    return this.NotificationsRepo.save(Notification);
                })
                .orElseThrow(()-> new ResourceNotFoundException("Notification not found "+id));
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

    private LocalDate d(String dateString) {
        return LocalDate.parse(dateString);
    }
}

