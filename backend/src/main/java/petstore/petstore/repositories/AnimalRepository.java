package petstore.petstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import petstore.petstore.entities.Animal;

import java.util.Optional;

public interface AnimalRepository
extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {
  Optional<Animal> findByName(String name);
}
