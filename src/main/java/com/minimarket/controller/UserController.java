package com.minimarket.controller;
import com.minimarket.model.User;
import com.minimarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
        private PasswordEncoder passwordEncoder;
    // Lấy toàn bộ danh sách nhân viên
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // Lấy thông tin 1 user theo username (Dùng cho hàm editUser trong JS)
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm mới tài khoản
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
        }
        return ResponseEntity.ok(userService.save(user));
    }

    // Cập nhật tài khoản (Xử lý cả đổi mật khẩu hoặc giữ nguyên)
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User userDetails) {
        return userService.findByUsername(username)
                .map(user -> {
                    user.setRole(userDetails.getRole());
                    user.setFullName(userDetails.getFullName()); 

                    // Kiểm tra nếu người dùng có nhập mật khẩu mới trên giao diện
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        // Mã hóa mật khẩu mới trước khi set vào object
                        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
                        user.setPassword(encodedPassword);
                    }
                    
                    return ResponseEntity.ok(userService.update(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Xóa tài khoản
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> {
                    if (user != null) { // Thêm check null tường minh bên trong map
                        userService.delete(user);
                        return ResponseEntity.ok().build();
                    }
                    return ResponseEntity.notFound().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}