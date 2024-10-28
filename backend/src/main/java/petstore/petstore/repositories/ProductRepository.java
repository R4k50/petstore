package petstore.petstore.repositories;

import petstore.petstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
  Optional<Product> findByName(String name);
}
