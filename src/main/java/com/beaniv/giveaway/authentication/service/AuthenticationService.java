package com.beaniv.giveaway.authentication.service;

import com.beaniv.giveaway.model.Credentials;
import com.beaniv.giveaway.model.dto.UserDto;
import com.beaniv.giveaway.model.dto.UserRegistrationDto;
import com.beaniv.giveaway.model.entity.User;
import com.beaniv.giveaway.util.dtotransformservice.DtoTransformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beaniv.giveaway.authentication.security.jwt.JwtTokenProvider;
import com.beaniv.giveaway.authentication.security.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    @Lazy
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final DtoTransformService userDtoTransformService;

    @Transactional
    public String generateToken(Credentials credentials) {
        String login = credentials.getLogin();
        String password = credentials.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        User user = userService.findByEmail(login);

        log.info("User {} logged in", login);

        return jwtTokenProvider.createToken(login, user.getRoles());
    }

    public void registerUser(UserRegistrationDto userRegistrationDto) {
        log.info("IN registerUser - user {} wants to be registered", userRegistrationDto.getEmail());

        checkEmail(userRegistrationDto);
        userService.register(userDtoTransformService.convertToUser(userRegistrationDto));
    }

    private void checkEmail(UserDto userDto) {
        if (userService.findByEmail(userDto.getEmail()) != null) {
            throw new IllegalArgumentException("Your email address is already registered: " + userDto.getEmail());
        }
        log.info("IN checkEmail - email {} is available", userDto.getEmail());
    }
}
