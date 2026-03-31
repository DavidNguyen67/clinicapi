package com.clinicsystem.clinicapi.service;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.clinicsystem.clinicapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BloomFilterWarmUp implements ApplicationRunner {

    private final UserRepository userRepository;
    private final EmailBloomFilter emailBloomFilter;
    private final PhoneBloomFilter phoneBloomFilter;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Warming up bloom filter...");

        int pageSize = 1000;
        int page = 0;

        while (true) {
            List<Object[]> users = userRepository.findAllEmails(
                    PageRequest.of(page, pageSize));

            if (users.isEmpty())
                break;

            users.forEach(user -> emailBloomFilter.add((String) user[0]));
            users.forEach(user -> phoneBloomFilter.add((String) user[1]));

            log.info("Bloom filter warmed up {}", (page + 1) * pageSize);
            page++;
        }

        log.info("Bloom filter warm up completed");
    }
}