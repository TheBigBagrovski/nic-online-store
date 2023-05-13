package nic.project.onlinestore.services.user;

import nic.project.onlinestore.exceptions.EmailNotFoundException;
import nic.project.onlinestore.models.User;
import nic.project.onlinestore.models.UserDetailsImpl;
import nic.project.onlinestore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (!user.isPresent()) throw new EmailNotFoundException("Пользователь не найден");
        return new UserDetailsImpl(user.get());
    }

}
