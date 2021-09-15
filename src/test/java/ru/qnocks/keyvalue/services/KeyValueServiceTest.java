package ru.qnocks.keyvalue.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.qnocks.keyvalue.domain.KeyValue;
import ru.qnocks.keyvalue.repositories.KeyValueRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class KeyValueServiceTest {
    private KeyValueService underTest;
    @MockBean
    private KeyValueRepository keyValueRepository;
    @MockBean
    private FilesService filesService;

    private final Long TEST_TTL = 3600L;

    @BeforeEach
    void setUp() {
        underTest = new KeyValueService(keyValueRepository, filesService);
    }

    @Test
    @DisplayName("Should save new key-value pair")
    void canSaveNewKeyValue() {
        // given
        KeyValue expected = new KeyValue("some key", "some value", TEST_TTL);
        // when
        underTest.save(expected);
        // then
        var captor = ArgumentCaptor.forClass(KeyValue.class);
        verify(keyValueRepository).save(captor.capture());
        KeyValue actual = captor.getValue();

        assertThat(actual.getKey()).isEqualTo(expected.getKey());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
        assertThat(actual.getTtl()).isEqualTo(expected.getTtl());
    }

    @Test
    @DisplayName("Should get an existing value by a key")
    void canGetValue() {
        // given
        KeyValue expected = new KeyValue("some key", "some value", TEST_TTL);
        // when
        when(keyValueRepository.find(expected.getKey())).thenReturn(expected);
        underTest.save(expected);
        KeyValue actual = underTest.find(expected.getKey());
        // then
        verify(keyValueRepository).save(expected);
        verify(keyValueRepository).find(expected.getKey());

        assertThat(actual.getKey()).isEqualTo(expected.getKey());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
        assertThat(actual.getTtl()).isEqualTo(expected.getTtl());
    }

    @Test
    @DisplayName("Should return null after getting in case of empty value associated with the key")
    void canGetErrorMessage() {
        // given
        String key = "some key";
        // when
        when(keyValueRepository.find(key)).thenReturn(null);
        KeyValue returnedKeyValue = underTest.find(key);
        // then
        verify(keyValueRepository).find(key);
        assertThat(returnedKeyValue).isNull();
    }

    @Test
    @DisplayName("Should delete an existing value by a key")
    void canDeleteExistingValue() {
        // given
        KeyValue expected = new KeyValue("some key", "some value", TEST_TTL);
        // when
        when(keyValueRepository.delete(expected.getKey())).thenReturn(expected);
        KeyValue actual = underTest.delete(expected.getKey());
        // then
        verify(keyValueRepository).delete(expected.getKey());

        assertThat(actual.getKey()).isEqualTo(expected.getKey());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
        assertThat(actual.getTtl()).isEqualTo(expected.getTtl());
    }

    @Test
    @DisplayName("Should return null after deleting in case of empty value associated with a key")
    void canDeleteEmptyValue() {
        // given
        String key = "some key";
        when(keyValueRepository.delete(key)).thenReturn(null);
        // when
        KeyValue returnedKeyValue = underTest.delete(key);
        // then
        verify(keyValueRepository).delete(key);
        assertThat(returnedKeyValue).isNull();
    }

}
