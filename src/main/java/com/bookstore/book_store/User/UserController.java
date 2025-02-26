package com.bookstore.book_store.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public OidcUser getUser(OidcUser oidcUser) {
        userService.saveUser(oidcUser);
        return oidcUser;
    }
}