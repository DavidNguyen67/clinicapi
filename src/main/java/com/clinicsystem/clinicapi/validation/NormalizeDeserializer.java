package com.clinicsystem.clinicapi.validation;

import com.clinicsystem.clinicapi.util.NormalizationUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class NormalizeDeserializer extends StdDeserializer<String> implements ContextualDeserializer {

    private Normalize.NormalizeType[] normalizeTypes;

    public NormalizeDeserializer() {
        super(String.class);
        this.normalizeTypes = new Normalize.NormalizeType[] { Normalize.NormalizeType.TRIM };
    }

    public NormalizeDeserializer(Normalize.NormalizeType[] normalizeTypes) {
        super(String.class);
        this.normalizeTypes = normalizeTypes;
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return NormalizationUtil.normalize(value, normalizeTypes);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if (property != null) {
            Normalize annotation = property.getAnnotation(Normalize.class);
            if (annotation != null) {
                return new NormalizeDeserializer(annotation.value());
            }
        }
        return this;
    }
}
