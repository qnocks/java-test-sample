package ru.qnocks.keyvalue.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.qnocks.keyvalue.domain.KeyValue;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduleService {
    private final KeyValueService keyValueService;

    // time to update ttl of values
    @Value("${schedule.check}")
    private Long checkTime;

    // frequency to log updating info
    @Value("${schedule.log-frequency}")
    private Integer frequency;

    // counter of invokes
    private long invokeTimes = 0L;

    @Autowired
    public ScheduleService(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @Scheduled(fixedDelayString = "${schedule.check}")
    public void updateKeyValueTtl() {
        invokeTimes++;
        if (keyValueService.findAll() == null) return;
        keyValueService.findAll()
                .forEach(keyValue -> {
                    keyValue.setTtl(keyValue.getTtl() - checkTime);
                    if (invokeTimes % frequency == 0) log.info("Updating: " + keyValue);
                });
    }

    @Scheduled(fixedDelayString = "${schedule.check}")
    private void checkKeyValueTtl() {
        List<KeyValue> toDelete = keyValueService.findAll().stream()
                .filter(keyValue -> keyValue.getTtl() <= 0)
                .collect(Collectors.toList());
        toDelete.forEach(this::deleteKeyValue);
    }

    private void deleteKeyValue(KeyValue keyValue) {
        keyValueService.delete(keyValue.getKey());
    }
}
