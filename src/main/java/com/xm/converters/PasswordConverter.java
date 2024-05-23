// 在converters包下创建PasswordConverter.java
package com.xm.converters;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;


public class PasswordConverter implements Converter<String, String> {
    @Override
    public String convert(MappingContext<String, String> context) {
        String source = context.getSource();
        return (source == null) ? null : BCrypt.hashpw(source, BCrypt.gensalt());
    }
}