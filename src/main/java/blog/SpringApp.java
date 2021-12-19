package blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringApp.class, args);
        System.in.read();
    }
}
