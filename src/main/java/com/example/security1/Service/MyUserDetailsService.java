package com.example.security1.Service;

import com.example.security1.Repo.UserRepo;
import com.example.security1.model.UserPrincipal;
import com.example.security1.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Users user = repo.findByUsername(username);

       if (user == null){
           System.out.println("User not found");
           throw new UsernameNotFoundException("User not found");
       }
       return new UserPrincipal(user);
    }
}
