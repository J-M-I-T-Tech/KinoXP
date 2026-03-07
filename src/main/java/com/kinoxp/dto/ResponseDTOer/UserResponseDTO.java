package com.kinoxp.dto.ResponseDTOer;

import com.kinoxp.model.user.Role;

import java.time.LocalDate;

public class UserResponseDTO {
    private Long userId;
    private String name;
    private LocalDate dateOfBirth;
    private Role role;
    private String email;

    public UserResponseDTO(){}

    public UserResponseDTO(Long userId, String name, LocalDate dateOfBirth, Role role, String email) {
        this.userId = userId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
