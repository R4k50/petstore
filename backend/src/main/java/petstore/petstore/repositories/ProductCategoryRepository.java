package petstore.petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import petstore.petstore.entities.ProductCategory;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>
{
  Optional<ProductCategory> findByName(String name);
  ProductCategory getByName(String name);
}
