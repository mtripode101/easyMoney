package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.User;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.UserService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphQLController {

    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public User userById(@Argument Long id) {
        return userService.findById(id);
    }

    @QueryMapping
    public User userByEmail(@Argument String email) {
        return userService.findByEmail(email);
    }

    @QueryMapping
    public List<User> allUsers() {
        return userService.findAll();
    }

    @SchemaMapping(typeName = "User", field = "transactions")
    public List<EasyMoney> resolveTransactions(User user) {
        return user.getTransactions();
    }

    @MutationMapping
    public User createUser(@Argument String name, @Argument String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userService.save(user);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.delete(id);
        return true;
    }
}