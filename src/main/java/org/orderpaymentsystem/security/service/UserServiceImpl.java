package org.orderpaymentsystem.security.service;

import org.orderpaymentsystem.security.entity.User;
import org.orderpaymentsystem.security.model.UserPrinicipal;
import org.orderpaymentsystem.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    final private UserRepository userRepository;
    
    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user==null) {
            throw new UsernameNotFoundException(username + " user not found");
        }
        UserPrinicipal userPrinicipal = new UserPrinicipal(user);
        return userPrinicipal;
    }
}
