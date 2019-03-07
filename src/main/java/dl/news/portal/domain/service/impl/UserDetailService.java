package dl.news.portal.domain.service.impl;

import dl.news.portal.domain.entity.User;
import dl.news.portal.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserDetails userDetails = userService.findUserByUsername(s)
                .map(this::mapUser)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + s));
        return userDetails;
    }

    private UserDetails mapUser(User user) {

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                Collections.singletonList(new SimpleGrantedAuthority("user")));
        return userDetails;

    }
}
