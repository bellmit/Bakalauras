package com.imckify.bakis.adapters.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Component
public class AlphaVantageAdapter {
    public static final Logger logger = LoggerFactory.getLogger(AlphaVantageAdapter.class);

    private String priceDir = "";

    @PostConstruct
    private void setupPriceDir () {
        String resourcesDir = Objects.requireNonNull(getClass().getResource("/")).getPath();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String prefix = "prices_alphaVantage_";
        this.priceDir = prefix + sdf.format(new Date());

        try {
            Files.walk(Paths.get(resourcesDir), 1)
                    .filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .filter(s -> s.contains(prefix))
                    .forEach(s -> {
                        if (!s.contains(this.priceDir)) {
                            try {
                                FileSystemUtils.deleteRecursively(Paths.get(resourcesDir, s));
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        }
                    });


            Files.createDirectory(Paths.get(resourcesDir, this.priceDir));
        } catch (IOException e) {
            logger.error((Arrays.toString(e.getStackTrace())));
            e.printStackTrace();
        }
    }
}
