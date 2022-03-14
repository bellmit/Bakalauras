package com.imckify.bakis.adapters.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class AlphaVantageAdapter {
    public static final Logger logger = LoggerFactory.getLogger(AlphaVantageAdapter.class);

    private String priceDir = "";

    @PostConstruct
    private void load () throws IOException {
        String resourcesDir = getClass().getResource("/").getPath();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.priceDir = "prices " + sdf.format(new Date());

        String existingPricesDir = null;

        try (Stream<Path> paths = Files.walk(Paths.get(resourcesDir))) {
            existingPricesDir = paths
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().toLowerCase().contains(this.priceDir))
                    .findFirst()
                    .map(Path::toString)
                    .orElseGet(() -> "");
        }

        if (!existingPricesDir.equals("")) {
            Files.walk(Paths.get(resourcesDir))
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().toLowerCase().contains("prices"))
                    .map(p -> {
                        try {
                            return Files.deleteIfExists(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    });
        }

        Files.createDirectory(Paths.get(existingPricesDir));
        this.priceDir = existingPricesDir;
    }
}
