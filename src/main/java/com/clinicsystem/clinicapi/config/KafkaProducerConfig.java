package com.clinicsystem.clinicapi.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
import com.clinicsystem.clinicapi.dto.AuthEventDto;
import com.clinicsystem.clinicapi.dto.QueueEventDto;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, AppointmentEventDto> appointmentEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(buildConfigProps());
    }

    @Bean(name = "appointmentEventKafkaTemplate")
    public KafkaTemplate<String, AppointmentEventDto> appointmentEventKafkaTemplate() {
        return new KafkaTemplate<>(appointmentEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, AuthEventDto> authEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(buildConfigProps());
    }

    @Bean(name = "authEventKafkaTemplate")
    public KafkaTemplate<String, AuthEventDto> authEventKafkaTemplate() {
        return new KafkaTemplate<>(authEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, QueueEventDto> queueEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(buildConfigProps());
    }

    @Bean(name = "queueEventKafkaTemplate")
    public KafkaTemplate<String, QueueEventDto> queueEventKafkaTemplate() {
        return new KafkaTemplate<>(queueEventProducerFactory());
    }

    private Map<String, Object> buildConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return configProps;
    }
}