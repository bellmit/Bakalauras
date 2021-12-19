package blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("/blog/integration.xml")
public class Application {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = new SpringApplication(Application.class).run(args);
        System.in.read();
        ctx.close();
    }
}
