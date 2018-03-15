package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.User;
import ro.msg.learning.shop.dto.UserDetailsImpl;
import ro.msg.learning.shop.repository.UserRepository;

@Service
public class ShopUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Autowired
    public ShopUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }
}
