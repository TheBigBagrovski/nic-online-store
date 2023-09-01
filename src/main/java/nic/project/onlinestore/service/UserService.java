package nic.project.onlinestore.service;

import nic.project.onlinestore.exception.exceptions.UserNotFoundException;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.security.utils.Role;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, CartRepository cartRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с таким email не найден"));
    }

    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        userRepository.save(user);
    }

}
