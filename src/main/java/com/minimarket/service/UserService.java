package com.minimarket.service;

import com.minimarket.model.User;
import com.minimarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull; // Import cái này
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
@Autowired
    private PasswordEncoder passwordEncoder;
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Thay vì Suppress, ta dùng @NonNull để khẳng định tham số không null
    public User save(@NonNull User user) {
        // Mã hóa mật khẩu trước khi lưu mới
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(@NonNull User user) {
        // Lưu ý: Việc mã hóa ở hàm update cần cẩn thận 
        // để tránh mã hóa 2 lần chuỗi đã mã hóa rồi.
        return userRepository.save(user);
    }

    public void delete(@NonNull User user) {
        userRepository.delete(user);
    }
}