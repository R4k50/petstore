package petstore.petstore.mappers;

import petstore.petstore.dtos.auth.RegisterDto;
import petstore.petstore.dtos.users.NewUserDto;
import petstore.petstore.dtos.users.PatchUserDto;
import petstore.petstore.dtos.users.UserDto;
import petstore.petstore.entities.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserDto toUserDto(User user);
    List<UserDto> toUserDtoList(List<User> users);

    @Mapping(target = "password", ignore = true)
    User registerToUser(RegisterDto registerDto);

    User newUserDtoToUser(NewUserDto newUserDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget User user, PatchUserDto patchUserDto);
}
