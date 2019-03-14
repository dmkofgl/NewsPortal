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
public class UserDetailServiceImpl implements UserDetailsService {
    private final String USER_AUTHORITY = "user";
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        return userService.findUserByUsername(s)
                .map(this::convertUser)
                .orElseThrow(() -> new UsernameNotFoundException("No user with username " + s));
    }

    private UserDetails convertUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                Collections.singletonList(new SimpleGrantedAuthority(USER_AUTHORITY)));

    }
}
