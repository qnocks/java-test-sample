package ru.qnocks.keyvalue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.qnocks.keyvalue.domain.KeyValue;
import ru.qnocks.keyvalue.repositories.KeyValueRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeyValueService {
    private final KeyValueRepository keyValueRepository;
    private final FilesService filesService;

    @Autowired
    public KeyValueService(KeyValueRepository keyValueRepository, FilesService filesService) {
        this.keyValueRepository = keyValueRepository;
        this.filesService = filesService;
    }

    public KeyValue save(KeyValue keyValue) {
        return keyValueRepository.save(keyValue);
    }

    public KeyValue find(String key) {
        return keyValueRepository.find(key);
    }

    public List<KeyValue> findAll() {
        return keyValueRepository.findAll();
    }

    public KeyValue delete(String key) {
        return keyValueRepository.delete(key);
    }

    public Path dump() {
        List<KeyValue> keyValues = keyValueRepository.findAll();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Long> ttls = new ArrayList<>();

        keyValues.forEach(keyValue -> {
            keys.add(keyValue.getKey());
            values.add(keyValue.getValue());
            ttls.add(keyValue.getTtl());
        });

        var sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            sb.append(keys.get(i)).append("=").append(values.get(i)).append(" ").append(ttls.get(i)).append("\n");
        }

        String filename = filesService.dumpData(sb.toString());
        return Path.of(filename);
    }

    public void load() {
        String data = filesService.loadDump();
        keyValueRepository.saveAll(data);
    }
}
