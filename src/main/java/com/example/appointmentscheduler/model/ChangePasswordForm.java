package com.example.appointmentscheduler.model;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Đối tượng biểu diễn biểu mẫu thay đổi mật khẩu người dùng.
 * Áp dụng các quy tắc kiểm tra hợp lệ bằng các annotation từ javax.validation.constraints
 * và các custom validation annotation như @FieldsMatches và @CurrentPasswordMatches.
 */
//TODO: Add password matching
public class ChangePasswordForm {

    // ID của người dùng cần thay đổi mật khẩu
    @NotNull
    private int id;

    // Mật khẩu mới
    @Size(min = 5, max = 10, message = "Password should have 5-15 letters")
    @NotBlank()
    private String password;

    // Mật khẩu xác nhận (phải khớp với mật khẩu mới)
    @Size(min = 5, max = 10, message = "Password should have 5-15 letters")
    @NotBlank()
    private String matchingPassword;

    // Mật khẩu hiện tại của người dùng
    private String currentPassword;

    /**
     * Constructor với ID của người dùng cần thay đổi mật khẩu.
     *
     * @param id ID của người dùng cần thay đổi mật khẩu.
     */
    public ChangePasswordForm(int id) {
        this.id = id;
    }

    /**
     * Phương thức để lấy ID của người dùng.
     *
     * @return ID của người dùng.
     */
    public int getId() {
        return id;
    }

    /**
     * Phương thức để thiết lập ID của người dùng.
     *
     * @param id ID của người dùng cần thiết lập.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Phương thức để lấy mật khẩu hiện tại của người dùng.
     *
     * @return Mật khẩu hiện tại của người dùng.
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Phương thức để thiết lập mật khẩu hiện tại của người dùng.
     *
     * @param currentPassword Mật khẩu hiệ
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * Phương thức để lấy mật khẩu mới của người dùng.
     *
     * @return Mật khẩu mới của người dùng.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Phương thức để thiết lập mật khẩu mới của người dùng.
     *
     * @param password Mật khẩu mới của người dùng cần thiết lập.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Phương thức để lấy mật khẩu xác nhận của người dùng.
     *
     * @return Mật khẩu xác nhận của người dùng.
     */
    public String getMatchingPassword() {
        return matchingPassword;
    }

    /**
     * Phương thức để thiết lập mật khẩu xác nhận của người dùng.
     *
     * @param matchingPassword Mật khẩu xác nhận của người dùng cần thiết lập.
     */
    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}