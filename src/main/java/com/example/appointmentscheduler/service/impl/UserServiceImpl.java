package com.example.appointmentscheduler.service.impl;

import com.example.appointmentscheduler.dao.RoleRepository;
import com.example.appointmentscheduler.dao.user.UserRepository;
import com.example.appointmentscheduler.dao.user.customer.CorporateCustomerRepository;
import com.example.appointmentscheduler.dao.user.customer.CustomerRepository;
import com.example.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.appointmentscheduler.dao.user.doctor.DoctorRepository;
import com.example.appointmentscheduler.entity.Work;
import com.example.appointmentscheduler.entity.WorkingPlan;
import com.example.appointmentscheduler.entity.user.Role;
import com.example.appointmentscheduler.entity.user.User;
import com.example.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.appointmentscheduler.entity.user.customer.Customer;
import com.example.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.appointmentscheduler.entity.user.doctor.Doctor;
import com.example.appointmentscheduler.model.ChangePasswordForm;
import com.example.appointmentscheduler.model.UserForm;
import com.example.appointmentscheduler.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final DoctorRepository doctorRepository;
    private final CustomerRepository customerRepository;
    private final CorporateCustomerRepository corporateCustomerRepository;
    private final RetailCustomerRepository retailCustomerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(DoctorRepository doctorRepository, CustomerRepository customerRepository, CorporateCustomerRepository corporateCustomerRepository, RetailCustomerRepository retailCustomerRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.customerRepository = customerRepository;
        this.corporateCustomerRepository = corporateCustomerRepository;
        this.retailCustomerRepository = retailCustomerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean userExists(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

    @Override
    @PreAuthorize("#userId == principal.id")
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    // phuong thuc getCustomerById duoc thuc hien neu id cua customer duoc truyen vao = id hien tai va user co role la admin
    @Override
    @PreAuthorize("#customerId == principal.id or hasRole('ADMIN')")
    public Customer getCustomerById(int customerId) {
        return customerRepository.getOne(customerId);
    }

    @Override
    public Doctor getDoctorById(int doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    @PreAuthorize("#retailCustomerId == principal.id or hasRole('ADMIN')")
    public RetailCustomer getRetailCustomerById(int retailCustomerId) {
        return retailCustomerRepository.findById(retailCustomerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

    }

    @Override
    @PreAuthorize("#corporateCustomerId == principal.id or hasRole('ADMIN')")
    public CorporateCustomer getCorporateCustomerById(int corporateCustomerId) {
        return corporateCustomerRepository.findById(corporateCustomerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<RetailCustomer> getAllRetailCustomers() {
        return retailCustomerRepository.findAll();
    }


    @Override
    public User getUserByUsername(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<Doctor> getDoctorsWithRetailWorks() {
        return doctorRepository.findAllWithRetailWorks();
    }

    @Override
    public List<Doctor> getDoctorsWithCorporateWorks() {
        return doctorRepository.findAllWithCorporateWorks();
    }

    @Override
    public List<Doctor> getDoctorsByWork(Work work) {
        return doctorRepository.findByWorks(work);
    }

    @Override
    @PreAuthorize("#passwordChangeForm.id == principal.id")
    public void updateUserPassword(ChangePasswordForm passwordChangeForm) {
        User user = userRepository.getOne(passwordChangeForm.getId());
        user.setPassword(passwordEncoder.encode(passwordChangeForm.getPassword()));
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("#updateData.id == principal.id or hasRole('ADMIN')")
    public void updateDoctorProfile(UserForm updateData) {
        Doctor doctor = doctorRepository.getOne(updateData.getId());
        doctor.update(updateData);
        doctorRepository.save(doctor);
    }

    @Override
    @PreAuthorize("#updateData.id == principal.id or hasRole('ADMIN')")
    public void updateRetailCustomerProfile(UserForm updateData) {
        RetailCustomer retailCustomer = retailCustomerRepository.getOne(updateData.getId());
        retailCustomer.update(updateData);
        retailCustomerRepository.save(retailCustomer);

    }

    @Override
    @PreAuthorize("#updateData.id == principal.id or hasRole('ADMIN')")
    public void updateCorporateCustomerProfile(UserForm updateData) {
        CorporateCustomer corporateCustomer = corporateCustomerRepository.getOne(updateData.getId());
        corporateCustomer.update(updateData);
        corporateCustomerRepository.save(corporateCustomer);

    }

    @Override
    public void saveNewRetailCustomer(UserForm userForm) {
        RetailCustomer retailCustomer = new RetailCustomer(userForm, passwordEncoder.encode(userForm.getPassword()), getRolesForRetailCustomer());
        retailCustomerRepository.save(retailCustomer);
    }

    @Override
    public void saveNewCorporateCustomer(UserForm userForm) {
        CorporateCustomer corporateCustomer = new CorporateCustomer(userForm, passwordEncoder.encode(userForm.getPassword()), getRoleForCorporateCustomers());
        corporateCustomerRepository.save(corporateCustomer);
    }

    @Override
    public void saveNewDoctor(UserForm userForm) {
        WorkingPlan workingPlan = WorkingPlan.generateDefaultWorkingPlan();
        Doctor doctor = new Doctor(userForm, passwordEncoder.encode(userForm.getPassword()), getRolesForDoctor(), workingPlan);
        doctorRepository.save(doctor);
    }

    @Override
    public Collection<Role> getRolesForRetailCustomer() {
        HashSet<Role> roles = new HashSet();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_RETAIL"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }


    @Override
    public Collection<Role> getRoleForCorporateCustomers() {
        HashSet<Role> roles = new HashSet();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER_CORPORATE"));
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        return roles;
    }

    @Override
    public Collection<Role> getRolesForDoctor() {
        HashSet<Role> roles = new HashSet();
        roles.add(roleRepository.findByName("ROLE_PROVIDER"));
        return roles;
    }


}

