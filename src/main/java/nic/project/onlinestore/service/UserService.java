package nic.project.onlinestore.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nic.project.onlinestore.model.Cart;
import nic.project.onlinestore.model.User;
import nic.project.onlinestore.repository.CartRepository;
import nic.project.onlinestore.repository.UserRepository;
import nic.project.onlinestore.util.Role;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Optional<User> getByEmail(@NonNull String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User register(String email, String encodedPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(Role.ROLE_USER);
        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        cartRepository.save(cart);
        userRepository.save(user);
        return user;
    }

//    public User register()

//    @Override
//    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (!user.isPresent()) throw new EmailNotFoundException("Пользователь c таким email не найден");
//        return new JwtAuthentication();
//    }

//    private final PasswordEncoder passwordEncoder;
//    private final CartRepository cartRepository;
//
//    @Autowired
//    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, CartRepository cartRepository) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//        this.cartRepository = cartRepository;
//    }

//    public User getCurrentAuthorizedUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return userRepository.findByEmail(auth.getName()).get();
//    }
//
//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    @Transactional
//    public void register(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(Role.ROLE_USER);
//        Cart cart = new Cart();
//        cart.setUser(user);
//        user.setCart(cart);
//        cartRepository.save(cart);
//        userRepository.save(user);
//    }

}
