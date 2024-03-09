package com.example.appointmentscheduler.service.user;

import com.example.appointmentscheduler.dao.RoleRepository;
import com.example.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.appointmentscheduler.entity.user.Role;
import com.example.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.appointmentscheduler.model.UserForm;
import com.example.appointmentscheduler.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RetailCustomerUserServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RetailCustomerRepository retailCustomerRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserForm retailUserForm;
    RetailCustomer retailCustomer;
    Optional<RetailCustomer> optionalRetailCustomer;

    private int userId;
    private String userName;
    private String password;
    private String matchingPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String street;
    private String postcode;
    private String city;

    String passwordEncoded;

    private String roleNameCustomer;
    private String roleNameRetailCustomer;
    private Role roleCustomer;
    private Role roleRetailCustomer;
    Collection<Role> retailCustomerRoles;


    @Before
    public void initObjects() {

        userId = 1;
        userName = "username";
        password = "password";
        matchingPassword = "password";
        firstName = "firstname";
        lastName = "lastname";
        email = "email@example.com";
        mobile = "123456789";
        // TODO - sửa chung thành address
        street = "street";
        postcode = "12-345";
        city = "city";

        passwordEncoded = "xxxx";

        retailUserForm = new UserForm();
        retailUserForm.setUserName(userName);
        retailUserForm.setPassword(password);
        retailUserForm.setMatchingPassword(matchingPassword);
        retailUserForm.setFirstName(firstName);
        retailUserForm.setLastName(lastName);
        retailUserForm.setEmail(email);
        retailUserForm.setMobile(mobile);
        retailUserForm.setStreet(street);
//        retailUserForm.setPostcode(postcode);
        retailUserForm.setCity(city);
        retailUserForm.setId(userId);

        roleNameCustomer = "ROLE_CUSTOMER";
        roleNameRetailCustomer = "ROLE_CUSTOMER_RETAIL";
        roleCustomer = new Role(roleNameCustomer);
        roleRetailCustomer = new Role(roleNameRetailCustomer);
        retailCustomerRoles = new HashSet<>();
        retailCustomerRoles.add(roleCustomer);
        retailCustomerRoles.add(roleRetailCustomer);


    }
    /**
     * Test for saving new retail customer
     */
    @Test
    public void shouldSaveNewRetailCustomer() {
        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);
        // verify that save method was called once and capture the argument
        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
    }

    /**
     * Test for ecoding password for new retail customer
     */
    @Test
    public void shouldEncodePasswordWhenForNewRetailCustomer() {
        when(passwordEncoder.encode(password)).thenReturn(passwordEncoded);
        // Khởi tạo một đối tượng ArgumentCaptor để bắt đối tượng RetailCustomer được truyền vào phương thức save()
        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        // lưu đối tượng RetailCustomer
        userService.saveNewRetailCustomer(retailUserForm);
        // xác nhận rằng phương thức save() được gọi một lần và bắt được đối tượng RetailCustomer được truyền vào
        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        // xác nhận rằng mật khẩu đã được mã hóa
        Assert.assertEquals(argumentCaptor.getValue().getPassword(), passwordEncoded);
    }

    @Test
    public void userFormDataShouldMatchRetailCustomerObject() {

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue().getUserName(), retailUserForm.getUserName());
        Assert.assertEquals(argumentCaptor.getValue().getFirstName(), retailUserForm.getFirstName());
        Assert.assertEquals(argumentCaptor.getValue().getLastName(), retailUserForm.getLastName());
        Assert.assertEquals(argumentCaptor.getValue().getEmail(), retailUserForm.getEmail());
        Assert.assertEquals(argumentCaptor.getValue().getMobile(), retailUserForm.getMobile());
        Assert.assertEquals(argumentCaptor.getValue().getStreet(), retailUserForm.getStreet());
        Assert.assertEquals(argumentCaptor.getValue().getCity(), retailUserForm.getCity());
//        Assert.assertEquals(argumentCaptor.getValue().getPostcode(), retailUserForm.getPostcode());
    }

    @Test
    public void shouldAssignTwoRolesForRetailCustomer() {
        doReturn(roleRetailCustomer).when(roleRepository).findByName(roleNameRetailCustomer);
        doReturn(roleCustomer).when(roleRepository).findByName(roleNameCustomer);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue().getRoles().size(), 2);
    }

    @Test
    public void shouldAssignCorrectRolesForRetailCustomer() {
        doReturn(roleRetailCustomer).when(roleRepository).findByName(roleNameRetailCustomer);
        doReturn(roleCustomer).when(roleRepository).findByName(roleNameCustomer);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.saveNewRetailCustomer(retailUserForm);

        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue().hasRole(roleNameRetailCustomer), true);
        Assert.assertEquals(argumentCaptor.getValue().hasRole(roleNameCustomer), true);
    }

    @Test
    public void shouldUpdateRetailCustomerProfileData() {
        RetailCustomer customerToBeUpdated = new RetailCustomer();
        customerToBeUpdated.setId(userId);

        doReturn(customerToBeUpdated).when(retailCustomerRepository).getOne(userId);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.updateRetailCustomerProfile(retailUserForm);
        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue().getFirstName(), retailUserForm.getFirstName());
        Assert.assertEquals(argumentCaptor.getValue().getLastName(), retailUserForm.getLastName());
        Assert.assertEquals(argumentCaptor.getValue().getEmail(), retailUserForm.getEmail());
        Assert.assertEquals(argumentCaptor.getValue().getMobile(), retailUserForm.getMobile());
        Assert.assertEquals(argumentCaptor.getValue().getStreet(), retailUserForm.getStreet());
        Assert.assertEquals(argumentCaptor.getValue().getCity(), retailUserForm.getCity());
//        Assert.assertEquals(argumentCaptor.getValue().getPostcode(), retailUserForm.getPostcode());
    }

    @Test
    public void shouldNotAffectUsernameAndPasswordAndRolesWhileRetailCustomerProfileUpdate() {
        RetailCustomer customerToBeUpdated = new RetailCustomer();
        String currentUsername = "username2";
        String currentPassword = "password2";
        customerToBeUpdated.setUserName(currentUsername);
        customerToBeUpdated.setPassword(currentPassword);
        customerToBeUpdated.setRoles(retailCustomerRoles);
        doReturn(customerToBeUpdated).when(retailCustomerRepository).getOne(userId);

        ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
        userService.updateRetailCustomerProfile(retailUserForm);
        verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals(argumentCaptor.getValue().getUserName(), currentUsername);
        Assert.assertEquals(argumentCaptor.getValue().getPassword(), currentPassword);
        Assert.assertEquals(argumentCaptor.getValue().getRoles(), retailCustomerRoles);
    }

    @Test
    public void shouldFindRetailCustomerById() {
        retailCustomer = new RetailCustomer();
        retailCustomer.setId(userId);
        retailCustomer.setUserName(userName);
        optionalRetailCustomer = Optional.of(retailCustomer);
        when(retailCustomerRepository.findById(userId)).thenReturn(optionalRetailCustomer);
        Assert.assertEquals(optionalRetailCustomer.get(), userService.getRetailCustomerById(userId));
        verify(retailCustomerRepository, times(1)).findById(userId);
    }

}


