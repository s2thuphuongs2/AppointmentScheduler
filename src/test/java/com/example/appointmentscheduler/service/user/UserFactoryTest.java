package com.example.appointmentscheduler.service.user;

import com.example.appointmentscheduler.model.UserForm;

public class UserFactoryTest {

    public static UserForm prepareSampleUserForm() {
        UserForm userForm = new UserForm();
        userForm.setCity("Wroclaw");
        userForm.setEmail("example@example.com");
        userForm.setFirstName("Tomasz");
        userForm.setLastName("Nowak");
        userForm.setMobile("123456");
        // TODO - change to address
//        userForm.setPostcode("12-234");
        userForm.setStreet("Street");
        userForm.setUserName("sampleUser");
        //---
        userForm.setPassword("password");
        return userForm;
    }
}
