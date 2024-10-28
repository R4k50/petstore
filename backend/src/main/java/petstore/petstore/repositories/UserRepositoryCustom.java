package petstore.petstore.repositories;

import petstore.petstore.dtos.users.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom
{
  Page<UserDto> findAll(String searchParam, String searchValue, Pageable pageable);
}
