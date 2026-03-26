package com.kinoxp.shared;

import com.kinoxp.user.Role;
import com.kinoxp.user.User;
import com.kinoxp.user.UserService;

public class AdminChecker {
    public static boolean isAdmin(UserService userService, Long userId) {
        User user = userService.findById(userId);
        return user != null && user.getRole() == Role.ADMIN;
    }
}
