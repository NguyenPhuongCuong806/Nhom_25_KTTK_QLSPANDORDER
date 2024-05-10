package com.example.orderservice.convert;

import com.example.orderservice.enums.OrderEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderEnumConvert implements AttributeConverter<OrderEnum,Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderEnum orderEnum) {
        if(orderEnum == null){
            return null;
        }
        return orderEnum.getValue();
    }

    @Override
    public OrderEnum convertToEntityAttribute(Integer integer) {
        if (integer == null) {

            return null;

        }
        return Stream.of(OrderEnum.values())
                .filter(c->c.getValue() == integer)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
