package com.example.springbootsampleec.validators;

import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class MatchFieldsValidator implements ConstraintValidator<MatchFields, Object>{

    private String[] fields;

    @Override
    public void initialize(MatchFields matchFields) {
        this.fields = matchFields.fields();        
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String comparator = (String)beanWrapper.getPropertyValue(fields[0]);       
        return Stream.of(fields).allMatch(s -> ((String)beanWrapper.getPropertyValue(s)).equals(comparator));
    }

}
