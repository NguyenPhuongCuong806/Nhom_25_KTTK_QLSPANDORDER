package com.example.userservice.converter;

import com.example.userservice.enums.RoleEnums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<RoleEnums,Integer> {

    @Override
    public Integer convertToDatabaseColumn(RoleEnums roleEnums) {
        if(roleEnums == null){
            return null;

        }
        return roleEnums.getValue();
    }

    @Override
    public RoleEnums convertToEntityAttribute(Integer integer) {
        if(integer == null){
            return null;

        }
        return Stream.of(RoleEnums.values())
                .filter(c->c.getValue() == integer)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
