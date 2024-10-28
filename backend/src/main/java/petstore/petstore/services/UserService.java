package petstore.petstore.services;

import petstore.petstore.dtos.auth.LoginDto;
import petstore.petstore.dtos.auth.RegisterDto;
import petstore.petstore.dtos.users.PatchUserDto;
import petstore.petstore.dtos.users.UserDto;
import petstore.petstore.entities.User;
import petstore.petstore.exceptions.AppException;
import petstore.petstore.mappers.UserMapper;
import petstore.petstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto findById(Long id)
    {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(user);
    }

    public Page<UserDto> findAll(Pageable pageable)
    {
        Page<User> users = userRepository.findAll(pageable);
        List<UserDto> userDtos = userMapper.toUserDtoList(users.getContent());

        return new PageImpl<>(userDtos, users.getPageable(), users.getTotalElements());
    }

    public Page<UserDto> findAll(Pageable pageable, String search)
    {
        String[] searchArray = search.split(",");

        String searchParam = searchArray[0];
        String searchValue = searchArray[1];

        Page<UserDto> users = userRepository.findAll(searchParam, searchValue, pageable);

        return users;
    }

    public UserDto update(PatchUserDto patchUserDto, Long id)
    {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (patchUserDto.getEmail() != null && !patchUserDto.getEmail().isEmpty())
        {
            Optional<User> existingUser = userRepository.findByEmail(patchUserDto.getEmail());

            if (existingUser.isPresent())
                throw new AppException("Email already taken", HttpStatus.BAD_REQUEST);
        }

        if (patchUserDto.getPassword() != null && !patchUserDto.getPassword().isEmpty())
        {
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(patchUserDto.getPassword())));
        }

        userMapper.update(user, patchUserDto);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserDto(updatedUser);
    }

    public void delete(Long id)
    {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        userRepository.delete(user);
    }

    public UserDto login(LoginDto loginDto)
    {
        User user = userRepository.findByEmail(loginDto.getEmail())
            .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);

        return userMapper.toUserDto(user);
    }

    public UserDto register(RegisterDto registerDto)
    {
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());

        if (existingUser.isPresent())
            throw new AppException("Email already taken", HttpStatus.BAD_REQUEST);

        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirmation()))
            throw new AppException("Passwords do not match", HttpStatus.BAD_REQUEST);

        User user = userMapper.registerToUser(registerDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public User findByEmail(String email)
    {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return user;
    }
}
