package com.komal.carehub.controller;

import com.komal.carehub.entity.User;
import com.komal.carehub.entity.enums.Role;
import com.komal.carehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    /**
     * Get all users (admin only).
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = userRepository.findAll()
                .stream()
                .map(this::toUserMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Update a user's role (admin only).
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long id, @RequestParam String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
        return ResponseEntity.ok(toUserMap(user));
    }

    /**
     * Delete a user account (admin only).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toUserMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("address", user.getAddress());
        map.put("city", user.getCity());
        map.put("zipCode", user.getZipCode());
        map.put("profilePicture", user.getProfilePicture());
        map.put("role", user.getRole().name());
        map.put("createdAt", user.getCreatedAt());
        return map;
    }
}
