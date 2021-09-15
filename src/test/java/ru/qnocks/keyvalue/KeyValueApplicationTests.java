package ru.qnocks.keyvalue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KeyValueApplicationTests {
    @Autowired
    MessageSource messageSource;

    @Test
    void contextLoads() {
        assertThat(messageSource).isNotNull();
    }

}
