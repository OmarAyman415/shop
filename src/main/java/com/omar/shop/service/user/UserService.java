package com.omar.shop.service.user;

import com.omar.shop.exceptions.AlreadyExistException;
import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.User;
import com.omar.shop.repository.UserRepository;
import com.omar.shop.request.CreateUserRequest;
import com.omar.shop.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    public static final String USER_NOT_FOUND = "User not found!";

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(this::assignUserData)
                .orElseThrow(() -> new AlreadyExistException("Oops! " + request.getEmail() + " already exist!"));
    }

    private User assignUserData(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser -> updateUserData(existingUser, request))
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    private User updateUserData(User user, UpdateUserRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException(USER_NOT_FOUND);
                });

    }
}
