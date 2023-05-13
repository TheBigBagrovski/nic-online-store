package nic.project.onlinestore.services.user;

import nic.project.onlinestore.models.Cart;
import nic.project.onlinestore.models.Role;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.repositories.CartRepository;
import nic.project.onlinestore.repositories.UserRepository;
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
        return userRepository.findUserByEmail(email).get();
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
