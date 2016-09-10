package com.example.thymeleaf.common;

import com.example.thymeleaf.model.User;
import com.example.thymeleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * user details service implementation
 *
 * @author linux_china
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    public CurrentUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(Long.parseLong(username));
        return new CurrentUserDetails(user);
    }

}


