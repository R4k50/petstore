package petstore.petstore.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import petstore.petstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository
extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  Optional<Product> findByName(String name);
}
