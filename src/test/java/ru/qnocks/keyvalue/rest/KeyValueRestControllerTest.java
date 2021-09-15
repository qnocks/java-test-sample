package ru.qnocks.keyvalue.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ru.qnocks.keyvalue.domain.KeyValue;
import ru.qnocks.keyvalue.exceptions.ApiException;
import ru.qnocks.keyvalue.services.KeyValueService;

import java.time.ZonedDateTime;
import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KeyValueRestController.class)
public class KeyValueRestControllerTest {
    @MockBean
    private KeyValueService keyValueService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final long TEST_TTL = 3600L;

    @Test
    @DisplayName("GET /api/v1/{key}")
    @SneakyThrows
    void canGetValue() {
        // given
        KeyValue keyValue = new KeyValue("some key", "some value", TEST_TTL);
        // when
        when(keyValueService.find(keyValue.getKey())).thenReturn(keyValue);
        // then
        mockMvc.perform(get("http://localhost:8080/api/v1/{key}", keyValue.getKey()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(keyValue)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"));
    }

    @Test
    @DisplayName("GET /api/v1/{key} (FAILURE)")
    @SneakyThrows
    void cannotGetNonExistingValue() {
        // given
        String key = "some key";
        String httpStatusAsString = "BAD_REQUEST";
        ApiException apiException = new ApiException(
                messageSource.getMessage("message.get.fail", null, Locale.US),
                HttpStatus.valueOf(httpStatusAsString),
                ZonedDateTime.now() // dummy
        );
        // when
        when(keyValueService.find(key)).thenReturn(null);
        // then
        mockMvc.perform(get("http://localhost:8080/api/v1/{key}", key))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(apiException.getMessage()))
                .andExpect(jsonPath("$.httpStatus").value(httpStatusAsString))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"));
    }

    @Test
    @DisplayName("POST /api/v1/{key}/{value}")
    @SneakyThrows
    void canSaveKeyValue() {
        // given
        KeyValue keyValue = new KeyValue("some key", "some value", TEST_TTL);
        // when
        when(keyValueService.save(keyValue)).thenReturn(keyValue);
        // then
        mockMvc.perform(post("http://localhost:8080/api/v1/{key}/{value}", keyValue.getKey(), keyValue.getValue())
                .param("ttl", String.valueOf(TEST_TTL)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(keyValue)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"));
    }

    @Test
    @DisplayName("DELETE /api/v1/{key}")
    @SneakyThrows
    void canDeleteValue() {
        // given
        KeyValue keyValue = new KeyValue("some key", "some value", TEST_TTL);
        // when
        when(keyValueService.delete(keyValue.getKey())).thenReturn(keyValue);
        // then
        mockMvc.perform(delete("http://localhost:8080/api/v1/{key}", keyValue.getKey()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(keyValue)))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"));
    }

    @Test
    @DisplayName("DELETE /api/v1/{key} (FAILURE)")
    @SneakyThrows
    void cannotDeleteNonExistingValue() {
        // given
        String key = "some key";
        String httpStatusAsString = "BAD_REQUEST";
        ApiException apiException = new ApiException(
                messageSource.getMessage("message.delete.fail", null, Locale.US),
                HttpStatus.valueOf(httpStatusAsString),
                ZonedDateTime.now() // dummy
        );
        // then
        mockMvc.perform(delete("http://localhost:8080/api/v1/{key}", key))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(apiException.getMessage()))
                .andExpect(jsonPath("$.httpStatus").value(httpStatusAsString))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"));
    }
}
