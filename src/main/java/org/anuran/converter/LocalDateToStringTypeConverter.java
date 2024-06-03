package org.anuran.converter;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.EnhancedAttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LocalDateToStringTypeConverter implements AttributeConverter<LocalDate> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Override
    public AttributeValue transformFrom(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return EnhancedAttributeValue.fromString(localDate.format(FORMATTER)).toAttributeValue();
    }

    @Override
    public LocalDate transformTo(AttributeValue attributeValue) {
        if (attributeValue == null || attributeValue.s().isEmpty()) {
            return null;
        }
        return LocalDate.parse(attributeValue.s(), FORMATTER);
    }

    @Override
    public EnhancedType<LocalDate> type() {
        return EnhancedType.of(LocalDate.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }

}
