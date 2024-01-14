package com.example.appointmentscheduler.service.user;

import com.example.appointmentscheduler.dao.user.UserRepository;
import com.example.appointmentscheduler.entity.user.User;
import com.example.appointmentscheduler.model.ChangePasswordForm;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    // Khai báo đối tượng mock cho mật khẩu mã hóa
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    // Khai báo đối tượng mock cho UserRepository
    @Mock
    private UserRepository userRepository;

    // Tự động inject các mock vào đối tượng cần kiếm thử (UserServiceImpl)
    @InjectMocks
    private UserServiceImpl userService;

    private int userId;
    private String password;
    private String passwordEncoded;
    private String userName;
    private String newPassword;
    private User user;
    private Optional<User> optionalUser;

    // Phương thức được thực hiện trước mỗi phương thức để khởi tạo các đối tượng cần kiểm thử
    @Before
    public void initObjects() {
        userId = 1;
        passwordEncoded = "encodedpass";
        userName = "username";
        password = "password";
        newPassword = "newpassword";
        user = new User();
        user.setId(userId);
        user.setUserName(userName);
        optionalUser = Optional.of(user);
    }

    /**
     * Kiểm tra xem phương thức saveUser có lưu đúng user được truyền vào hay không?
     */
    @Test
    public void shouldFindUserById() {
        // khi được gọi với userId cụ thể, phương thức findById của
        // userRepository sẽ trả về optionalUser (đã được khởi tạo ở trên)
        // tương ứng với userId đó (1)
        // (do đó phương thức getOne của userRepository sẽ trả về user đã được khởi tạo ở trên)
        when(userRepository.findById(userId)).thenReturn(optionalUser);
        // Khẳng định rằng phương thức getUserById(userId) sẽ trả về user đã được khởi tạo ở trên
        Assert.assertEquals(optionalUser.get(), userService.getUserById(userId));
        // xác nhận rằng phương thức findById của userRepository đã được gọi với đúng userId
        verify(userRepository, times(1)).findById(userId);
    }

    /**
     * Kiểm tra xem phương thức saveUser có lưu đúng user được truyền vào hay không?
     */
    @Test
    public void shouldUpdateUserPassword() {
        // trả về user đã được khởi tạo ở trên khi gọi phương thức getOne(userId)
        doReturn(new User()).when(userRepository).getOne(userId);
        // khởi tạo đối tượng ChangePasswordForm với userId đã được khởi tạo ở trên
        ChangePasswordForm changePasswordForm = new ChangePasswordForm(userId);
        // thiết lập mật khẩu hiện tại và mật khẩu mới cho đối tượng changePasswordForm
        userService.updateUserPassword(changePasswordForm);
        // khởi tạo đối tượng bắt các tham số truyền vào phương thức save của userRepository
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        // xác nhận rằng phương thức save của userRepository đã được gọi với đúng user
        verify(userRepository, times(1)).getOne(userId);
        // xác nhận rằng phương thức encode của passwordEncoder đã được gọi với đúng mật khẩu mới
        verify(userRepository, times(1)).save(argumentCaptor.capture());
    }

    /**
     * Test method: should encode user password while updating
     */
    @Test
    public void shouldEncodeUserPasswordWhileUpdate() {
        User userToBeUpdated = new User();
        // set password cho user cần updated
        userToBeUpdated.setPassword(password);
        // thiết lập giá trị trả về của phương thức getOne(userId) của userRepository (mock object),
        // sau đó trả về giá trị userToBeUpdated đã được thiết lập ở trên
        doReturn(userToBeUpdated).when(userRepository).getOne(userId);
        // thiết lập giá trị trả về của phương thức encode(password) của passwordEncoder (mock object),
        // sau đó trả về giá trị passwordEncoded đã được thiết lập ở trên
        doReturn(passwordEncoded).when(passwordEncoder).encode(newPassword);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm(userId);
        // thiết lập giá trị cho các thuộc tính của đối tượng changePasswordForm
        changePasswordForm.setCurrentPassword(password);
        changePasswordForm.setPassword(newPassword);

        userService.updateUserPassword(changePasswordForm);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getPassword(), passwordEncoded);
    }

    /**
     * Test method: should find user by username
     */
    @Test
    public void shouldFindUserBeUsername() {
        User user = new User();
        user.setId(userId);
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findByUserName(userName)).thenReturn(optionalUser);
        Assert.assertEquals(optionalUser.get(), userService.getUserByUsername(userName));
        verify(userRepository, times(1)).findByUserName(userName);
    }


    @Test
    public void shouldFindAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> fetchedUsers = userService.getAllUsers();
        assertEquals(fetchedUsers, users);
        assertEquals(fetchedUsers.size(), 2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldDeleteUserById() {
        userService.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }


}
