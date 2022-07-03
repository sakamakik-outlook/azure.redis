package com.pcmmtl.azure.redis.contoroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcmmtl.azure.redis.domain.BicCodeList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RestController
@Component
@Slf4j
public class RedisRestController implements InitializingBean {

    @Autowired
    private StringRedisTemplate template;

    static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Initialize Redis cache. Read BIC codes from file and add to Redis if the code doesn't exist.
     *
     * @throws Exception exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        String text = readBicTextFile();

        BicCodeList bics = MAPPER.readValue(text, BicCodeList.class);
        log.info("BIC codes found {}", bics.getList().size());

        ValueOperations<String, String> ops = this.template.opsForValue();

        log.info("Initializing cache...");
        bics.getList().parallelStream().forEach(b -> {
            if (Boolean.FALSE.equals(this.template.hasKey(b.getSwift_code()))) {
                try {
                    log.info("Adding key = {}", b.getSwift_code());
                    ops.set(b.getSwift_code(), MAPPER.writeValueAsString(b));
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            } else {
                log.info("Already has key = {}", b.getSwift_code());
            }
        });
        log.info("Initialization finished.");
    }

    private String readBicTextFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (
                InputStream inputStream = RedisRestController.class.getClassLoader().getResourceAsStream("files/US-BIC-small.json");
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);
        ) {
            for (String line; (line = reader.readLine()) != null; ) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    @GetMapping("/")
    public String info() {
        return "This is a simple example of caching data in Redis. <BR>" +
                "http://&lt;server_address&gt;:8080/get/{bic_code}";
    }

    @GetMapping("/get/{bic}")
    public String get(@PathVariable String bic) {

        ValueOperations<String, String> ops = this.template.opsForValue();

        if (Boolean.TRUE.equals(this.template.hasKey(bic))) {
            log.info("BIC found: {}", bic);
            return ops.get(bic);
        } else {
            return "BIC not found: " + bic;
        }

    }

}