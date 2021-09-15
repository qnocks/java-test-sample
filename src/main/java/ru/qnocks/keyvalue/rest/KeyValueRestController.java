package ru.qnocks.keyvalue.rest;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.qnocks.keyvalue.domain.KeyValue;
import ru.qnocks.keyvalue.exceptions.BadRequestException;
import ru.qnocks.keyvalue.exceptions.InternalServerErrorException;
import ru.qnocks.keyvalue.services.KeyValueService;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/")
public class KeyValueRestController {
    private final KeyValueService keyValueService;
    private final MessageSource msgSource;

    @Autowired
    public KeyValueRestController(KeyValueService keyValueService, MessageSource messageSource) {
        this.keyValueService = keyValueService;
        this.msgSource = messageSource;
    }

    @GetMapping("{key}")
    public ResponseEntity<KeyValue> get(@PathVariable("key") String key) {
        KeyValue keyValue = keyValueService.find(key);
        if (keyValue == null) {
            throw new BadRequestException(msgSource.getMessage("message.get.fail", null, Locale.US));
        }
        return new ResponseEntity<>(keyValue, HttpStatus.OK);
    }

    @PostMapping(value = "{key}/{value}")
    public ResponseEntity<KeyValue>  set(@PathVariable("key") String key,
                                        @PathVariable("value") String value,
                                        @RequestParam(value = "ttl", required = false, defaultValue = "${ttl.default}") Long ttl) {
        KeyValue keyValue = keyValueService.save(new KeyValue(key, value, ttl));
        if (keyValue == null) {
            throw new InternalServerErrorException(msgSource.getMessage("message.post.fail", null, Locale.US));
        }
        return new ResponseEntity<>(keyValue, HttpStatus.CREATED);
    }

    @DeleteMapping("{key}")
    public ResponseEntity<KeyValue>  delete(@PathVariable("key") String key) {
        KeyValue keyValue = keyValueService.delete(key);
        if (keyValue == null) {
            throw new BadRequestException(msgSource.getMessage("message.delete.fail", null, Locale.US));
        }
        return new ResponseEntity<>(keyValue, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "dump")
    public ResponseEntity<Resource> dump() {
        Path path = keyValueService.dump();
        UrlResource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @GetMapping("load")
    public ResponseEntity<Void> load() {
        keyValueService.load();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<KeyValue>> list() {
        return new ResponseEntity<>(keyValueService.findAll(), HttpStatus.OK);
    }
}
