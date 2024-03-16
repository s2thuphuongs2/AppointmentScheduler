package com.example.appointmentscheduler.validation;

import com.example.appointmentscheduler.model.UserForm;
import com.example.appointmentscheduler.validation.groups.UpdateDoctor;
import com.example.appointmentscheduler.validation.groups.UpdateUser;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UpdateDoctorValidationTest {

    private ValidatorFactory factory;
    private Validator validator;

    @Before
    public void stup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldHave9ViolationsForEmptyFormWhenUpdateDoctor() {
        UserForm form = new UserForm();
        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, UpdateUser.class, UpdateDoctor.class);
        assertEquals(violations.size(), 9);
    }
}
