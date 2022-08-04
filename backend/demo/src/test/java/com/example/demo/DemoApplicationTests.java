package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private TextResource textResource;

    @Autowired
    private TextService textService;

    @Test
    public void contextLoads() {
        assertThat(textResource).isNotNull();
        assertThat(textService).isNotNull();
    }

}
