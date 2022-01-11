package com.imckify.bakis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imckify.bakis.Bakis.ResourceNotFoundException;
import com.imckify.bakis.models.Users;
import com.imckify.bakis.repos.UsersRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.sql.*;
import java.util.Date;

@RestController
@RequestMapping("/api/Users")
public class UsersControl {

    @Autowired
    private UsersRepo UsersRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    //>>>>>>>>>>>>>>>>>>        Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users User) {
        Optional<Users> Users = this.UsersRepo.findByEmail(User.getEmail());

        if(Users.isPresent()){
//            if(Users.get().getPassword().equals(User.getPassword())) {
            if(bCryptPasswordEncoder.matches(User.getPassword(), Users.get().getPassword())) {
                return ResponseEntity.ok().body(createJWT(Users.get().getID(), Users.get().getUsername()));
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Incorrect password");
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User not found");
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<String> getUserByUsername(@PathVariable(value = "username") String username) throws JsonProcessingException {
        Users User = this.UsersRepo.findByUsername(username).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );
        
        // remove password from store
        Map<String, Object> map = new HashMap<>();
        map.put("id", User.getID());
        map.put("username", User.getUsername());
        map.put("email", User.getEmail());
        map.put("role", User.getRole());
        map.put("portfoliosID", User.getPortfoliosID());

        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);

        return  ResponseEntity.ok().body(jsonResult);
    }
    //<<<<<<<<<<<<<<<<      Login

    @GetMapping("")
    public ResponseEntity<List<Users>> getUsers() {
        return ResponseEntity.ok(
                this.UsersRepo.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUsers(@PathVariable(value = "id") int id){
        Users User = this.UsersRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );

        return  ResponseEntity.ok().body(User);
    }

    @PostMapping("/create")
    public Users createUser(@RequestBody Users User){
        int id = retrieveID();/////////////////////////////////////////////////////////////////////////////////////////

        User.setPassword(bCryptPasswordEncoder.encode(User.getPassword()));
        User.setPortfoliosID(id);
        return this.UsersRepo.save(User);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") int id){
        Users User =this.UsersRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found "+id)
        );

        this.UsersRepo.delete(User);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public Users updateUser(@RequestBody Users newUser, @PathVariable(value = "id") int id){
        return this.UsersRepo.findById(id)
                .map(User -> {
                    User.setUsername(newUser.getUsername());
                    User.setPassword(newUser.getPassword());
                    User.setEmail(newUser.getEmail());
                    User.setRole(newUser.getRole());

                    return this.UsersRepo.save(User);
                })
                .orElseGet(()->{
                    newUser.setID(id);
                    return this.UsersRepo.save(newUser);
                });
    }

    private int retrieveID() {
        int id = 0;
        try {
            String sql = "INSERT INTO Portfolios (`value`, `changeValue`, `date`) VALUES (NULL,NULL,NULL)";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.execute();
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    return Integer.parseInt(rs.getObject(1).toString());  // <-- contains the generated key
                }
            }
            conn.close();
        } catch(Exception e) {
            LoggerFactory.getLogger(UsersControl.class).error("retrieveID(): " + e.getMessage(), e);
        }
        return id;
    }

    private String createJWT(Integer id, String username) {
        Date nowDate = new Date();
        Instant now = nowDate.toInstant();

        return Jwts.builder()
                .claim("UserID", id)
                .claim("id", id)
                .claim("username", username)
//                .setSubject("Investor1")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .compact();
    }
}

