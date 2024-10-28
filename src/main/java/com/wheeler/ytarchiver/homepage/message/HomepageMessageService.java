package com.wheeler.ytarchiver.homepage.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class HomepageMessageService {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public List<HomepageMessage> messages;

    @Value("classpath:homepage-messages.yml")
    private Resource messagesFileResource;

    @PostConstruct
    public void loadMessages() {
        try {
            messages = mapper.readValue(messagesFileResource.getURL(), new TypeReference<>() {
            });
            messages.sort(Comparator.comparing(HomepageMessage::getDate));
        } catch (IOException e) {
            log.error("Unable to open homepage-messages file, assigning empty list:", e);
            messages = Collections.emptyList();
        }
    }

    public List<HomepageMessage> getMessages() {
        return messages;
    }
}
