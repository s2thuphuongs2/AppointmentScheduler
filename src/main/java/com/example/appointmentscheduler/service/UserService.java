package com.example.appointmentscheduler.service;


import com.example.appointmentscheduler.entity.Work;
import com.example.appointmentscheduler.entity.user.Role;
import com.example.appointmentscheduler.entity.user.User;
import com.example.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.appointmentscheduler.entity.user.customer.Customer;
import com.example.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.appointmentscheduler.entity.user.doctor.Doctor;
import com.example.appointmentscheduler.model.ChangePasswordForm;
import com.example.appointmentscheduler.model.UserForm;

import java.util.Collection;
import java.util.List;

public interface UserService {
    /*
     * User
     * */
    boolean userExists(String userName);

    User getUserById(int userId);

    User getUserByUsername(String userName);

    List<User> getUsersByRoleName(String roleName);

    List<User> getAllUsers();

    void deleteUserById(int userId);

    void updateUserPassword(ChangePasswordForm passwordChangeForm);

    /*
     * Doctor
     * */
    Doctor getDoctorById(int doctorId);

    List<Doctor> getDoctorsWithRetailWorks();

    List<Doctor> getDoctorsWithCorporateWorks();

    List<Doctor> getDoctorsByWork(Work work);

    List<Doctor> getAllDoctors();

    void saveNewDoctor(UserForm userForm);

    void updateDoctorProfile(UserForm updateData);

    Collection<Role> getRolesForDoctor();

    /*
     * Customer
     * */
    Customer getCustomerById(int customerId);

    List<Customer> getAllCustomers();

    /*
     * RetailCustomer
     * */
    RetailCustomer getRetailCustomerById(int retailCustomerId);

    void saveNewRetailCustomer(UserForm userForm);

    void updateRetailCustomerProfile(UserForm updateData);

    Collection<Role> getRolesForRetailCustomer();

    /*
     * CorporateCustomer
     * */
    CorporateCustomer getCorporateCustomerById(int corporateCustomerId);

    List<RetailCustomer> getAllRetailCustomers();

    void saveNewCorporateCustomer(UserForm userForm);

    void updateCorporateCustomerProfile(UserForm updateData);

    Collection<Role> getRoleForCorporateCustomers();


}

