package com.dac.fly.authservice.config;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class DateToOffsetDateTimeConverter
        implements Converter<Date, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(Date source) {
        // ajusta para UTC; se quiser outro fuso, troque ZoneOffset.UTC
        return OffsetDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
    }
}
