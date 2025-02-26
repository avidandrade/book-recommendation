package com.bookstore.book_store.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(OidcUser oidcUser) {
        String auth0Id = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();

        User user = userRepository.findByAuth0Id(auth0Id)
                .orElse(new User(auth0Id, email, name));

        userRepository.save(user);
    }
}