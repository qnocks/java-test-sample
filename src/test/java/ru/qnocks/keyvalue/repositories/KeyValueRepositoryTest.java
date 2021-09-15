package ru.qnocks.keyvalue.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.qnocks.keyvalue.domain.KeyValue;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyValueRepositoryTest {
    private KeyValueRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new KeyValueRepository();
    }

    @Test
    void canSaveAllFromString() {
        // given
        String[] keys = {"key1", "key2", "key3"};
        String[] values = {"key1", "key2", "key3"};
        String[] ttls = {"60000", "40000", "30000"};
        String data = produceStringData(keys, values, ttls);
        // when
        underTest.saveAll(data);
        // then
        assertThat(underTest.find(keys[0]).getValue()).isEqualTo(values[0]);
        assertThat(underTest.find(keys[1]).getValue()).isEqualTo(values[1]);
        assertThat(underTest.find(keys[2]).getValue()).isEqualTo(values[2]);
    }

    String produceStringData(String[] keys, String[] values, String[] ttls) {
        return  keys[0] + "=" + values[0] + " " + ttls[0] + "\n" +
                keys[1] + "=" + values[1] + " " + ttls[1] + "\n" +
                keys[2] + "=" + values[2] + " " + ttls[2] + "\n";
    }
}
