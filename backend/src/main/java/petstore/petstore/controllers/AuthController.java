package petstore.petstore.controllers;

import org.springframework.web.bind.MethodArgumentNotValidException;
import petstore.petstore.dtos.auth.LoginDto;
import petstore.petstore.dtos.auth.RegisterDto;
import petstore.petstore.dtos.users.UserDto;
import petstore.petstore.providers.UserAuthenticationProvider;
import petstore.petstore.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Validated
public class AuthController
{
    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid LoginDto loginDto) throws MethodArgumentNotValidException
    {
        UserDto userDto = userService.login(loginDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail()));

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterDto user) throws MethodArgumentNotValidException
    {
        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(user.getEmail()));

        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

}
