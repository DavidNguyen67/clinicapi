package com.clinicsystem.clinicapi.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
import com.clinicsystem.clinicapi.dto.AuthEventDto;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

        @Value(value = "${spring.kafka.bootstrap-servers}")
        private String bootstrapAddress;

        @Bean
        public ConsumerFactory<String, AppointmentEventDto> appointmentEventConsumerFactory() {
                return new DefaultKafkaConsumerFactory<>(buildConfigProps(AppointmentEventDto.class),
                                new StringDeserializer(),
                                new JsonDeserializer<>(AppointmentEventDto.class, false));
        }

        @Bean(name = { "appointmentKafkaListenerContainerFactory" })
        public ConcurrentKafkaListenerContainerFactory<String, AppointmentEventDto> appointmentKafkaListenerContainerFactory() {
                ConcurrentKafkaListenerContainerFactory<String, AppointmentEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(appointmentEventConsumerFactory());
                return factory;
        }

        @Bean
        public ConsumerFactory<String, AuthEventDto> authEventConsumerFactory() {
                return new DefaultKafkaConsumerFactory<>(buildConfigProps(AuthEventDto.class), new StringDeserializer(),
                                new JsonDeserializer<>(AuthEventDto.class, false));
        }

        @Bean(name = "authKafkaListenerContainerFactory")
        public ConcurrentKafkaListenerContainerFactory<String, AuthEventDto> authKafkaListenerContainerFactory() {
                ConcurrentKafkaListenerContainerFactory<String, AuthEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(authEventConsumerFactory());
                return factory;
        }

        private Map<String, Object> buildConfigProps(Class<?> valueType) {
                Map<String, Object> configProps = new HashMap<>();
                configProps.put(
                                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                bootstrapAddress);
                configProps.put(
                                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                                StringDeserializer.class);
                configProps.put(
                                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                                JsonDeserializer.class);
                configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.clinicsystem.clinicapi.dto");
                configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType.getName());
                configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
                return configProps;
        }
}