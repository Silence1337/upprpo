package com.beaniv.giveaway.authentication.security.jwt;

import com.beaniv.giveaway.model.entity.Role;
import com.beaniv.giveaway.model.entity.Status;
import com.beaniv.giveaway.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

class JwtUserFactoryTest {

    @Test
    void testCreate() {
        User user = new User();
        user.setId(1);
        user.setEmail("ivan@gmail.com");
        user.setPassword("password");
        user.setStatus(Status.ACTIVE);

        List<Role> userRoles = new ArrayList<>();

        Role role = new Role();
        role.setName("HR");

        userRoles.add(role);

        user.setRoles(userRoles);

        JwtUser jwtUser = JwtUserFactory.create(user);

        Assertions.assertEquals(user.getId(), jwtUser.getId());
        Assertions.assertEquals(user.getEmail(), jwtUser.getEmail());
        Assertions.assertEquals(user.getPassword(), jwtUser.getPassword());
        Assertions.assertTrue(jwtUser.isEnabled());

        int i = 0;
        for (GrantedAuthority g : jwtUser.getAuthorities()) {
            Assertions.assertEquals(user.getRoles().get(i).getName(), g.getAuthority());
            i++;
        }
    }
}