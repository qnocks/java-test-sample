package ru.qnocks.keyvalue.repositories;

import org.springframework.stereotype.Component;
import ru.qnocks.keyvalue.domain.KeyValue;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyValueRepository {
    private final List<KeyValue> list = new ArrayList<>();

    public KeyValue save(KeyValue keyValue) {
        list.stream()
                .filter(kv -> kv.getKey().equals(keyValue.getKey()))
                .findFirst()
                .ifPresentOrElse(
                        kv -> {
                            kv.setValue(keyValue.getValue());
                            kv.setTtl(keyValue.getTtl());},
                        () ->
                            list.add(keyValue));
        return keyValue;
    }

    public KeyValue find(String key) {
        return list.stream()
                .filter(keyValue -> keyValue.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }

    public List<KeyValue> findAll() {
        return list;
    }

    public KeyValue delete(String key) {
        KeyValue keyValue = find(key);
        if (keyValue == null) return null;
        list.remove(keyValue);
        return keyValue;
    }
    
    public void saveAll(String data) {
        if (data.isEmpty()) return;
        list.clear();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Long> ttls = new ArrayList<>();

        String[] lines = data.split("\n");

        for (var line : lines) {
            String[] keyAndValuePlusTtl = line.split("=");
            String[] valuePlusTtl = keyAndValuePlusTtl[1].split(" ");
            keys.add(keyAndValuePlusTtl[0]);
            values.add(valuePlusTtl[0]);
            ttls.add(Long.parseLong(valuePlusTtl[1]));
        }

        for (int i = 0; i < keys.size(); i++) {
            list.add(new KeyValue(keys.get(i), values.get(i), ttls.get(i)));
        }
    }

}
