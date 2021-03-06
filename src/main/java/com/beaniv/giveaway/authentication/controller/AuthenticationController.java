package com.beaniv.giveaway.authentication.controller;

import com.beaniv.giveaway.authentication.service.AuthenticationService;
import com.beaniv.giveaway.model.Credentials;
import com.beaniv.giveaway.model.dto.TokenDto;
import com.beaniv.giveaway.model.dto.UserRegistrationDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    @ApiOperation("Вход в систему")
    public TokenDto signIn(
            @ApiParam(required = true, value = "Данные для входа")
            @RequestBody Credentials credentials) {
        String token = authenticationService.generateToken(credentials);
        return new TokenDto(token);
    }

    @PostMapping("/sign-up")
    @ApiOperation("Регистрация")
    public void signUp(
            @ApiParam(required = true, value = "Информация о пользователе для регистрации")
            @RequestBody UserRegistrationDto userRegistrationDto) {
        authenticationService.registerUser(userRegistrationDto);
    }
}
