package com.hoyrak.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.hoyrak.model.CustomerInfo;

@Component  //generic stereotype for any Spring component
public class CustomerInfoValidator implements Validator{

	 private EmailValidator emailValidator = EmailValidator.getInstance();
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz==CustomerInfo.class;
	}
	
	@Override
	public void validate(Object target, Errors errors) 
	{
		CustomerInfo custInfo=(CustomerInfo) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.address");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.phone");
		
		 if (!emailValidator.isValid(custInfo.getEmail())) 
		 {
	            errors.rejectValue("email", "Pattern.customerForm.email");
		 }
	}
}
