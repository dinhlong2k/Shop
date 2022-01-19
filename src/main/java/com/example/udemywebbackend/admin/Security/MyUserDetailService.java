package com.example.udemywebbackend.admin.Security;

import com.example.udemywebbackend.admin.User.User;
import com.example.udemywebbackend.admin.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user=userRepo.findByEmail(email);

        if(user == null) throw new UsernameNotFoundException("Could not find this User");

        return new UserPrincipal(user);
    }
}
