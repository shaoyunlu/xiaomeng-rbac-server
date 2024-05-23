package com.xm.converters;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class EmptyStringToNullConverter implements Converter<String, String> {
    @Override
    public String convert(MappingContext<String, String> context) {
        String source = context.getSource();
        return (source != null && source.isEmpty()) ? null : source;
    }
}