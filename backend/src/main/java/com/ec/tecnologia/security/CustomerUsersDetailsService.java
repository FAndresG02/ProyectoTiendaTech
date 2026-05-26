package com.ec.tecnologia.security;

import com.ec.tecnologia.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private com.ec.tecnologia.entity.UserEntity userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Inside loadUserByUsername {}", username);

        userDetail = userRepository.findByEmail(username);

        if (userDetail != null) {
            return new User(
                    userDetail.getEmail(),
                    userDetail.getPassword(),
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public com.ec.tecnologia.entity.UserEntity getUserDetail(){
        return userDetail;
    }
}
