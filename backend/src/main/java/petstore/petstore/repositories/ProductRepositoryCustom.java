package petstore.petstore.repositories;

import petstore.petstore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom
{
  Page<Product> findAll(String searchParam, String searchValue, Pageable pageable);
}
