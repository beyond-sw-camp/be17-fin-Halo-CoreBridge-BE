package com.halo.core_bridge.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.halo.core_bridge.common.model.ColorCode;

import java.io.IOException;

public class ColorCodeSerializer extends JsonSerializer<ColorCode> {

    @Override
    public void serialize(ColorCode value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", value.name());
        gen.writeStringField("label", value.getLabel());
        gen.writeStringField("code", value.getCode());
        gen.writeEndObject();
    }
}