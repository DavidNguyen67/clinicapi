package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.KafkaTopics;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "topic")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AppointmentEventDto.class, name = KafkaTopics.APPOINTMENT_EVENT),
        @JsonSubTypes.Type(value = QueueEventDto.class, name = KafkaTopics.QUEUE_EVENTS)
})
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEventDto {
    private String topic;
}